package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.services.SeminarService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteSeminarCommand extends AbstractCommand {
    private static DeleteSeminarCommand instance;

    private DeleteSeminarCommand() {}

    public static synchronized DeleteSeminarCommand getInstance() {
        if (instance == null) {
            instance = new DeleteSeminarCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        SeminarService service = SeminarService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (UserService.getInstance().isAdminUser(request, login)) {
            Integer seminarId = Integer.parseInt(request.getParameter("seminarId"));
            service.delete(request, seminarId);

            request.setAttribute("info", "Семинар с ID " + seminarId + " успешно удалён");

            AdminSeminarsCommand.getInstance().execute(request, response);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
