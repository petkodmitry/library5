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
    private BookDao() {}

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

            log.info("getOrdersByLoginAndStatus in OrderDao");
        } catch (HibernateException e) {
            String message = "Error getOrdersByLoginAndStatus in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }
}
