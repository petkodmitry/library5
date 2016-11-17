package com.petko;

import com.petko.commands.Command;
import com.petko.commands.CommandType;
import com.petko.commands.UnknownCommand;
import com.petko.constants.Constants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public class RequestHandler {
    private RequestHandler() {}

    public static void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        Command command = UnknownCommand.getInstance();

        /*if ("orderToHome".equals(cmd)) {
            cmd = (String) request.getAttribute("cmd");
            request.removeAttribute("cmd");
        }*/

        /*Map<String, String[]> testMap = request.getParameterMap();
        Enumeration<String> test = request.getAttributeNames();
        request.removeAttribute("doOrderToHome");
        testMap = request.getParameterMap();
        test = request.getAttributeNames();*/

        if (cmd != null) command = CommandType.getCommand(cmd);

        command.execute(request, response);
        /*if ("orderToHome".equals(cmd) *//*|| "prolongOrder".equals(cmd)*//*) {
//            response.sendRedirect(ResourceManager.getInstance().getProperty(Constants.PAGE_MY_ORDERS));
            // перенаправляет, но не показывает errorMessage
            response.sendRedirect("controller?cmd=searchbook");
        } else {*/
            RequestDispatcher dispatcher = request.getServletContext().
                    getRequestDispatcher((String) request.getAttribute(Constants.FORWARD_PAGE_ATTRIBUTE));
            dispatcher.forward(request, response);
        /*}*/
    }
}
