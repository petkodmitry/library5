package com.petko;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class ActiveUsers {
    private static HashSet<String> connectedUsers = new LinkedHashSet<String>();

    public static synchronized HashSet<String> getSet() {
        if (connectedUsers.isEmpty()) return null;
        else return connectedUsers;
    }

    public static synchronized void addUser(String login) {
        connectedUsers.add(login);
    }

    public static synchronized void removeUser(String login) {
        if (isUserActive(login)) connectedUsers.remove(login);
    }

    public static synchronized boolean isUserActive(String login) {
        return !(login == null) && !connectedUsers.isEmpty() && connectedUsers.contains(login);
    }

    private ActiveUsers() {}
}
