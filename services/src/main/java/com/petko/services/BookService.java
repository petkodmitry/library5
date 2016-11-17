package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.dao.BookDaoOLD;
import com.petko.entities.BookEntityOLD;
import com.petko.managers.PoolManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BookService implements Service<BookEntityOLD>{
    private static BookService instance;

    private BookService() {}

    public static synchronized BookService getInstance() {
        if(instance == null){
            instance = new BookService();
        }
        return instance;
    }

    public Set<BookEntityOLD> searchBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
        Set<BookEntityOLD> result = new HashSet<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            result.addAll(BookDaoOLD.getInstance().getFreeBooksByTitleOrAuthor(connection, searchTextInBook));
            result.addAll(BookDaoOLD.getInstance().getBusyBooksByTitleOrAuthor(connection, searchTextInBook));
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptySet();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }

    public List<BookEntityOLD> getAllBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
        List<BookEntityOLD> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            result = BookDaoOLD.getInstance().getBooksByTitleOrAuthor(connection, searchTextInBook);
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }

    public void setBookBusy(HttpServletRequest request, Integer bookId, Boolean isBusy) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            BookEntityOLD entity = BookDaoOLD.getInstance().getById(connection, bookId);
            entity.setBusy(isBusy);
            BookDaoOLD.getInstance().update(connection, entity);
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public boolean isBusy(HttpServletRequest request, Integer bookId) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            BookEntityOLD entity = BookDaoOLD.getInstance().getById(connection, bookId);
            return entity.isBusy();
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return true;
    }

    public void deleteBook(HttpServletRequest request, Integer bookId) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            BookDaoOLD.getInstance().delete(connection, bookId);
//            entity.setBusy(isBusy);
//            BookDaoOLD.getInstance().update(connection, entity);
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public void add(HttpServletRequest request, BookEntityOLD bookEntity) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
            BookDaoOLD.getInstance().add(connection, bookEntity);
        } catch (DaoException | SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public void add(BookEntityOLD entity) {
    }

    public List<BookEntityOLD> getAll() {
        return null;
    }

    public BookEntityOLD getByLogin(String login) {
        return null;
    }

    public void update(BookEntityOLD entity) {
    }

    public void delete(int id) {

    }
}
