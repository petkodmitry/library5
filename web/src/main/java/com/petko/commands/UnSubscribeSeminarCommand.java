package com.petko.commands;

import com.petko.services.SeminarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UnSubscribeSeminarCommand extends AbstractCommand{
    private static UnSubscribeSeminarCommand instance;

    private UnSubscribeSeminarCommand() {
    }

    public static synchronized UnSubscribeSeminarCommand getInstance() {
        if (instance == null) {
            instance = new UnSubscribeSeminarCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        SeminarService service = SeminarService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        Integer seminarId = Integer.parseInt(request.getParameter("seminarId"));
        service.unSubscribeSeminar(request, login, seminarId);

        MySeminarsCommand.getInstance().execute(request, response);
    }
}
