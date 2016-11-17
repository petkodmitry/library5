package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.services.OrderService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProvideBookCommand extends AbstractCommand {
    private static ProvideBookCommand instance;

    private ProvideBookCommand() {
    }

    public static synchronized ProvideBookCommand getInstance() {
        if (instance == null) {
            instance = new ProvideBookCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        OrderService service = OrderService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (UserService.getInstance().isAdminUser(request, login)) {
            Integer orderId = Integer.parseInt(request.getParameter("orderId"));
            service.provideBook(request, orderId);
            WaitingOrdersCommand.getInstance().execute(request, response);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
