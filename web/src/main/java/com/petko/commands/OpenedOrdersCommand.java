package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities2.OrderStatus;
import com.petko.services.OrderService;
import com.petko.services.UserService;
import com.petko.vo.AnyStatusOrdersList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OpenedOrdersCommand extends AbstractCommand{
    private static OpenedOrdersCommand instance;

    private OpenedOrdersCommand() {}

    public static synchronized OpenedOrdersCommand getInstance() {
        if (instance == null) {
            instance = new OpenedOrdersCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = UserService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (userService.isAdminUser(request, login)) {
            OrderService service = OrderService.getInstance();
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_OPENED_ORDERS);
            List<AnyStatusOrdersList> openedOrdersList = service.getOrdersByStatus(request, OrderStatus.ON_HAND);
            session.setAttribute("openedOrdersList", openedOrdersList);
            setForwardPage(request, page);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
