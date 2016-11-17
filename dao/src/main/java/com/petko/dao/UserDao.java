package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities2.UsersEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

public class UserDao extends BaseDao<UsersEntity> {
    private static Logger log = Logger.getLogger(UserDao.class);
//    private Session session;

    private static UserDao instance;
    private UserDao() {}
    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public UsersEntity getByLogin(String login) throws DaoException {
        UsersEntity result;
        try {
            session = util.getSession();
//            session = sessionFactory.getCurrentSession();
            String hql = "select U from UsersEntity U where U.login=:param";
            Query query = session.createQuery(hql);
            query.setCacheable(true);
            query.setParameter("param", login);
            result = (UsersEntity) query.uniqueResult();
            log.info("get user by login");
//            log.info("get user by login (commit)");
        } catch (HibernateException e) {
            String message = "Error get user by login in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    @Override
    public List<UsersEntity> getAll(int first, int max) throws DaoException{
        List<UsersEntity> result;
        try {
            session = util.getSession();
//            session = sessionFactory.getCurrentSession();
//            String hql = "FROM UsersEntity";
//            Query query = session.createQuery(hql);
            Criteria criteria = session.createCriteria(UsersEntity.class);
            criteria.setCacheable(true);
            criteria.setFirstResult(first);
            criteria.setMaxResults(max);
            result = criteria.list();
            log.info("getAll users. CountOfUsers=" + result.size());
//            log.info("getTotal users (commit)");
        } catch (HibernateException e) {
            String message = "Error getAll users in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public Long getTotal() throws DaoException{
        Long result;
        try {
            session = util.getSession();
//            session = sessionFactory.getCurrentSession();
            String hql = "SELECT count(id) FROM UsersEntity U";
            Query query = session.createQuery(hql);
//            Criteria criteria = session.createCriteria(UsersEntity.class);
            query.setCacheable(true);
            result = (Long) query.uniqueResult();
            log.info("getTotal users. CountOfUsers=" + result);
        } catch (HibernateException e) {
            String message = "Error getTotal users in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }
}
