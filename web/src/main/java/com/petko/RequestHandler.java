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

public class RequestHandler {
    private RequestHandler() {
    }

    public static void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        Command command = UnknownCommand.getInstance();

        if (cmd != null) command = CommandType.getCommand(cmd);

        command.execute(request, response);
        RequestDispatcher dispatcher = request.getServletContext().
                getRequestDispatcher((String) request.getAttribute(Constants.FORWARD_PAGE_ATTRIBUTE));
        dispatcher.forward(request, response);
    }
}
