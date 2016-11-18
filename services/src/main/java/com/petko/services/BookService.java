package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.entities2.BooksEntity;
import com.petko.managers.PoolManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BookService implements Service<BooksEntity>{
    private static BookService instance;

    private BookService() {}

    public static synchronized BookService getInstance() {
        if(instance == null){
            instance = new BookService();
        }
        return instance;
    }

    public Set<BooksEntity> searchBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
        Set<BooksEntity> result = new HashSet<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            result.addAll(BookDaoOLD.getInstance().getFreeBooksByTitleOrAuthor(connection, searchTextInBook));
//            result.addAll(BookDaoOLD.getInstance().getBusyBooksByTitleOrAuthor(connection, searchTextInBook));
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptySet();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }

    public List<BooksEntity> getAllBooksByTitleOrAuthor(HttpServletRequest request, String searchTextInBook) {
        List<BooksEntity> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            result = BookDaoOLD.getInstance().getBooksByTitleOrAuthor(connection, searchTextInBook);
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
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
            BooksEntity entity = null;
//            entity = BookDaoOLD.getInstance().getById(connection, bookId);
            entity.setIsBusy(isBusy);
//            BookDaoOLD.getInstance().update(connection, entity);
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public boolean isBusy(HttpServletRequest request, Integer bookId) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            BooksEntity entity = null;
//            entity = BookDaoOLD.getInstance().getById(connection, bookId);
            return entity.getIsBusy();
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
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
//            BookDaoOLD.getInstance().delete(connection, bookId);
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public void add(HttpServletRequest request, BooksEntity bookEntity) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            BookDaoOLD.getInstance().add(connection, bookEntity);
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
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
