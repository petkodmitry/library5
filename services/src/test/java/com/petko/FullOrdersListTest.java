package com.petko;

import com.petko.vo.FullOrdersList;
import org.junit.BeforeClass;
import org.junit.Test;

//import javax.servlet.http.HttpServletRequest;
//import static org.mockito.Mockito.mock;

public class FullOrdersListTest {
//    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
//        request = mock(HttpServletRequest.class);
    }

    @Test
    public void testNewFullOrdersList1() {
        FullOrdersList list = new FullOrdersList(-1_000, null, -1_000, null, null, null);
        list.getOrderId();
        list.getTitle();
        list.getAuthor();
        list.getBookId();
        list.getDelayDays();
        list.getStartDate();
        list.getEndDate();
        list.getLogin();
        list.getPlace();
        list.isBlocked();
    }
}
