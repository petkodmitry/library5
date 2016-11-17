package com.petko;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class Controller extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestHandler.processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestHandler.processRequest(request, response);
    }
}
