package com.petko;

import com.petko.entities.UsersEntity;
import com.petko.services.UserService;
import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserServiceTest {
    public static UserService userService;
    public static HttpServletRequest request;

    @BeforeClass
    public static void init() {
        userService = UserService.getInstance();
        request = mock(HttpServletRequest.class);
    }

    @Test(expected = NullPointerException.class)
    public void testAdd1() {
        userService.add(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAll1() {
        userService.getAll(null, null);
    }

    @Test
    public void testGetAll2() {
        List<UsersEntity> list = userService.getAll(request, "1");
        Assert.assertTrue(!list.isEmpty());
    }

    @Test (expected = NullPointerException.class)
    public void testGetAll3() {
        userService.getAll(request, null);
    }

    @Test (expected = NullPointerException.class)
    public void testLogOut1() {
        userService.logOut(null, null);
    }

    @Test
    public void testIsLoginSuccess1() {
        boolean result = userService.isLoginSuccess(null, null, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsLoginSuccess2() {
        boolean result = userService.isLoginSuccess(null, "1", "2");
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAdminUser() {
        boolean result = userService.isAdminUser(null, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsLoginExists() {
        boolean result = userService.isLoginExists(null, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAllPasswordRulesFollowed1() {
        boolean result = userService.isAllPasswordRulesFollowed(null, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAllPasswordRulesFollowed2() {
        boolean result = userService.isAllPasswordRulesFollowed("1", "2");
        Assert.assertTrue(!result);
    }

    @Test
    public void testSetBlockUser() {
        userService.setBlockUser(null, null, false);
    }

    @Test
    public void testGetUsersByBlock() {
        List<UsersEntity> list = userService.getUsersByBlock(null, false);
        Assert.assertTrue(list != null);
    }

    @Test
    public void testIsAllRegisterDataEntered1() {
        boolean result = userService.isAllRegisterDataEntered(null, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAllRegisterDataEntered2() {
        boolean result = userService.isAllRegisterDataEntered(new UsersEntity(), null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAllRegisterDataEntered3() {
        UsersEntity entity = new UsersEntity();
        entity.setFirstName("f");
        entity.setLastName("l");
        entity.setLogin("login");
        entity.setPassword("psw");
        boolean result = userService.isAllRegisterDataEntered(entity, null);
        Assert.assertTrue(!result);
    }

    @Test
    public void testIsAllRegisterDataEntered4() {
        UsersEntity entity = new UsersEntity();
        entity.setFirstName("f");
        entity.setLastName("l");
        entity.setLogin("login");
        entity.setPassword("psw");
        boolean result = userService.isAllRegisterDataEntered(entity, "psw1");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAllRegisterDataEntered5() {
        UsersEntity entity = new UsersEntity();
        entity.setFirstName("f");
        entity.setLastName("l");
        entity.setLogin("login");
        entity.setPassword("psw");
        boolean result = userService.isAllRegisterDataEntered(entity, "psw");
        Assert.assertTrue(result);
    }

    @Test
    public void testSetAllDataOfEntity1() {
        UsersEntity entity = new UsersEntity();
        entity = userService.setAllDataOfEntity(entity, null, null, null, null, false, false);
        Assert.assertTrue(entity.getFirstName() == null && entity.getLastName() == null &&
                entity.getLogin() == null && entity.getPassword() == null &&
                !entity.getIsAdmin() && !entity.getIsBlocked());
    }

    @Test
    public void testSetAllDataOfEntity2() {
        UsersEntity entity = new UsersEntity();
        entity = userService.setAllDataOfEntity(entity, "f", "l", "login", "psw", true, true);
        Assert.assertTrue(entity.getFirstName().equals("f") && entity.getLastName().equals("l") &&
                entity.getLogin().equals("login") && entity.getPassword().equals("psw") &&
                entity.getIsAdmin() && entity.getIsBlocked());
    }
}
