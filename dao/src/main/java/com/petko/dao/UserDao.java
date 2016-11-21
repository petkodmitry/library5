package com.petko.dao;

import com.petko.DaoException;
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

    /**
     * gets User by his Login
     * @param login of searched User
     * @return User by his Login
     * @throws DaoException
     */
    public UsersEntity getByLogin(String login) throws DaoException {
        UsersEntity result;
        try {
            session = util.getSession();
            String hql = "select U from UsersEntity U where U.login=:param";
            Query query = session.createQuery(hql);
//            query.setCacheable(true);
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

    /**
     * gets Users by their Logins
     * @param logins - Set of searched Users' logins
     * @return Users by their Logins
     * @throws DaoException
     */
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

    /**
     * gets Users by their Block status
     * @param isBlocked - status to be selected
     * @return Users by their Block status
     * @throws DaoException
     */
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
}
