package com.petko.commands;

import com.petko.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CancelUserOrderCommand extends AbstractCommand{
    private static CancelUserOrderCommand instance;

    private CancelUserOrderCommand() {
    }

    public static synchronized CancelUserOrderCommand getInstance() {
        if (instance == null) {
            instance = new CancelUserOrderCommand();
        }
        return instance;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        OrderService service = OrderService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        service.closeOrder(request, login, orderId);

        MyOrdersCommand.getInstance().execute(request, response);
    }
}
