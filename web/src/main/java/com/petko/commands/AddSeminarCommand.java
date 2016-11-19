package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.SeminarsEntity;
import com.petko.services.SeminarService;
import com.petko.services.UserService;
import com.sun.org.apache.bcel.internal.generic.NOP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSeminarCommand extends AbstractCommand {
    private static AddSeminarCommand instance;

    private AddSeminarCommand() {}

    public static synchronized AddSeminarCommand getInstance() {
        if (instance == null) {
            instance = new AddSeminarCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        SeminarService service = SeminarService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (UserService.getInstance().isAdminUser(request, login)) {
            SeminarsEntity regData = (SeminarsEntity) session.getAttribute("regData");
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_ADD_SEMINAR);
            if (regData == null) {
                regData = new SeminarsEntity();
                session.setAttribute("regData", regData);
            } else {
                String subject = request.getParameter("newSubject");
                String dateParam = request.getParameter("newDate");
                Date date = null;
                if (!"".equals(dateParam)) {
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateParam);
                    } catch (ParseException e) {}
                }
                Date today1 = new Date();
                Date today = new Date(today1.getYear(), today1.getMonth(), today1.getDate());
                if (!"".equals(subject) && date != null && (today.before(date) || today.equals(date))) {
                    regData.setSubject(subject);
                    regData.setSeminarDate(date);
                    service.add(request, regData);
//                    if (regData.getSeminarId() != 0) {
                    request.setAttribute("info", "Семинар добавлен в базу");
//                    }
                    session.removeAttribute("regData");
                } else {
                    setErrorMessage(request, "Поля данных семинара не должны быть пустыми и дата должна быть >= сегодня");
                }
            }

            setForwardPage(request, page);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
