package com.petko;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

public class ActiveUsersTest {

    @BeforeClass
    public static void init() {
    }

    @Test
    public void testGetSet1() {
        ActiveUsers.getSet();
    }

    @Test
    public void testAddUser1() {
        ActiveUsers.addUser(null);
    }

    @Test
    public void testAddUser2() {
        ActiveUsers.addUser("notToBe");
    }

    @Test
    public void testRemoveUser1() {
        ActiveUsers.removeUser("notToBe");
    }

    @Test
    public void testIsUserActive1() {
        boolean active = ActiveUsers.isUserActive(null);
        Assert.assertTrue(!active);
    }

    @Test
    public void testIsUserActive2() {
        boolean active = ActiveUsers.isUserActive("user1");
        Assert.assertTrue(!active);
    }
}
