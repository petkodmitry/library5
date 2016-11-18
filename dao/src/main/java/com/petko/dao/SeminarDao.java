package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities2.SeminarsEntity;
import com.petko.entities2.UsersEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

public class SeminarDao extends BaseDao<SeminarsEntity> {
    private static Logger log = Logger.getLogger(SeminarDao.class);

    private static SeminarDao instance;
    private SeminarDao() {}

    public static synchronized SeminarDao getInstance() {
        if (instance == null) {
            instance = new SeminarDao();
        }
        return instance;
    }

    public List<SeminarsEntity> getSeminarsByLogin(String login) throws DaoException {
        List<SeminarsEntity> result;
        try {
            session = util.getSession();
            String hql = "SELECT S FROM SeminarsEntity S JOIN S.users U WHERE U.login=:param AND S.seminarDate>=:param2";
            Query query = session.createQuery(hql);
//            query.setCacheable(true);
            query.setParameter("param", login);
            query.setParameter("param2", new Date());
            result = query.list();
            log.info("Get seminars by login");
        } catch (HibernateException e) {
            String message = "Error getting seminars by login in SeminarDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /*public List<SeminarsEntity> chooseSeminarsForLogin(String login) throws DaoException {
        List<SeminarsEntity> result;
        try {
            session = util.getSession();

            String hql = "SELECT S FROM SeminarsEntity S WHERE " +
                    "((SELECT U.login FROM S.users U) != :param) " +
                    "AND S.seminarDate>=:param2";
            Query query = session.createQuery(hql);
            query.setParameter("param", login);
            query.setParameter("param2", new Date());
            result = query.list();

            *//*Criteria criteria = session.createCriteria(SeminarsEntity.class);
            UsersEntity userEntity = UserDao.getInstance().getByLogin(login);
            Criterion logins =  (Restrictions.like("users.contains(userEntity)", login));
            Criterion dates = (Restrictions.ge("seminarDate", new Date()));
            LogicalExpression andExp = Restrictions.and(logins, dates);
            criteria.add(andExp);
            result = criteria.list();*//*

            log.info("Choose seminars for login");
        } catch (HibernateException e) {
            String message = "Error choosing seminars for login in SeminarDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }*/

    public List<SeminarsEntity> getAll() throws DaoException{
        List<SeminarsEntity> result;
        try {
            session = util.getSession();
            String hql = "FROM SeminarsEntity S WHERE S.seminarDate>=:param";
            Query query = session.createQuery(hql);
            query.setParameter("param", new Date());
            result = query.list();
            log.info("getAll seminars.");
        } catch (HibernateException e) {
            String message = "Error getAll seminars in SeminarDao";
            log.error(message + e);
            throw new DaoException(message);
        }
        return result;
    }

    /*public Long getTotal() throws DaoException{
        Long result;
        try {
            session = util.getSession();
            String hql = "SELECT count(id) FROM SeminarsEntity U";
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
