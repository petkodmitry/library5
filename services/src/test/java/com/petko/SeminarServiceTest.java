package com.petko;

import com.petko.entities.SeminarsEntity;
import com.petko.services.SeminarService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.Mockito.mock;

public class SeminarServiceTest {
    public static SeminarService seminarService;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        seminarService = SeminarService.getInstance();
        request = mock(HttpServletRequest.class);
    }

    @Test(expected = NullPointerException.class)
    public void testAdd1() {
        seminarService.add(null, null);
    }

    @Test
    public void testGetSeminarsByLogin1() {
        List<SeminarsEntity> list = seminarService.getSeminarsByLogin(null, null);
        Assert.assertTrue(list.isEmpty());
    }

    @Test (expected = NullPointerException.class)
    public void testSubscribeToSeminar1() {
        seminarService.subscribeToSeminar(null, null, -1_000);
    }

    @Test
    public void testSubscribeToSeminar2() {
        seminarService.subscribeToSeminar(request, null, -1_000);
    }

    @Test (expected = NullPointerException.class)
    public void testUnSubscribeToSeminar1() {
        seminarService.unSubscribeSeminar(null, null, -1_000);
    }

    @Test
    public void testUnSubscribeToSeminar2() {
        seminarService.unSubscribeSeminar(request, null, -1_000);
    }

    @Test
    public void testAvailableSeminarsForLogin1() {
        List<SeminarsEntity> list = seminarService.availableSeminarsForLogin(null, null);
        Assert.assertTrue(list != null);
    }

    @Test
    public void testGetAll1() {
        List<SeminarsEntity> list = seminarService.getAll(null);
        Assert.assertTrue(list != null);
    }

    @Test (expected = NullPointerException.class)
    public void testDelete1() {
        seminarService.delete(null, -1_000);
    }

    @Test
    public void testDelete2() {
        seminarService.delete(request, -1_000);
    }

    @Test
    public void testGetById1() {
        SeminarsEntity result = seminarService.getById(null, -1_000);
        Assert.assertNull(result);
    }

    @Test
    public void testGetById2() {
        SeminarsEntity result = seminarService.getById(request, -1_000);
        Assert.assertNull(result);
    }
}
