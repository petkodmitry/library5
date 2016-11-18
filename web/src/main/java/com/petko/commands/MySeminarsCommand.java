package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities2.SeminarsEntity;
import com.petko.services.SeminarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MySeminarsCommand extends AbstractCommand{
    private static MySeminarsCommand instance;

    private MySeminarsCommand() {
    }

    public static synchronized MySeminarsCommand getInstance() {
        if (instance == null) {
            instance = new MySeminarsCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        SeminarService service = SeminarService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_MY_SEMINARS);
        List<SeminarsEntity> mySeminarsList;
        mySeminarsList = service.getSeminarsByLogin(request, login);
        session.setAttribute("mySeminars", mySeminarsList);

        setForwardPage(request, page);
    }
}
