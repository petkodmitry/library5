package com.petko.commands;

import com.petko.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    String errorMessageAttribute = Constants.ERROR_MESSAGE_ATTRIBUTE;
    String forwardPageAttribute = Constants.FORWARD_PAGE_ATTRIBUTE;

    // основной метод для логики команды
    void execute(HttpServletRequest request, HttpServletResponse response);
    // добавить в параметры запроса страницу для перехода после успешного выполнения команды
    void setForwardPage(HttpServletRequest request, String page);
    // добавить в параметры запроса сообщение об ошибке, при неуспешном выполнении команды
    void setErrorMessage(HttpServletRequest request, String message);
}
