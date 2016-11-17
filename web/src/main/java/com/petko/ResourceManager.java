package com.petko;

import java.util.ResourceBundle;

public class ResourceManager {
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private static ResourceManager ourInstance = new ResourceManager();

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    public String getProperty(String key){
        return bundle.getString(key);
    }
}
