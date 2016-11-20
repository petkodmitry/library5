package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OrderToReadingRoomCommand extends AbstractCommand{
    private static OrderToReadingRoomCommand instance;

    private OrderToReadingRoomCommand() {
    }

    public static synchronized OrderToReadingRoomCommand getInstance() {
        if (instance == null) {
            instance = new OrderToReadingRoomCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        OrderService service = OrderService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        service.orderToHomeOrToRoom(request, login, bookId, false);

        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_SEARCH_BOOK_FOR_USER);
        setForwardPage(request, page);
    }
}
