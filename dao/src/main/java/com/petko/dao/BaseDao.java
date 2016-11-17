package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities2.Entity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class BaseDao<T extends Entity> implements Dao<T> {
    private static Logger log = Logger.getLogger(BaseDao.class);
    protected HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();
//    protected SessionFactory sessionFactory = HibernateUtilLibrary.getHibernateUtil().sessionFactory;
    protected Session session;
//    private Transaction transaction = null;

    @Override
    public void saveOrUpdate(T entity) throws DaoException {
        try {
            session = util.getSession();
//            session = sessionFactory.getCurrentSession();
//            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            log.info("saveOrUpdate(entity): " + entity);
//            transaction.commit();
            log.info("Save or update (commit): " + entity);
        } catch (HibernateException e) {
            String message = "Error save or update ENTITY in Dao";
            log.error(message + e);
//            transaction.rollback();
            throw new DaoException(message);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> getAll(int first, int max) throws DaoException {
        return null;
    }

    @Override
    public T getById(int id) throws DaoException {
        return null;
    }

    @Override
    public void delete(int id) throws DaoException {
    }
}
