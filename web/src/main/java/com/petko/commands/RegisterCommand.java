package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.UserEntityOLD;
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
        if (!service.isAdminUser(request, login)) return;

        // TODO перенести в методы service
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_REGISTRATION);
        UserEntityOLD regData;
        /**
         * creating attribute of the session: UserEntityOLD regData
         */
        if (session.getAttribute("regData") == null) {
            regData = new UserEntityOLD();
            session.setAttribute("regData", regData);
        }
        /**
         * reading data from session attribute regData
         */
        else {
            regData = (UserEntityOLD) session.getAttribute("regData");
            regData.setFirstName(request.getParameter("newName"));
            regData.setLastName(request.getParameter("newLastName"));
            regData.setLogin(request.getParameter("newLogin"));
            regData.setPassword(request.getParameter("newPassword"));
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
                    if (!"".equals(regData.getFirstName()) &&
                            !"".equals(regData.getLastName()) &&
                            !"".equals(regData.getLogin()) &&
                            !"".equals(regData.getPassword()) &&
                            !"".equals(repeatPassword)) {
                        if (service.isAllPasswordRulesFollowed(regData.getPassword(), repeatPassword)) {
                            service.addNewEntityToDataBase(request, regData.getFirstName(), regData.getLastName(),
                                    regData.getLogin(), regData.getPassword(), regData.isAdmin(), regData.isBlocked());
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
    }
}
