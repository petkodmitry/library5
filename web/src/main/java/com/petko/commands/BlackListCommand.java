package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.UsersEntity;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class BlackListCommand extends AbstractCommand{
    private static BlackListCommand instance;

    private BlackListCommand() {}

    public static synchronized BlackListCommand getInstance() {
        if (instance == null) {
            instance = new BlackListCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserService service = UserService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (service.isAdminUser(request, login)) {
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_BLACK_LIST);
            List<UsersEntity> blackList = service.getUsersByBlock(request, true);
            session.setAttribute("blackList", blackList);
            setForwardPage(request, page);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
