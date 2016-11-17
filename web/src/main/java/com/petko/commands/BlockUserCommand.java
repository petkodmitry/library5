package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.services.OrderService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BlockUserCommand extends AbstractCommand {
    private static BlockUserCommand instance;

    private BlockUserCommand() {}

    public static synchronized BlockUserCommand getInstance() {
        if (instance == null) {
            instance = new BlockUserCommand();
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
            service.setBlockUser(request, userLogin, true);
            ExpiredOrdersCommand.getInstance().execute(request, response);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
