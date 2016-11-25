package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class LoginCommand extends AbstractCommand {
    private static LoginCommand instance;

    private LoginCommand() {}

    public static synchronized LoginCommand getInstance() {
        if(instance == null){
            instance = new LoginCommand();
        }
        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // deleting all session attributes, except "user"
        Enumeration<String> attributesNames = session.getAttributeNames();
        while (attributesNames.hasMoreElements()) {
            String attr = attributesNames.nextElement();
            if (!"user".equals(attr)) session.removeAttribute(attr);
        }

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (session.getAttribute("user") != null) {
            login = (String) session.getAttribute("user");
            redirectToMainPage(request, login);
        } else if (!"".equals(login) && UserService.getInstance().isLoginSuccess(request, login, password)) {
            session.setAttribute("user", login);
            redirectToMainPage(request, login);
        } else {
            if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null && login != null && !"".equals(login)) {
                setErrorMessage(request, "Неверный логин или пароль!");
            }
            redirectToLoginPage(request);
        }
    }
}
