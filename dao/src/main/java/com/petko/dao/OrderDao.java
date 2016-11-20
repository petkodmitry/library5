package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.OrderStatus;
import com.petko.entities.OrdersEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

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
        } catch (HibernateException e) {
            String message = "Error getOrdersByLoginAndStatus in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public List<OrdersEntity> getOrdersByLoginBookIdStatuses(String login, int bookId, String[] orderStatuses) throws DaoException {
        List<OrdersEntity> result;
        try {
            session = util.getSession();

            String hql = "SELECT O FROM OrdersEntity O WHERE O.login=:loginParam AND O.bookId=:bookIdParam AND O.status IN :statusParam";
            Query query = session.createQuery(hql);
            query.setParameter("loginParam", login);
            query.setParameter("bookIdParam", bookId);
            query.setParameterList("statusParam", orderStatuses);
            result = query.list();

            log.info("getOrdersByLoginBookIdStatuses in OrderDao");
        } catch (HibernateException e) {
            String message = "Error getOrdersByLoginBookIdStatuses in OrderDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }
}
