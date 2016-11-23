package com.petko;

import com.petko.dao.OrderDao;
import com.petko.entities.BooksEntity;
import com.petko.entities.OrderStatus;
import com.petko.entities.OrdersEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class OrderDaoTest {
    public static OrderDao orderDao;

    @BeforeClass
    public static void init() {
        orderDao = OrderDao.getInstance();
    }

    @Test (expected = DaoException.class)
    public void testSave1() throws DaoException {
        orderDao.save(null);
    }

    @Test (expected = DaoException.class)
    public void testSave2() throws DaoException {
        OrdersEntity bookEntity = new OrdersEntity();
        orderDao.save(bookEntity);
    }

    @Test (expected = DaoException.class)
    public void testUpdate1() throws DaoException {
        orderDao.update(null);
    }

    @Test
    public void testUpdate2() throws DaoException {
        OrdersEntity bookEntity = orderDao.getById(1);
        Date date = new Date();
        bookEntity.setEndDate(date);
        orderDao.update(bookEntity);
        bookEntity = orderDao.getById(1);
        Assert.assertEquals("order date must be today", date, bookEntity.getEndDate());
    }

    @Test (expected = DaoException.class)
    public void testDelete1() throws DaoException {
        orderDao.delete(null);
    }

    @Test (expected = DaoException.class)
    public void testGetOrdersByLoginAndStatus1() throws DaoException {
        orderDao.getOrdersByLoginAndStatus(null, null);
    }

    @Test
    public void testGetOrdersByLoginAndStatus2() throws DaoException {
        List<OrdersEntity> list = orderDao.getOrdersByLoginAndStatus("notExistedLogin", null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetOrdersByLoginAndStatus3() throws DaoException {
        List<OrdersEntity> list = orderDao.getOrdersByLoginAndStatus(null, OrderStatus.CLOSED);
        Assert.assertTrue(!list.isEmpty());
    }

    @Test
    public void testGetOrdersByLoginAndStatus4() throws DaoException {
        List<OrdersEntity> list = orderDao.getOrdersByLoginAndStatus("notExistedLogin", OrderStatus.CLOSED);
        Assert.assertTrue(list.isEmpty());
    }

    @Test (expected = DaoException.class)
    public void testGetOrdersByStatusAndEndDate1() throws DaoException {
        orderDao.getOrdersByStatusAndEndDate(null, null);
    }

    @Test
    public void testGetOrdersByStatusAndEndDate2() throws DaoException {
        List<OrdersEntity> list = orderDao.getOrdersByStatusAndEndDate(OrderStatus.ORDERED, null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test (expected = DaoException.class)
    public void testGetOrdersByStatusAndEndDate3() throws DaoException {
        orderDao.getOrdersByStatusAndEndDate(null, new Date());
    }

    @Test (expected = DaoException.class)
    public void testGetOrdersByLoginBookIdStatuses1() throws DaoException {
        orderDao.getOrdersByLoginBookIdStatuses(null, null, null);
    }

    @Test (expected = DaoException.class)
    public void testGetOrdersByLoginBookIdStatuses2() throws DaoException {
        orderDao.getOrdersByLoginBookIdStatuses("notExistedLogin", new BooksEntity(), null);
    }

    @Test
    public void testGetOrdersByLoginBookIdStatuses3() throws DaoException {
        String[] statuses = {OrderStatus.ORDERED.toString(), OrderStatus.ON_HAND.toString()};
        List<OrdersEntity> list = orderDao.getOrdersByLoginBookIdStatuses("notExistedLogin", new BooksEntity(), statuses);
        Assert.assertTrue(list.isEmpty());
    }
}
