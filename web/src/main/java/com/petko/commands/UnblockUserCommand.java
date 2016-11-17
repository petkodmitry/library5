package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UnblockUserCommand extends AbstractCommand {
    private static UnblockUserCommand instance;

    private UnblockUserCommand() {}

    public static synchronized UnblockUserCommand getInstance() {
        if (instance == null) {
            instance = new UnblockUserCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserService service = UserService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (service.isAdminUser(request, login)) {
            String userLogin = request.getParameter("login");
            service.setBlockUser(request, userLogin, false);
            BlackListCommand.getInstance().execute(request, response);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
