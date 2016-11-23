package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.BooksEntity;
import com.petko.entities.OrderStatus;
import com.petko.entities.OrdersEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.Date;
import java.util.List;


public class OrderDao extends BaseDao<OrdersEntity> {
    private static Logger log = Logger.getLogger(OrderDao.class);

    private static OrderDao instance;

    private OrderDao() {
    }

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDao();
        }
        return instance;
    }

    /**
     * all Orders of a User by specific Status
     * @param login - desired login
     * @param orderStatus - desired status of order
     * @return List of OrdersEntity considering given options
     * @throws DaoException
     */
    public List<OrdersEntity> getOrdersByLoginAndStatus(String login, OrderStatus orderStatus) throws DaoException {
        List<OrdersEntity> result;
        try {
            session = util.getSession();

            Query query = null;
            if (login != null && orderStatus != null) {
                String hql = "SELECT O FROM OrdersEntity O WHERE O.login=:loginParam AND O.status=:statusParam";
                query = session.createQuery(hql);
                query.setParameter("loginParam", login);
                query.setParameter("statusParam", orderStatus.toString());
            } else if (orderStatus != null) {
                String hql = "SELECT O FROM OrdersEntity O WHERE O.status=:statusParam";
                query = session.createQuery(hql);
                query.setParameter("statusParam", orderStatus.toString());
            } else if (login != null) {
                String hql = "SELECT O FROM OrdersEntity O WHERE O.login=:loginParam";
                query = session.createQuery(hql);
                query.setParameter("loginParam", login);
            }

            result = query.list();

            log.info("getOrdersByLoginAndStatus in OrderDao");
        } catch (HibernateException | NullPointerException e) {
            String message = "Error getOrdersByLoginAndStatus in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /**
     * all Orders by Status less then specific Date
     * @param orderStatus - desired status of order
     * @param endDate - result will include entities with less then this date
     * @return List of OrdersEntity considering given options
     * @throws DaoException
     */
    public List<OrdersEntity> getOrdersByStatusAndEndDate(OrderStatus orderStatus, Date endDate) throws DaoException {
        List<OrdersEntity> result;
        try {
            session = util.getSession();

            String hql = "SELECT O FROM OrdersEntity O WHERE O.status=:statusParam AND O.endDate<:endDateParam";
            Query query = session.createQuery(hql);
            query.setParameter("statusParam", orderStatus.toString());
            query.setParameter("endDateParam", endDate);

            result = query.list();

            log.info("getOrdersByStatusAndEndDate in OrderDao");
        } catch (HibernateException | NullPointerException e) {
            String message = "Error getOrdersByStatusAndEndDate in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /**
     * all Orders of specific Book by User and specific Statuses
     * @param login - desired user
     * @param bookEntity - we need to search all the exemplars of this bookEntity (by title and author)
     * @param orderStatuses - list of desired statuses
     * @return List of OrdersEntity considering given options
     * @throws DaoException
     */
    public List<OrdersEntity> getOrdersByLoginBookIdStatuses(String login, BooksEntity bookEntity, String[] orderStatuses) throws DaoException {
        List<OrdersEntity> result;
        try {
            session = util.getSession();

            String title = bookEntity.getTitle();
            String author = bookEntity.getAuthor();
            String hql = "SELECT O FROM OrdersEntity O WHERE O.login=:loginParam AND O.status IN :statusParam" +
                    " AND O.bookId IN (SELECT B.bookId FROM BooksEntity B WHERE B.title=:titleParam AND B.author=:authorParam)";
            Query query = session.createQuery(hql);
            query.setParameter("loginParam", login);
            query.setParameterList("statusParam", orderStatuses);
            query.setParameter("titleParam", title);
            query.setParameter("authorParam", author);
            result = query.list();

            log.info("getOrdersByLoginBookIdStatuses in OrderDao");
        } catch (HibernateException | NullPointerException e) {
            String message = "Error getOrdersByLoginBookIdStatuses in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }
}
