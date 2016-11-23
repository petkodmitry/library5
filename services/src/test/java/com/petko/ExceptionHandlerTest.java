package com.petko;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import static org.mockito.Mockito.mock;

public class ExceptionHandlerTest {
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void testProcessException1() {
        ExceptionsHandler.processException(request, new SQLException());
        ExceptionsHandler.processException(request, new ClassNotFoundException());
    }

    @Test (expected = NullPointerException.class)
    public void testProcessException2() {
        ExceptionsHandler.processException(null, null);
    }
}
