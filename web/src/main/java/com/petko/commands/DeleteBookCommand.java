package com.petko.commands;

import com.petko.constants.Constants;
import com.petko.entities.BooksEntity;
import com.petko.services.BookService;
import com.petko.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class DeleteBookCommand extends AbstractCommand {
    private static DeleteBookCommand instance;

    private DeleteBookCommand() {}

    public static synchronized DeleteBookCommand getInstance() {
        if (instance == null) {
            instance = new DeleteBookCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        BookService service = BookService.getInstance();
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("user");
        if (UserService.getInstance().isAdminUser(request, login)) {
            Integer bookId = Integer.parseInt(request.getParameter("bookId"));
            BooksEntity book = service.deleteBook(request, bookId);

            if (book != null) request.setAttribute("info", "Книга с ID " + bookId + " успешно удалена");
            List<BooksEntity> searchBookAdmin = (ArrayList<BooksEntity>) session.getAttribute("searchBookAdmin");
            if (searchBookAdmin != null) searchBookAdmin.remove(book);

            SearchBookAdminCommand.getInstance().execute(request, response);
        // если не админ, сообщаем о невозможности выполнения команды
        } else if ((request.getAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE)) == null) {
            setErrorMessage(request, "У Вас нет прав для выполнения данной команды");
            redirectToMainPage(request, login);
        }
    }
}
