package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.SeminarsEntity;
import com.petko.services.SeminarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ChooseSeminarsCommand extends AbstractCommand{
    private static ChooseSeminarsCommand instance;

    private ChooseSeminarsCommand() {
    }

    public static synchronized ChooseSeminarsCommand getInstance() {
        if (instance == null) {
            instance = new ChooseSeminarsCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        SeminarService service = SeminarService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_CHOOSE_SEMINARS);
        List<SeminarsEntity> availableSeminarsList;
        availableSeminarsList = service.availableSeminarsForLogin(request, login);
        session.setAttribute("availableSeminars", availableSeminarsList);

        setForwardPage(request, page);
    }
}
