package com.petko;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class ActiveUsers {
    private static HashSet<String> connectedUsers = new LinkedHashSet<String>();

    /**
     * gives Set of connected Users
     * @return Set of connected Users
     */
    public static synchronized HashSet<String> getSet() {
        if (connectedUsers.isEmpty()) return null;
        else return connectedUsers;
    }

    /**
     * adds login to connected Users Set
     * @param login to be added
     */
    public static synchronized void addUser(String login) {
        connectedUsers.add(login);
    }

    /**
     * removes login from connected Users Set
     * @param login to be deleted
     */
    public static synchronized void removeUser(String login) {
        if (isUserActive(login)) connectedUsers.remove(login);
    }

    /**
     * if connected Users Set contains specific Login
     * @param login to be checked
     * @return true or false
     */
    public static synchronized boolean isUserActive(String login) {
        return !(login == null) && !connectedUsers.isEmpty() && connectedUsers.contains(login);
    }

    private ActiveUsers() {}
}
