package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.dao.SeminarDao;
import com.petko.dao.UserDao;
import com.petko.entities2.SeminarsEntity;
import com.petko.entities2.UsersEntity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class SeminarService implements Service<SeminarsEntity>{
    private static SeminarService instance;
    private static Logger log = Logger.getLogger(SeminarService.class);
    private static SeminarDao seminarDao = SeminarDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private SeminarService() {}

    public static synchronized SeminarService getInstance() {
        if(instance == null){
            instance = new SeminarService();
        }
        return instance;
    }

    public List<SeminarsEntity> getSeminarsByLogin(HttpServletRequest request, String login) {
        List<SeminarsEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = seminarDao.getSeminarsByLogin(login);

            transaction.commit();
            log.info("Get seminars by login (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    public void unSubscribeSeminar(HttpServletRequest request, String login, int seminarId) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity user = UserDao.getInstance().getByLogin(login);
            user.getSeminars().remove(seminarDao.getById(seminarId));

            transaction.commit();
            log.info("Get user by login (commit)");
            log.info("Get seminar by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    public List<SeminarsEntity> availableSeminarsForLogin(HttpServletRequest request, String login) {
        List<SeminarsEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = seminarDao.getAll();
            result.removeAll(seminarDao.getSeminarsByLogin(login));

            transaction.commit();
            log.info("Get all seminars >= today (commit)");
            log.info("Get seminars by login (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    public void add(SeminarsEntity entity) {
    }

    public List<SeminarsEntity> getAll() {
        return null;
    }

    public SeminarsEntity getByLogin(String login) {
        return null;
    }

    public void update(SeminarsEntity entity) {
    }

    public void delete(int id) {
    }
}
