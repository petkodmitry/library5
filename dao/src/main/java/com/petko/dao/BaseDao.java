package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.Entity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends Entity> implements Dao<T> {
    private static Logger log = Logger.getLogger(BaseDao.class);
    protected static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();
    protected static Session session;

    /**
     * adds entity in database
     * @param entity - new entity
     * @throws DaoException
     */
    @Override
    public void save(T entity) throws DaoException {
        try {
            log.info("save(): " + entity);
            session = util.getSession();
            session.save(entity);
        } catch (HibernateException | IllegalArgumentException e) {
            String message = "Error save " + entity + " in Dao.";
            log.error(message + e);
            throw new DaoException(message);
        }
    }

    /**
     * updates entity in database
     * @param entity - entity
     * @throws DaoException
     */
    @Override
    public void update(T entity) throws DaoException {
        try {
            log.info("update(): " + entity);
            session = util.getSession();
            session.update(entity);
        } catch (HibernateException | IllegalArgumentException e) {
            String message = "Error update " + entity + " in Dao.";
            log.error(message + e);
            throw new DaoException(message);
        }
    }

    /**
     *
     * @param first - the first record for a page
     * @param max - max count of elements for pagination
     * @return  List of Entities considering given options
     * @throws DaoException
     */
    @Override
    public List<T> getAll(int first, int max, String sortBy, String orderType, Map<String, String> filters) throws DaoException {
        List<T> result;
        try {
            session = util.getSession();
            Criteria criteria = session.createCriteria(getPersistentClass());
            criteria.setCacheable(true);

            if (sortBy != null && orderType != null) {
                criteria = orderType.equals("asc") ? criteria.addOrder(Order.asc(sortBy).ignoreCase())
                        : criteria.addOrder(Order.desc(sortBy).ignoreCase());
            }
            for (String filter : filters.keySet()) {
                switch (filter) {
                    case "userId":
                        criteria.add(Restrictions.sqlRestriction(" uid LIKE '%" + filters.get(filter) + "%' "));
                        break;
                    case "isAdmin":
                    case "isBlocked":
                        String filterValue = filters.get(filter);
                        if ("true".contains(filterValue.toLowerCase())) criteria.add(Restrictions.eq(filter, true));
                        else if ("false".contains(filterValue.toLowerCase())) criteria.add(Restrictions.eq(filter, false));
                        else {
                            criteria.add(Restrictions.ne(filter, true));
                            criteria.add(Restrictions.ne(filter, false));
                        }
                        break;
                    default:
                        criteria.add(Restrictions.ilike(filter, "%" + filters.get(filter) + "%"));
                        break;
                }
            }

            criteria.setFirstResult(first);
            criteria.setMaxResults(max);
            result = criteria.list();
            log.info("getAll " + getPersistentClass().getName() + ". Count=" + result.size());
        } catch (HibernateException e) {
            String message = "Error getAll " + getPersistentClass().getName() + " in BaseDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }


    public List<T> getAbsolutelyAll() throws DaoException {
        List<T> result;
        try {
            session = util.getSession();
            Query query = session.createQuery("FROM " + getPersistentClass().getSimpleName());
            result = query.list();
            log.info("getAll " + getPersistentClass().getName());
        } catch (HibernateException e) {
            String message = "Error getAll " + getPersistentClass().getName();
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /**
     * gives a list of all elements in the DB
     * @return List of all elements
     * @throws DaoException
     */
    public Long getTotal() throws DaoException{
        Long result;
        try {
            session = util.getSession();
            String hql = "SELECT count(id) FROM " + getPersistentClass().getSimpleName();
            Query query = session.createQuery(hql);
            query.setCacheable(true);
            result = (Long) query.uniqueResult();
            log.info("getTotal " + getPersistentClass().getSimpleName() + ". Count=" + result);
        } catch (HibernateException e) {
            String message = "Error getTotal " + getPersistentClass().getSimpleName() + " in BaseDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /**
     * gives Entity by id
     * @param id - id of looking Entity
     * @return Entity by id
     * @throws DaoException
     */
    @Override
    public T getById(int id) throws DaoException {
        log.info("Get ENTITY by id: " + id);
        T entity;
        try {
            session = util.getSession();
            entity = (T) session.get(getPersistentClass(), id);
            log.info("get() clazz: " + entity);
        } catch (HibernateException | ClassCastException e) {
            String message = "Error get() " + getPersistentClass() + " in BaseDao.";
            log.error(message + e);
            throw new DaoException(message);
        }
        return entity;
    }

    /**
     * deletes Entity
     * @param entity - Entity to be deleted
     * @throws DaoException
     */
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

    /**
     * defines correct class which extends Entity class
     * @return exactly used persistant class
     */
    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
