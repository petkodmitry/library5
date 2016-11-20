package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.BooksEntity;
import com.petko.entitiesOLD.BookEntityOLD;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDao extends BaseDao<BooksEntity> {
    private static Logger log = Logger.getLogger(BookDao.class);

    private static BookDao instance;

    private BookDao() {
    }

    public static synchronized BookDao getInstance() {
        if (instance == null) {
            instance = new BookDao();
        }
        return instance;
    }

    public List<BooksEntity> getAllByCoupleIds(Set<Integer> ids) throws DaoException {
        List<BooksEntity> result;
        try {
            session = util.getSession();
            String hql = "SELECT B FROM BooksEntity B WHERE B.bookId IN :idsParam";
            Query query = session.createQuery(hql);
            query.setParameterList("idsParam", ids);
            result = query.list();

            log.info("getAllByCoupleIds in BookDao");
        } catch (HibernateException e) {
            String message = "Error getAllByCoupleIds in BookDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public List<BooksEntity> getBooksByTitleOrAuthorAndStatus(String searchTextInBook, Boolean status) throws DaoException {
        searchTextInBook = "%" + searchTextInBook + "%";
        List<BooksEntity> result;
        try {
            session = util.getSession();

            Query query;
            if (status != null) {
                String hql = "SELECT B FROM BooksEntity B WHERE (B.title LIKE :searchParam OR B.author LIKE :searchParam) AND B.isBusy = :statusParam";
                query = session.createQuery(hql);
                query.setParameter("searchParam", searchTextInBook);
                query.setParameter("statusParam", status);
            } else {
                String hql = "SELECT B FROM BooksEntity B WHERE (B.title LIKE :searchParam OR B.author LIKE :searchParam)";
                query = session.createQuery(hql);
                query.setParameter("searchParam", searchTextInBook);
            }

            result = query.list();

            log.info("getBooksByTitleOrAuthorAndStatus in BookDao");
        } catch (HibernateException e) {
            String message = "Error getBooksByTitleOrAuthorAndStatus in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public BooksEntity createNewEntity(String title, String author, boolean isBusy) {
        BooksEntity result = new BooksEntity();
        result.setTitle(title);
        result.setAuthor(author);
        result.setIsBusy(isBusy);
        return result;
    }
}
