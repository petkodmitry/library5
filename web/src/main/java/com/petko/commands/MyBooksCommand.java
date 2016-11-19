package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.OrderStatus;
import com.petko.services.OrderService;
import com.petko.vo.OrderForMyOrdersList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MyBooksCommand extends AbstractCommand{
    private static MyBooksCommand instance;

    private MyBooksCommand() {
    }

    public static synchronized MyBooksCommand getInstance() {
        if (instance == null) {
            instance = new MyBooksCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        OrderService service = OrderService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_MY_BOOKS);
        List<OrderForMyOrdersList> myBooksList;
        myBooksList = service.getOrdersByLoginAndStatus(request, login, OrderStatus.ON_HAND);
        session.setAttribute("myBooksList", myBooksList);

        setForwardPage(request, page);
    }
}
