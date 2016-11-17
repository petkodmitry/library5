package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractCommand implements Command{
    public void setErrorMessage(HttpServletRequest request, String message) {
        request.setAttribute(errorMessageAttribute, message);
    }

    public void setForwardPage(HttpServletRequest request, String page) {
        request.setAttribute(forwardPageAttribute, page);
    }

    protected void redirectToMainPage(HttpServletRequest request, String login) {
        String page;
        if (UserService.getInstance().isAdminUser(request, login)) {
            page = ResourceManager.getInstance().getProperty(Constants.PAGE_MAIN_ADMIN);
        } else {
            page = ResourceManager.getInstance().getProperty(Constants.PAGE_MAIN);
        }
        setForwardPage(request, page);
    }

    protected void redirectToLoginPage(HttpServletRequest request) {
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_INDEX);
        setForwardPage(request, page);
    }
}
