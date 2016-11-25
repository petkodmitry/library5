package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.UsersEntity;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowUsersCommand extends AbstractCommand {
    private static ShowUsersCommand instance;

    private ShowUsersCommand() {}

    public static synchronized ShowUsersCommand getInstance() {
        if(instance == null){
            instance = new ShowUsersCommand();
        }
        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserService service = UserService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        String webPage = request.getParameter("page");
        String perPage = request.getParameter("perPage");
        Integer perPageSession = (Integer) session.getAttribute("perPage");
        Integer oldPerPageSession = (Integer) session.getAttribute("oldPerPage");
        // если админ, то выполняем команду
        if (service.isAdminUser(request, login)) {

            int max;
            if (perPage != null) max = Integer.parseInt(perPage);
            else if (perPageSession != null) max = perPageSession;
            else max = 5;

            session.setAttribute("perPage", max);

            /*if (oldPerPageSession != null && !oldPerPageSession.equals(max)) {
                int tempPage = Integer.parseInt(webPage);
                tempPage = tempPage * oldPerPageSession / max;
                webPage = Integer.toString(tempPage);
                session.setAttribute("oldPerPage", max);
                session.setAttribute("page", webPage);
            }*/

            List<UsersEntity> userSet = service.getAll(request, webPage, max);
            if (userSet.isEmpty()) setErrorMessage(request, "Не удалось получить список пользователей");
            request.setAttribute(Constants.USER_SET, userSet);
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_SHOW_USERS);
            setForwardPage(request, page);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
