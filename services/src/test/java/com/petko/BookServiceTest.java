package com.petko;

import com.petko.entities.BooksEntity;
import com.petko.services.BookService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BookServiceTest {
    public static BookService bookService;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        bookService = BookService.getInstance();
        request = mock(HttpServletRequest.class);
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

    @Test(expected = NullPointerException.class)
    public void testDeleteBook1() {
        bookService.deleteBook(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBook2() {
        bookService.deleteBook(request, null);
    }

    @Test
    public void testDeleteBook3() {
        bookService.deleteBook(request, 10_000);
    }
}
