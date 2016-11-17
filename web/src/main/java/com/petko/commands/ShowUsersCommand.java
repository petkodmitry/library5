package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities2.UsersEntity;
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
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        String webPage = request.getParameter("page");
        /*int first;
        if (firstParameter == null) first = 0;
        else first = Integer.parseInt(firstParameter);*/
//        int max = 5;
        // если админ, то выполняем команду
        if (UserService.getInstance().isAdminUser(request, login)) {
//            Set<UserEntityOLD> userSet = UserService.getInstance().getAllOLD(request);
            List<UsersEntity> userSet = UserService.getInstance().getAll(request, webPage/*, max*/);
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
