package com.petko;

import com.petko.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class ExceptionsHandler {
    private ExceptionsHandler() {}

    public static void processException(HttpServletRequest request, Throwable exception) {
        String eMessage = exception.getMessage();
        if (exception instanceof DaoException) {
            if (eMessage == null) eMessage = "Неизвестная ошибка работы с БД";
        } else if (exception instanceof SQLException) {
            if (eMessage == null) eMessage = "Ошибка отправки запроса";
        } else if (exception instanceof ClassNotFoundException) {
            if (eMessage == null) eMessage = "Ошибка загрузки неизвестного класса";
        }
        request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, eMessage);
    }
}
