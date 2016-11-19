package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.SeminarsEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

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

    public List<SeminarsEntity> getAll() throws DaoException{
        List<SeminarsEntity> result;
        try {
            session = util.getSession();
            String hql = "FROM SeminarsEntity S WHERE S.seminarDate>=:param ORDER BY S.seminarDate ASC";
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
}
