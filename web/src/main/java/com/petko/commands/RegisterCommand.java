package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.UsersEntity;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterCommand extends AbstractCommand {
    private static RegisterCommand instance;

    private RegisterCommand() {
    }

    public static synchronized RegisterCommand getInstance() {
        if (instance == null) {
            instance = new RegisterCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserService service = UserService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (login == null || service.isAdminUser(request, login)) {

            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_REGISTRATION);
            UsersEntity regData;
            /**
             * creating attribute of the session: UsersEntity regData
             */
            if (session.getAttribute("regData") == null) {
                regData = new UsersEntity();
                session.setAttribute("regData", regData);
            }
            /**
             * reading data from session attribute regData
             */
            else {
                regData = (UsersEntity) session.getAttribute("regData");
                regData = service.setAllDataOfEntity(regData, request.getParameter("newName"), request.getParameter("newLastName"),
                        request.getParameter("newLogin"), request.getParameter("newPassword"), false, false);
                String repeatPassword = request.getParameter("repeatPassword");
                /**
                 * if 'login' is entered
                 */
                if (regData.getLogin() != null && !"".equals(regData.getLogin())) {
                    /**
                     * check if asked login exists in database
                     */
                    if (service.isLoginExists(request, regData.getLogin())) {
                        request.setAttribute("unavailableMessage", "логин НЕдоступен!");
                    } else {
                        request.setAttribute("unavailableMessage", "логин доступен");
                        /**
                         * if all data is entered
                         */
                        if (service.isAllRegisterDataEntered(regData, repeatPassword)) {
                            if (service.isAllPasswordRulesFollowed(regData.getPassword(), repeatPassword)) {
                                service.add(request, regData);
                                session.removeAttribute("regData");
                                page = ResourceManager.getInstance().getProperty(Constants.PAGE_REGISTRATION_OK);
                            } else {
                                setErrorMessage(request, "Пароль должен содержать 8 символов");
                            }
                        }
                    }
                }
            }
            setForwardPage(request, page);
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
