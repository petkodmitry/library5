package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnknownCommand extends AbstractCommand{
    private static UnknownCommand instance;

    private UnknownCommand() {}

    public static synchronized UnknownCommand getInstance() {
        if(instance == null){
            instance = new UnknownCommand();
        }
        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        setErrorMessage(request, "Команда не распознана или не задана!");
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_ERROR);
        setForwardPage(request, page);
    }
}
