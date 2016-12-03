package com.petko;

import com.petko.dao.SeminarDao;
import com.petko.entities.SeminarsEntity;
import com.petko.entities.UsersEntity;
import com.petko.services.SeminarService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

public class SeminarServiceTest {
    public static SeminarService seminarService;
    public static SeminarDao seminarDao;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        seminarService = SeminarService.getInstance();
        seminarDao = SeminarDao.getInstance();
        request = mock(HttpServletRequest.class);
    }

    private int getTheLastSeminarId() throws DaoException {
        List<SeminarsEntity> list = seminarDao.getAbsolutelyAll();
        Set<Integer> seminarIds = list.stream().map(SeminarsEntity::getSeminarId).collect(Collectors.toSet());
        Object[] ids = seminarIds.toArray();
        Arrays.sort(ids);
        return (int) ids[ids.length - 1];
    }

    @Test(expected = NullPointerException.class)
    public void testAdd1() {
        seminarService.add(null, null);
    }

    @Test
    public void testAdd2() throws DaoException {
        SeminarsEntity newSeminar = new SeminarsEntity();
        newSeminar.setSeminarDate(new Date());
        newSeminar.setSubject("test");
        newSeminar.setUsers(new HashSet<>());
        seminarService.add(request, newSeminar);
        int seminarId = getTheLastSeminarId();
        UserServiceTest userServiceTest = new UserServiceTest();
        UserServiceTest.init();
        int userId = userServiceTest.getTheLastUserId();
        UsersEntity usersEntity = userServiceTest.userDao.getById(userId);
        String userLogin = usersEntity.getLogin();
        seminarService.subscribeToSeminar(request, userLogin, seminarId);
        seminarService.unSubscribeSeminar(request, userLogin, seminarId);
        seminarService.delete(request, seminarId);
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
