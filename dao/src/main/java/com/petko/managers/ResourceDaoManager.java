package com.petko.managers;

import java.util.ResourceBundle;

public class ResourceDaoManager {
    private final ResourceBundle bundle = ResourceBundle.getBundle("mysql");
    private static ResourceDaoManager ourInstance = new ResourceDaoManager();

    private ResourceDaoManager() {}

    public static ResourceDaoManager getInstance() {
        return ourInstance;
    }

    public String getProperty(String key){
        return bundle.getString(key);
    }
}
