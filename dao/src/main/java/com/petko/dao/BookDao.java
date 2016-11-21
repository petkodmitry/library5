package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.BooksEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

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

    /**
     *
     * @param ids - Set of IDs for searching
     * @return List of BooksEntity considering given options
     * @throws DaoException
     */
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

    /**
     *
     * @param searchTextInBook - what to search in Book
     * @param status - desired status
     * @return List of BooksEntity considering given options
     * @throws DaoException
     */
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

    /**
     *
     * @param title - title of the Book
     * @param author - author of the Book
     * @param isBusy - status
     * @return a new BooksEntity
     */
    public BooksEntity createNewEntity(String title, String author, boolean isBusy) {
        BooksEntity result = new BooksEntity();
        result.setTitle(title);
        result.setAuthor(author);
        result.setIsBusy(isBusy);
        return result;
    }
}
