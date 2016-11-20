package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.BooksEntity;
import com.petko.entities.UsersEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;
import java.util.Set;

public class UserDao extends BaseDao<UsersEntity> {
    private static Logger log = Logger.getLogger(UserDao.class);

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
            String hql = "select U from UsersEntity U where U.login=:param";
            Query query = session.createQuery(hql);
            query.setCacheable(true);
            query.setParameter("param", login);
            result = (UsersEntity) query.uniqueResult();
            log.info("get user by login");
        } catch (HibernateException e) {
            String message = "Error getting user by login in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public List<UsersEntity> getAllByCoupleLogins(Set<String> logins) throws DaoException {
        List<UsersEntity> result;
        try {
            session = util.getSession();
            String hql = "SELECT U FROM UsersEntity U WHERE U.login IN :loginsParam";
            Query query = session.createQuery(hql);
            query.setParameterList("loginsParam", logins);
            result = query.list();

            log.info("getAllByCoupleLogins in UserDao");
        } catch (HibernateException e) {
            String message = "Error getAllByCoupleLogins in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    public List<UsersEntity> getAllByBlockStatus(Boolean isBlocked) throws DaoException {
        List<UsersEntity> result;
        try {
            session = util.getSession();
            String hql = "SELECT U FROM UsersEntity U WHERE U.isBlocked=:blockParam";
            Query query = session.createQuery(hql);
            query.setParameter("blockParam", isBlocked);
            result = query.list();

            log.info("getAllByBlockStatus in UserDao");
        } catch (HibernateException e) {
            String message = "Error getAllByBlockStatus in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /*@Override
    public List<UsersEntity> getAll(int first, int max) throws DaoException{
        List<UsersEntity> result;
        try {
            session = util.getSession();
//            String hql = "FROM UsersEntity";
//            Query query = session.createQuery(hql);
            Criteria criteria = session.createCriteria(UsersEntity.class);
            criteria.setCacheable(true);
            criteria.setFirstResult(first);
            criteria.setMaxResults(max);
            result = criteria.list();
            log.info("getAll users. CountOfUsers=" + result.size());
        } catch (HibernateException e) {
            String message = "Error getAll users in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }*/

    /*public Long getTotal() throws DaoException{
        Long result;
        try {
            session = util.getSession();
            String hql = "SELECT count(id) FROM UsersEntity U";
            Query query = session.createQuery(hql);
            query.setCacheable(true);
            result = (Long) query.uniqueResult();
            log.info("getTotal users. CountOfUsers=" + result);
        } catch (HibernateException e) {
            String message = "Error getTotal users in UserDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }*/
}
