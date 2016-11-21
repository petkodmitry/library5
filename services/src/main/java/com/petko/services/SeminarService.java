package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.constants.Constants;
import com.petko.dao.SeminarDao;
import com.petko.dao.UserDao;
import com.petko.entities.SeminarsEntity;
import com.petko.entities.UsersEntity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class SeminarService {
    private static SeminarService instance;
    private static Logger log = Logger.getLogger(SeminarService.class);
    private static SeminarDao seminarDao = SeminarDao.getInstance();
    private static UserDao userDao = UserDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private SeminarService() {}

    public static synchronized SeminarService getInstance() {
        if(instance == null){
            instance = new SeminarService();
        }
        return instance;
    }

    /**
     * gives List of Seminars for User
     * @param request - current http request
     * @param login - login of the User
     * @return List of Seminars for User
     */
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

    /**
     * Subscribe User to Seminar
     * @param request - current http request
     * @param login of the User to subscribe
     * @param seminarId to be subscribed
     */
    public void subscribeToSeminar(HttpServletRequest request, String login, int seminarId) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity user = userDao.getByLogin(login);
            SeminarsEntity seminar = seminarDao.getById(seminarId);
            if (user != null && seminar != null) user.getSeminars().add(seminar);
            else request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Не удалось записаться на выбранный семинар");

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

    /**
     * UnSubscribe User from Seminar
     * @param request - current http request
     * @param login of the User to unSubscribe
     * @param seminarId to be unSubscribed
     */
    public void unSubscribeSeminar(HttpServletRequest request, String login, int seminarId) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity user = userDao.getByLogin(login);
            SeminarsEntity seminar = seminarDao.getById(seminarId);
            if (user != null && seminar != null) user.getSeminars().remove(seminar);
            else request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Не удалось отписаться от выбранного семинара");
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

    /**
     * gives List of Seminars for specific User
     * @param request - current http request
     * @param login to be searched in seminars
     * @return List of Seminars for specific User
     */
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

    /**
     * gives all future Seminars
     * @param request - current http request
     * @return all future Seminars
     */
    public List<SeminarsEntity> getAll(HttpServletRequest request) {
        List<SeminarsEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = seminarDao.getAll();

            transaction.commit();
            log.info("Get all seminars >= today (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /**
     * adds SeminarsEntity to DataBase
     * @param request - current http request
     * @param entity to be added
     */
    public void add(HttpServletRequest request, SeminarsEntity entity) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            seminarDao.save(entity);

            transaction.commit();
            log.info("Add seminar to DB (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**
     * removes SeminarsEntity from DataBase
     * @param request - current http request
     * @param id of the Seminar to be deleted
     */
    public void delete(HttpServletRequest request, int id) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            SeminarsEntity entity = seminarDao.getById(id);
            if (entity != null) {
                entity.getUsers().clear();
                seminarDao.delete(entity);
            } else request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Не удалось удалить выбранный семинар");

            transaction.commit();
            log.info("Get seminar by id (commit)");
            log.info("Delete seminar from DB (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**
     * gives Seminar by its ID
     * @param request - current http request
     * @param id to be searched
     * @return Seminar by its ID
     */
    public SeminarsEntity getById(HttpServletRequest request, int id) {
        SeminarsEntity result = null;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = seminarDao.getById(id);

            transaction.commit();
            log.info("Get seminar by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }
}
