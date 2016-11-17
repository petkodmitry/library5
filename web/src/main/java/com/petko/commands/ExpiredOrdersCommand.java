package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.OrderStatus;
import com.petko.services.OrderService;
import com.petko.services.UserService;
import com.petko.vo.AnyStatusOrdersList;
import com.petko.vo.FullOrdersList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ExpiredOrdersCommand extends AbstractCommand{
    private static ExpiredOrdersCommand instance;

    private ExpiredOrdersCommand() {}

    public static synchronized ExpiredOrdersCommand getInstance() {
        if (instance == null) {
            instance = new ExpiredOrdersCommand();
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
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_EXPIRED_ORDERS);
            List<FullOrdersList> expiredOrdersList = service.getExpiredOrders(request);
            session.setAttribute("expiredOrdersList", expiredOrdersList);
            setForwardPage(request, page);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
