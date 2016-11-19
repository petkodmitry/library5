package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.Entity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseDao<T extends Entity> implements Dao<T> {
    private static Logger log = Logger.getLogger(BaseDao.class);
    protected static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();
    protected static Session session;

    @Override
    public void saveOrUpdate(T entity) throws DaoException {
        try {
            log.info("saveOrUpdate(): " + entity);
            session = util.getSession();
            session.saveOrUpdate(entity);
        } catch (HibernateException e) {
            String message = "Error save or update ENTITY in Dao.";
            log.error(message + e);
            throw new DaoException(message);
        }
    }

    @Override
    public List<T> getAll(int first, int max) throws DaoException {
        return null;
    }

    @Override
    public T getById(int id) throws DaoException {
        log.info("Get ENTITY by id: " + id);
        T entity;
        try {
            session = util.getSession();
            entity = (T) session.get(getPersistentClass(), id);
            log.info("get() clazz: " + entity);
        } catch (HibernateException e) {
            String message = "Error get() " + getPersistentClass() + " in BaseDao.";
            log.error(message + e);
            throw new DaoException(message);
        }
        return entity;
    }

    @Override
    public void delete(T entity) throws DaoException {
        try {
            log.info("Delete ENTITY: " + entity);
            session = util.getSession();
            session.delete(entity);
        } catch (IllegalArgumentException | HibernateException e) {
            String message = "Error deleting " + getPersistentClass() + " in BaseDao.";
            log.error(message + e);
            throw new DaoException(message);
        }
    }

    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
