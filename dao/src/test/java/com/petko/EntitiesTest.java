package com.petko;

import com.petko.entities.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class EntitiesTest {

    @BeforeClass
    public static void init() {
    }

    /**
     * BooksEntity
     */
    @Test
    public void testBookEntityEquals1() {
        BooksEntity entity1 = new BooksEntity();
        BooksEntity entity2 = entity1;
        Assert.assertEquals(entity1, entity2);
    }

    @Test
    public void testBookEntityEqual2() {
        BooksEntity entity1 = new BooksEntity();
        BooksEntity entity2 = null;
        Assert.assertTrue(!entity1.equals(entity2));
    }

    @Test
    public void testBookEntityEqual3() {
        BooksEntity entity1 = new BooksEntity();
        entity1.setBookId(1);
        BooksEntity entity2 = new BooksEntity();
        entity2.setBookId(2);
        Assert.assertTrue(!entity1.equals(entity2));
    }

    @Test
    public void testBookEntityEqual4() {
        BooksEntity entity1 = new BooksEntity();
        entity1.setTitle("1");
        BooksEntity entity2 = new BooksEntity();
        entity2.setTitle("2");
        Assert.assertTrue(!entity1.equals(entity2));
    }

    @Test
    public void testBookEntityEqual5() {
        BooksEntity entity1 = new BooksEntity();
        entity1.setAuthor("1");
        BooksEntity entity2 = new BooksEntity();
        entity2.setAuthor("2");
        Assert.assertTrue(!entity1.equals(entity2));
    }

    @Test
    public void testBookEntityEqual6() {
        BooksEntity entity1 = new BooksEntity();
        entity1.setIsBusy(true);
        BooksEntity entity2 = new BooksEntity();
        entity2.setIsBusy(false);
        Assert.assertTrue(!entity1.equals(entity2));
    }

    /**
     * OrdersEntity
     */
    @Test
    public void testOrderEntityEquals1() {
        OrdersEntity entity1 = new OrdersEntity();
        OrdersEntity entity2 = entity1;
        Assert.assertEquals(entity1, entity2);
    }

    @Test
    public void testOrderEntityEqual2() {
        OrdersEntity entity1 = new OrdersEntity();
        OrdersEntity entity2 = null;
        Assert.assertTrue(!entity1.equals(entity2));
    }

    @Test
    public void testOrderEntityEqual3() {
        OrdersEntity entity1 = new OrdersEntity();
        entity1.setOrderId(1);
        OrdersEntity entity2 = new OrdersEntity();
        entity2.setOrderId(2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setOrderId(1);
        entity1.setLogin("1");
        entity2.setLogin("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setLogin("1");
        entity1.setBookId(1);
        entity2.setBookId(2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setBookId(1);
        entity1.setStatus("1");
        entity2.setStatus("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setStatus("1");
        entity1.setPlaceOfIssue("1");
        entity2.setPlaceOfIssue("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setPlaceOfIssue("1");
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1);
        entity1.setStartDate(date1);
        entity2.setStartDate(date2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setStartDate(date1);
        entity1.setEndDate(date1);
        entity2.setEndDate(date2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setEndDate(date1);
        Assert.assertTrue(entity1.equals(entity2));
        entity1.hashCode();
        entity2.hashCode();
    }

    /**
     * SeminarsEntity
     */
    @Test
    public void testSeminarEntityEqual3() {
        SeminarsEntity entity1 = new SeminarsEntity();
        entity1.setSeminarId(1);
        SeminarsEntity entity2 = new SeminarsEntity();
        entity2.setSeminarId(2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setSeminarId(1);
        entity1.setSubject("1");
        entity2.setSubject("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setSubject("1");
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1);
        entity1.setSeminarDate(date1);
        entity2.setSeminarDate(date2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setSeminarDate(date1);
        Assert.assertTrue(entity1.equals(entity2));
    }

    /**
     * UsersEntity
     */
    @Test
    public void testUserEntityEqual3() {
        UsersEntity entity1 = new UsersEntity();
        entity1.setUserId(1);
        UsersEntity entity2 = new UsersEntity();
        entity2.setUserId(2);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setUserId(1);
        entity1.setFirstName("1");
        entity2.setFirstName("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setFirstName("1");
        entity1.setLastName("1");
        entity2.setLastName("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setLastName("1");
        entity1.setLogin("1");
        entity2.setLogin("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setLogin("1");
        entity1.setPassword("1");
        entity2.setPassword("2");
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setPassword("1");
        entity1.setIsAdmin(true);
        entity2.setIsAdmin(false);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setIsAdmin(true);
        entity1.setIsBlocked(true);
        entity2.setIsBlocked(false);
        Assert.assertTrue(!entity1.equals(entity2));
        entity2.setIsBlocked(true);
        Assert.assertTrue(entity1.equals(entity2));
    }

    /**
     * OrderStatus
     */
    @Test
    public void testOrderStatus1() {
        Assert.assertEquals(OrderStatus.ORDERED, OrderStatus.getOrderStatus("Открыт"));
        Assert.assertEquals(OrderStatus.CLOSED, OrderStatus.getOrderStatus("Закрыт"));
        Assert.assertEquals(OrderStatus.ON_HAND, OrderStatus.getOrderStatus("На руках"));
        Assert.assertNull(OrderStatus.getOrderStatus("Status"));
        Assert.assertEquals("На руках", OrderStatus.ON_HAND.toString());
        Assert.assertEquals("Закрыт", OrderStatus.CLOSED.toString());
        Assert.assertEquals("Открыт", OrderStatus.ORDERED.toString());
    }

    /**
     * PlaceOfIssue
     */
    @Test
    public void testPlaceOfIssue1() {
        Assert.assertEquals(PlaceOfIssue.HOME, PlaceOfIssue.getPlaceOfIssue("Абонемент"));
        Assert.assertEquals(PlaceOfIssue.READING_ROOM, PlaceOfIssue.getPlaceOfIssue("Читальный зал"));
        Assert.assertNull(PlaceOfIssue.getPlaceOfIssue("Place"));
        Assert.assertEquals("Абонемент", PlaceOfIssue.HOME.toString());
        Assert.assertEquals("Читальный зал", PlaceOfIssue.READING_ROOM.toString());
    }
}
