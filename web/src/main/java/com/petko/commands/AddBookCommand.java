package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.BooksEntity;
import com.petko.services.BookService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddBookCommand extends AbstractCommand {
    private static AddBookCommand instance;

    private AddBookCommand() {}

    public static synchronized AddBookCommand getInstance() {
        if (instance == null) {
            instance = new AddBookCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        BookService service = BookService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (UserService.getInstance().isAdminUser(request, login)) {
            BooksEntity regData = (BooksEntity) session.getAttribute("regData");
            String page = ResourceManager.getInstance().getProperty(Constants.PAGE_ADD_BOOK);
            if (regData == null) {
                regData = new BooksEntity();
                session.setAttribute("regData", regData);
            } else {
                String title = request.getParameter("newTitle");
                String author = request.getParameter("newAuthor");
                if (!"".equals(title) && !"".equals(author)) {
                    regData.setTitle(title);
                    regData.setAuthor(author);
                    regData.setIsBusy(false);
                    service.add(request, regData);
                    request.setAttribute("info", "Книга добавлена в базу библиотеки");
                    session.removeAttribute("regData");
                    page = ResourceManager.getInstance().getProperty(Constants.PAGE_SEARCH_BOOK_ADMIN);
                } else {
                    setErrorMessage(request, "Поля данных книги не должны быть пустыми");
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
