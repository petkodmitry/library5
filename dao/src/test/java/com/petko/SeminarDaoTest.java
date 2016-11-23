package com.petko;

import com.petko.dao.SeminarDao;
import com.petko.entities.SeminarsEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class SeminarDaoTest {
    public static SeminarDao seminarDao;

    @BeforeClass
    public static void init() {
        seminarDao = SeminarDao.getInstance();
    }

    @Test (expected = DaoException.class)
    public void testSave1() throws DaoException {
        seminarDao.save(null);
    }

    @Test (expected = DaoException.class)
    public void testSave2() throws DaoException {
        SeminarsEntity bookEntity = new SeminarsEntity();
        seminarDao.save(bookEntity);
    }

    @Test (expected = DaoException.class)
    public void testUpdate1() throws DaoException {
        seminarDao.update(null);
    }

    @Test
    public void testUpdate2() throws DaoException {
        SeminarsEntity bookEntity = seminarDao.getById(1);
        bookEntity.setSubject("book1-1");
        seminarDao.update(bookEntity);
        bookEntity = seminarDao.getById(1);
        Assert.assertEquals("seminar subject must be 'book1-1'", "book1-1", bookEntity.getSubject());
    }

    @Test (expected = DaoException.class)
    public void testDelete1() throws DaoException {
        seminarDao.delete(null);
    }

    @Test
    public void testGetSeminarsByLogin1() throws DaoException {
        List<SeminarsEntity> list = seminarDao.getSeminarsByLogin(null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetSeminarsByLogin2() throws DaoException {
        List<SeminarsEntity> list = seminarDao.getSeminarsByLogin("notExistingLogin");
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetAll1() throws DaoException {
        List<SeminarsEntity> list = seminarDao.getAll();
        Assert.assertTrue(!list.isEmpty());
    }

    @Test
    public void testGetAbsolutelyAll1() throws DaoException {
        List<SeminarsEntity> list = seminarDao.getAbsolutelyAll();
        Assert.assertNotNull(list);
    }
}
