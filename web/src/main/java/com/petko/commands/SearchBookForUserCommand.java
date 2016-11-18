package com.petko.commands;

import com.petko.ResourceManager;
import com.petko.constants.Constants;
import com.petko.entities.BookEntityOLD;
import com.petko.services.BookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

public class SearchBookForUserCommand extends AbstractCommand{
    private static SearchBookForUserCommand instance;

    private SearchBookForUserCommand() {
    }

    public static synchronized SearchBookForUserCommand getInstance() {
        if (instance == null) {
            instance = new SearchBookForUserCommand();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        BookService service = BookService.getInstance();
        HttpSession session = request.getSession();
        String page = ResourceManager.getInstance().getProperty(Constants.PAGE_SEARCH_BOOK_FOR_USER);
        Set<BookEntityOLD> searchBookForUser = null;
        /**
         * if there is searchTextInBook parameter in request
         */
        String searchTextInBook;
        if ((searchTextInBook = request.getParameter("searchTextInBook")) != null && !"".equals(searchTextInBook)) {
//            searchBookForUser = service.searchBooksByTitleOrAuthor(request, searchTextInBook);
            session.setAttribute("searchBookForUser", searchBookForUser);
        }
        setForwardPage(request, page);
    }
}
