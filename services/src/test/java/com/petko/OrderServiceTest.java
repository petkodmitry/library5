package com.petko;

import static org.mockito.Mockito.*;

import com.petko.dao.BookDao;
import com.petko.dao.OrderDao;
import com.petko.dao.UserDao;
import com.petko.entities.*;
import com.petko.services.OrderService;
import com.petko.utils.HibernateUtilLibrary;
import com.petko.vo.FullOrdersList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderServiceTest {
    public static OrderService orderService;
    public static OrderDao orderDao;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        orderService = OrderService.getInstance();
        orderDao = OrderDao.getInstance();
        request = mock(HttpServletRequest.class);
    }

    private int getTheLastOrderId() throws DaoException {
        List<OrdersEntity> list = orderDao.getAbsolutelyAll();
        Set<Integer> orderIds = list.stream().map(OrdersEntity::getOrderId).collect(Collectors.toSet());
        Object[] ids = orderIds.toArray();
        Arrays.sort(ids);
        return (int) ids[ids.length - 1];
    }

    private OrdersEntity saveAndGetNewOrderEntity() throws DaoException {
        Session currentSession = HibernateUtilLibrary.getHibernateUtil().getSession();
        Transaction transaction = currentSession.beginTransaction();

        OrdersEntity orderEntity = new OrdersEntity();
        orderEntity.setLogin("who");
        orderEntity.setBookId(1);
        orderEntity.setStatus(OrderStatus.ON_HAND.toString());
        orderEntity.setPlaceOfIssue(PlaceOfIssue.HOME.toString());
        orderEntity.setStartDate(new Date());
        orderEntity.setEndDate(new Date());
        orderDao.save(orderEntity);

        transaction.commit();
        int pastId = getTheLastOrderId();
        return orderDao.getById(pastId);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOrdersByLoginAndStatus1() {
        orderService.getOrdersByLoginAndStatus(null, null, null);
    }

    @Test
    public void testGetOrdersByLoginAndStatus2() {
        List<FullOrdersList> list = orderService.getOrdersByLoginAndStatus(request, null, null);
        Assert.assertNotNull(list);
    }

    @Test
    public void testGetOrdersByLoginAndStatus3() throws DaoException {
        UsersEntity user = UserDao.getInstance().getById(2);
        List<FullOrdersList> list = orderService.getOrdersByLoginAndStatus(request, user.getLogin(), OrderStatus.CLOSED);
        Assert.assertTrue(!list.isEmpty());
    }

    @Test
    public void testGetExpiredOrders1() {
        List<FullOrdersList> list = orderService.getExpiredOrders(null);
        Assert.assertNotNull(list);
    }

    @Test(expected = NullPointerException.class)
    public void testCloseOrder1() {
        orderService.closeOrder(null, null, -10_000);
    }

    @Test
    public void testCloseOrder2() throws DaoException {
        OrdersEntity ordersEntity = saveAndGetNewOrderEntity();
        orderService.closeOrder(request, null, ordersEntity.getOrderId());
    }

    @Test
    public void testCloseOrder3() throws DaoException {
        OrdersEntity ordersEntity = saveAndGetNewOrderEntity();
        ordersEntity.setStatus(OrderStatus.ORDERED.toString());
        orderService.closeOrder(request, ordersEntity.getLogin(), ordersEntity.getOrderId());
    }

    @Test(expected = NullPointerException.class)
    public void testOrderToHomeOrToRoom1() {
        orderService.orderToHomeOrToRoom(null, null, -10_000, null);
    }

    @Test
    public void testOrderToHomeOrToRoom2() {
        orderService.orderToHomeOrToRoom(request, null, -10_000, null);
    }

    @Test
    public void testOrderToHomeOrToRoom3() {
        orderService.orderToHomeOrToRoom(request, null, -10_000, true);
    }

    @Test
    public void testOrderToHomeOrToRoom4() throws DaoException {
        int startCountOfOrders = getTheLastOrderId();
        int bookId = 0;
        List<BooksEntity> bookList = BookDao.getInstance().getAbsolutelyAll();
        for (BooksEntity book : bookList) {
            if (!book.getIsBusy()) {
                bookId = book.getBookId();
                break;
            }
        }
        String userLogin = "";
        List<UsersEntity> userList = UserDao.getInstance().getAbsolutelyAll();
        for (UsersEntity userTemp : userList) {
            if (!userTemp.getIsBlocked() && !userTemp.getIsAdmin()) {
                userLogin = userTemp.getLogin();
                break;
            }
        }
        orderService.orderToHomeOrToRoom(request, userLogin, bookId, true);
        int endCountOfOrders = getTheLastOrderId();
        if (startCountOfOrders != endCountOfOrders) orderService.closeOrder(request, null, getTheLastOrderId());
    }

    @Test
    public void testProlongOrder1() {
        orderService.prolongOrder(null, null, -10_000);
    }

    @Test
    public void testProlongOrder2() {
        orderService.prolongOrder(request, null, -10_000);
    }

    @Test
    public void testProlongOrder3() throws DaoException {
        OrdersEntity orderEntity = saveAndGetNewOrderEntity();
        orderService.prolongOrder(request, orderEntity.getLogin(), orderEntity.getOrderId());
        orderService.closeOrder(request, null, orderEntity.getOrderId());
    }

    @Test(expected = NullPointerException.class)
    public void testProvideBook1() {
        orderService.provideBook(null, -10_000);
    }

    @Test
    public void testProvideBook2() {
        orderService.provideBook(request, -10_000);
    }

    @Test
    public void testProvideBook3() throws DaoException {
        OrdersEntity orderEntity = saveAndGetNewOrderEntity();
        String userLogin = "";
        List<UsersEntity> userList = UserDao.getInstance().getAbsolutelyAll();
        for (UsersEntity userTemp : userList) {
            if (!userTemp.getIsBlocked() && !userTemp.getIsAdmin()) {
                userLogin = userTemp.getLogin();
                break;
            }
        }
        int bookId = 0;
        List<BooksEntity> bookList = BookDao.getInstance().getAbsolutelyAll();
        for (BooksEntity book : bookList) {
            if (!book.getIsBusy()) {
                bookId = book.getBookId();
                break;
            }
        }
        orderEntity.setLogin(userLogin);
        orderEntity.setBookId(bookId);
        orderEntity.setStatus(OrderStatus.ORDERED.toString());
        orderService.provideBook(request, orderEntity.getOrderId());
        orderService.closeOrder(request, null, orderEntity.getOrderId());
    }

    @Test
    public void testCreateNewEntity1() {
        orderService.createNewEntity(null, -10_000, null, null, null, null);
    }

    @Test
    public void testGetById1() {
        OrdersEntity entity = orderService.getById(null, -10_000);
        Assert.assertNull(entity);
    }

    @Test
    public void testGetById2() {
        OrdersEntity entity = orderService.getById(request, -10_000);
        Assert.assertNull(entity);
    }
}
