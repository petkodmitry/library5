package com.petko;

import com.petko.dao.UserDao;
import com.petko.entities.UsersEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoTest {
    public static UserDao userDao;

    @BeforeClass
    public static void init() {
        userDao = UserDao.getInstance();
    }

    @Test (expected = DaoException.class)
    public void testSave1() throws DaoException {
        userDao.save(null);
    }

    @Test (expected = DaoException.class)
    public void testSave2() throws DaoException {
        UsersEntity bookEntity = new UsersEntity();
        userDao.save(bookEntity);
    }

    @Test (expected = DaoException.class)
    public void testUpdate1() throws DaoException {
        userDao.update(null);
    }

    @Test
    public void testUpdate2() throws DaoException {
        UsersEntity bookEntity = userDao.getById(1);
        bookEntity.setFirstName("first1-1");
        userDao.update(bookEntity);
        bookEntity = userDao.getById(1);
        Assert.assertEquals("user fname must be 'first1-1'", "first1-1", bookEntity.getFirstName());
    }

    @Test (expected = DaoException.class)
    public void testDelete1() throws DaoException {
        userDao.delete(null);
    }

    @Test
    public void testGetAll1() throws DaoException {
        List<UsersEntity> list = userDao.getAll(-100, 0, "userId", "desc");
        Assert.assertTrue(!list.isEmpty());
    }

    @Test
    public void testGetTotal1() throws DaoException {
        Long count = userDao.getTotal();
        Assert.assertTrue(count >= 0);
    }

    @Test
    public void testGetByLogin1() throws DaoException {
        UsersEntity entity = userDao.getByLogin(null);
        Assert.assertNull(entity);
    }

    @Test
    public void testGetByLogin2() throws DaoException {
        UsersEntity entity = userDao.getByLogin("notExistedLogin");
        Assert.assertNull(entity);
    }

    @Test (expected = DaoException.class)
    public void testGetAllByCoupleLogins1() throws DaoException {
        userDao.getAllByCoupleLogins(null);
    }

    @Test (expected = DaoException.class)
    public void testGetAllByCoupleLogins2() throws DaoException {
        Set<String> logins = new HashSet<>();
        userDao.getAllByCoupleLogins(logins);
    }

    @Test
    public void testGetAllByCoupleLogins3() throws DaoException {
        Set<String> logins = new HashSet<>();
        logins.add("notExistedLogin");
        List<UsersEntity> list = userDao.getAllByCoupleLogins(logins);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetAllByBlockStatus1() throws DaoException {
        List<UsersEntity> list = userDao.getAllByBlockStatus(null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetAllByBlockStatus2() throws DaoException {
        List<UsersEntity> list = userDao.getAllByBlockStatus(false);
        Assert.assertTrue(!list.isEmpty());
    }
}
