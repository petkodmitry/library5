package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.dao.BookDao;
import com.petko.entities.BooksEntity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BookService {
    private static BookService instance;
    private static UserService userService = UserService.getInstance();
    private static Logger log = Logger.getLogger(BookService.class);
    private static BookDao bookDao = BookDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private BookService() {}

    public static synchronized BookService getInstance() {
        if(instance == null){
            instance = new BookService();
        }
        return instance;
    }

    /**
     * searches Books by Title or Author
     * @param request - current request
     * @param searchTextInBook - text to be searched in Book
     * @return List of Books according conditions
     */
    public List<BooksEntity> searchBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook, String login) {
        List<BooksEntity> result = new ArrayList<>();
        boolean isUserAdmin = userService.isAdminUser(request, login);
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = bookDao.getBooksByTitleOrAuthorAndStatus(searchTextInBook, null);
            // if User is not Admin, show him only one exemplar
            if (!isUserAdmin) result = showBooksByOneExemplar(result);

            transaction.commit();
            log.info("Search books by (login or title) and status (commit)");
        } catch (DaoException | NullPointerException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    private List<BooksEntity> showBooksByOneExemplar(List<BooksEntity> booksEntityList) {
        List<BooksEntity> result = new ArrayList<>();
        Map<Map<String, String>, BooksEntity> tempResult = new HashMap<>();

        for (BooksEntity book : booksEntityList) {
            Map<String, String> conditions = new HashMap<>();
            conditions.put(book.getTitle(), book.getAuthor());
            if (!tempResult.containsKey(conditions)) {
                tempResult.put(conditions, book);
            } else {
                BooksEntity existedBook = tempResult.get(conditions);
                // if exemplar in temp Map is busy and this exemplar is NOT busy - replace
                if (existedBook.getIsBusy() && !book.getIsBusy()) {
                    tempResult.put(conditions, book);
                }
            }
        }
        result.addAll(tempResult.values());

        return result;
    }

    /**
     * removes Book by book Id
     * @param request - current request
     * @param bookId - id of the Book to be deleted
     */
    public BooksEntity deleteBook(HttpServletRequest request, Integer bookId) {
        BooksEntity book;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            book = bookDao.getById(bookId);
            if (book != null) {
                bookDao.delete(book);
                transaction.commit();
                log.info("Delete book (commit)");
            }
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return null;
        } finally {
            util.releaseSession(currentSession);
        }
        return book;
    }

    /**
     * adds Book to DataBase
     * @param request - current request
     * @param bookEntity to be added to DB
     */
    public void add(HttpServletRequest request, BooksEntity bookEntity) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            bookDao.save(bookEntity);

            transaction.commit();
            log.info("Save book (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }
}
