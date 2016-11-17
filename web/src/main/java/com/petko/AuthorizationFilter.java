package com.petko;

import com.petko.commands.CommandType;
import com.petko.constants.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        CommandType commandType = CommandType.getCommandType(request.getParameter("cmd"));
        if (commandType != CommandType.REGISTER &&
                (!CommandType.LOGIN.equals(commandType) && !ActiveUsers.isUserActive((String) session.getAttribute("user")))) {
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_INDEX);
            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            dispatcher.forward(request, response);
            session.invalidate();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
    }
}
