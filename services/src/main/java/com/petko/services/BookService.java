package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.dao.BookDao;
import com.petko.entities.BooksEntity;
import com.petko.managers.PoolManager;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BookService implements Service<BooksEntity>{
    private static BookService instance;
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

//    public Set<BooksEntity> searchBooksByTitleOrAuthorOLD(HttpServletRequest request, String searchTextInBook) {
//        Set<BooksEntity> result = new HashSet<>();
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
////            result.addAll(BookDaoOLD.getInstance().getFreeBooksByTitleOrAuthor(connection, searchTextInBook));
////            result.addAll(BookDaoOLD.getInstance().getBusyBooksByTitleOrAuthor(connection, searchTextInBook));
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptySet();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

    public List<BooksEntity> searchBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
        List<BooksEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = bookDao.getBooksByTitleOrAuthorAndStatus(searchTextInBook, null);
            transaction.commit();
            log.info("Search books by (login or title) and status (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /**/
//    public List<BooksEntity> getAllBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
//        List<BooksEntity> result = new ArrayList<>();
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
////            result = BookDaoOLD.getInstance().getBooksByTitleOrAuthor(connection, searchTextInBook);
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptyList();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

    /**/
//    public void setBookBusyOLD(HttpServletRequest request, Integer bookId, Boolean isBusy) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            BooksEntity entity = null;
////            entity = BookDaoOLD.getInstance().getById(connection, bookId);
//            entity.setIsBusy(isBusy);
////            BookDaoOLD.getInstance().update(connection, entity);
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    /**/
//    public void setBookBusy(HttpServletRequest request, /*Integer bookId*/ BooksEntity entity, Boolean isBusy) {
////        Session currentSession = null;
////        Transaction transaction = null;
//        try {
////            currentSession = util.getSession();
////            transaction = currentSession.beginTransaction();
//
////            BooksEntity entity = bookDao.getById(bookId);
//            entity.setIsBusy(isBusy);
//            bookDao.update(entity);
//
////            transaction.commit();
////            log.info("Update book (commit)");
//        } catch (DaoException e) {
////            transaction.rollback();
//            ExceptionsHandler.processException(request, e);
//        } /*finally {
//            util.releaseSession(currentSession);
//        }*/
//    }

    /**/
//    public boolean isBusy(HttpServletRequest request, Integer bookId) {
//        boolean result = true;
//        Session currentSession = null;
//        Transaction transaction = null;
//        try {
//            currentSession = util.getSession();
//            transaction = currentSession.beginTransaction();
//
//            BooksEntity entity = bookDao.getById(bookId);
////            entity = BookDaoOLD.getInstance().getById(connection, bookId);
//            result = entity.getIsBusy();
//
//            transaction.commit();
//            log.info("Get book by ID (commit)");
//        } catch (DaoException e) {
//            transaction.rollback();
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            util.releaseSession(currentSession);
//        }
//        return result;
//    }

    /**/
//    public void deleteBookOLD(HttpServletRequest request, Integer bookId) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//    //            BookDaoOLD.getInstance().delete(connection, bookId);
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    public void deleteBook(HttpServletRequest request, Integer bookId) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            BooksEntity book = bookDao.getById(bookId);
            if (book != null) {
                bookDao.delete(book);
                transaction.commit();
                log.info("Delete book (commit)");
            }
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**/
//    public void addOLD(HttpServletRequest request, BooksEntity bookEntity) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
////            BookDaoOLD.getInstance().add(connection, bookEntity);
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

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

    public void add(BooksEntity entity) {
    }

    public List<BooksEntity> getAll() {
        return null;
    }

    public BooksEntity getByLogin(String login) {
        return null;
    }

    public void update(BooksEntity entity) {
    }

    public void delete(int id) {

    }
}
