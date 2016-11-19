package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.entities.OrderStatus;
import com.petko.services.OrderService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CloseOrderCommand extends AbstractCommand{
    private static CloseOrderCommand instance;

    private CloseOrderCommand() {}

    public static synchronized CloseOrderCommand getInstance() {
        if (instance == null) {
            instance = new CloseOrderCommand();
        }
        return instance;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        OrderService service = OrderService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");

        if (UserService.getInstance().isAdminUser(request, login)) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = service.getById(request, orderId).getStatus();
            service.closeOrder(request, null, orderId);
            if (OrderStatus.ORDERED.toString().equals(status)) {
                WaitingOrdersCommand.getInstance().execute(request, response);
            } else if (OrderStatus.ON_HAND.toString().equals(status)) {
                OpenedOrdersCommand.getInstance().execute(request, response);
            }
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
