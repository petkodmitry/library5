package com.petko;

import com.petko.dao.BookDao;
import com.petko.entities.BooksEntity;
import com.petko.entities.UsersEntity;
import com.petko.services.BookService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookServiceTest {
    public static BookService bookService;
    public static BookDao bookDao;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        bookService = BookService.getInstance();
        bookDao = BookDao.getInstance();
        request = mock(HttpServletRequest.class);
    }

    private int getTheLastBookId() throws DaoException {
        List<BooksEntity> list = bookDao.getAbsolutelyAll();
        Set<Integer> bookIds = list.stream().map(BooksEntity::getBookId).collect(Collectors.toSet());
        Object[] ids = bookIds.toArray();
        Arrays.sort(ids);
        return (int) ids[ids.length - 1];
    }

    @Test(expected = NullPointerException.class)
    public void testAdd1() {
        bookService.add(null, null);
    }

    @Test
    public void testSearchBooksByTitleOrAuthor1() {
        List<BooksEntity> list = bookService.searchBooksByTitleOrAuthor(null, null, null);
        Assert.assertTrue(list.isEmpty());
        list = bookService.searchBooksByTitleOrAuthor(request, null, "noUserExist");
        Assert.assertTrue(list.isEmpty());
        list = bookService.searchBooksByTitleOrAuthor(request, "noBook", "noUserExist");
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testSearchBooksByTitleOrAuthor2() throws DaoException {
        UserServiceTest.init();
        List<UsersEntity> usersList = UserServiceTest.userDao.getAbsolutelyAll();
        UsersEntity user = null;
        for (UsersEntity user2 : usersList) {
            if (!user2.getIsAdmin()) {
                user = user2;
                break;
            }
        }
        List<BooksEntity> list = bookService.searchBooksByTitleOrAuthor(request, "a", user.getLogin());
        Assert.assertNotNull(list);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBook1() {
        bookService.deleteBook(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBook2() {
        bookService.deleteBook(request, null);
    }

    @Test
    public void testDeleteBook3() throws DaoException {
        BooksEntity newBook = new BooksEntity();
        newBook.setAuthor("test");
        newBook.setTitle("test");
        newBook.setIsBusy(false);
        bookService.add(request, newBook);
        int id = getTheLastBookId();
        bookService.deleteBook(request, id);
    }
}
