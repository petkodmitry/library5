package com.petko.commands;

import com.petko.Controller;
import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

public class OrderToHomeCommand extends AbstractCommand{
    private static OrderToHomeCommand instance;

    private OrderToHomeCommand() {
    }

    public static synchronized OrderToHomeCommand getInstance() {
        if (instance == null) {
            instance = new OrderToHomeCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String str = (String) request.getAttribute("orderedToHome");
//        if (!"yes".equals(str)) {
            OrderService service = OrderService.getInstance();
            HttpSession session = request.getSession();
            String login = (String) session.getAttribute("user");
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            service.orderToHomeOrToRoom(request, login, bookId, true);
//        }

        /*try {
            request.setAttribute("cmd", "searchbook");
            new Controller().doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

//        request.setAttribute("orderedToHome", "yes");
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_SEARCH_BOOK_FOR_USER);
        setForwardPage(request, page);
    }
}
