package com.petko.commands;

import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand extends AbstractCommand {
    private static LogoutCommand instance;

    private LogoutCommand() {}

    public static synchronized LogoutCommand getInstance() {
        if(instance == null){
            instance = new LogoutCommand();
        }
        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        UserService.getInstance().logOut(request, login);
        redirectToLoginPage(request);
    }
}
