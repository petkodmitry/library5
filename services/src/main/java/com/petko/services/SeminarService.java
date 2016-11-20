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

public class SeminarService implements Service<SeminarsEntity>{
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

    public Set<UsersEntity> getUsersBySeminar(HttpServletRequest request, Integer seminarId) {
        Set<UsersEntity> result = new HashSet<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            result = seminarDao.getById(seminarId).getUsers();

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

    public void add(HttpServletRequest request, SeminarsEntity entity) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

//            seminarDao.saveOrUpdate(entity);
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
