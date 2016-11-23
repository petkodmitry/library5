package com.petko;

import com.petko.dao.BookDao;
import com.petko.entities.BooksEntity;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDaoTest {
    public static BookDao bookDao;

    @BeforeClass
    public static void init() {
        bookDao = BookDao.getInstance();
    }

    @Test (expected = DaoException.class)
    public void testSave1() throws DaoException {
        bookDao.save(null);
    }

    @Test
    public void testSave2() throws DaoException {
        BooksEntity bookEntity = new BooksEntity();
        bookDao.save(bookEntity);
    }

    @Test (expected = DaoException.class)
    public void testUpdate1() throws DaoException {
        bookDao.update(null);
    }

    @Test
    public void testUpdate2() throws DaoException {
        BooksEntity bookEntity = bookDao.getById(1);
        bookEntity.setTitle("book1-1");
        bookDao.update(bookEntity);
        bookEntity = bookDao.getById(1);
        Assert.assertSame("book title must be 'book1-1'", "book1-1", bookEntity.getTitle());
    }

    @Test (expected = DaoException.class)
    public void testDelete1() throws DaoException {
        bookDao.delete(null);
    }

    @Test (expected = DaoException.class)
    public void testGetAllByCoupleIds1() throws DaoException {
        List<BooksEntity> list = bookDao.getAllByCoupleIds(null);
        Assert.assertNotNull(list);
    }

    @Test
    public void testGetAllByCoupleIds2() throws DaoException {
        Set<Integer> idset = new HashSet<>();
        idset.add(1000);
        idset.add(2000);
        List<BooksEntity> list = bookDao.getAllByCoupleIds(idset);
        Assert.assertNotNull(list);
    }

    @Test
    public void testGetBooksByTitleOrAuthorAndStatus1() throws DaoException {
        List<BooksEntity> list = bookDao.getBooksByTitleOrAuthorAndStatus(null, null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetBooksByTitleOrAuthorAndStatus2() throws DaoException {
        List<BooksEntity> list = bookDao.getBooksByTitleOrAuthorAndStatus(null, true);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetBooksByTitleAndAuthorAndStatus1() throws DaoException {
        List<BooksEntity> list = bookDao.getBooksByTitleAndAuthorAndStatus(null, null, null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetBooksByTitleAndAuthorAndStatus2() throws DaoException {
        List<BooksEntity> list = bookDao.getBooksByTitleAndAuthorAndStatus(null, null, false);
        Assert.assertNotNull(list);
    }
}
