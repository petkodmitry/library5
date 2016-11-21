package com.petko.services;

import com.petko.ActiveUsers;
import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.dao.UserDao;
import com.petko.entities.UsersEntity;
import com.petko.utils.HibernateUtilLibrary;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class UserService {
    private static UserService instance;
    private static Logger log = Logger.getLogger(UserService.class);
    private static UserDao userDao = UserDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Adds into active users List
     * @param login to be added
     */
    private void addToActiveUsers(String login) {
        ActiveUsers.addUser(login);
    }

    /**
     * Removes from active users List
     * @param login to be removed
     */
    private void removeFromActiveUsers(String login) {
        ActiveUsers.removeUser(login);
    }

    /**
     * closes request session and removes user from active users list
     * @param request - current http request
     * @param login - user to be logOut
     */
    public void logOut(HttpServletRequest request, String login) {
        if (login != null) removeFromActiveUsers(login);
        request.getSession().invalidate();
    }

    /**
     * Checks if user is already logged in
     * @param request - current http request
     * @param login to be checked
     * @param password to be checked
     * @return succes or not
     */
    public boolean isLoginSuccess(HttpServletRequest request, String login, String password) {
        if (login == null || password == null) return false;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity user = userDao.getByLogin(login);

            transaction.commit();
            log.info("Get user by login (commit)");
            if (user != null && password.equals(user.getPassword())) {
                addToActiveUsers(login);
                return true;
            } else {
                return false;
            }
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return false;
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**
     * Checks if the User is Admin or not
     * @param request - current http request
     * @param login to be checked
     * @return admin or not (true or false)
     */
    public boolean isAdminUser(HttpServletRequest request, String login) {
        boolean result = false;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity user = userDao.getByLogin(login);
            if (user != null) result = user.getIsAdmin();
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return false;
        }  finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /**
     * gives List of all Users
     * @param request - current http request
     * @param page to be shown in WEB
     * @return List of all Users
     */
    public List<UsersEntity> getAll(HttpServletRequest request, String page/*, int max*/) {
        List<UsersEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        HttpSession httpSession = request.getSession();
        int max = 2;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            int firstInt;
            if (page == null) {
                Long total = userDao.getTotal();
                log.info("getTotal users (commit)");
                httpSession.setAttribute("total", total);
                httpSession.setAttribute("max", max);
                firstInt = 0;
            } else {
                firstInt = (Integer.parseInt(page) - 1) * max;
            }
            result = userDao.getAll(firstInt, max);
            transaction.commit();
            log.info("getAll users (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        }  finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /**
     * Checks if login exists in DataBase
     * @param request - current http request
     * @param login to be checked
     * @return exist or not (true or false)
     */
    public boolean isLoginExists(HttpServletRequest request, String login) {
        boolean result = false;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity userEntity = userDao.getByLogin(login);
            String entityLogin = null;
            if (userEntity != null) entityLogin = userEntity.getLogin();
            if (login.equals(entityLogin)) result = true;

            transaction.commit();
            log.info("Get user by login (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        }  finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /**
     * check for equality and the length
     * @param password to be checked
     * @param repeatPassword to check for equality with password
     * @return true or false
     */
    public boolean isAllPasswordRulesFollowed(String password, String repeatPassword) {
        return password.equals(repeatPassword) && password.length() >= 8;
    }

    /**
     * Adds User to DataBase
     * @param request - current http request
     * @param entity to be added
     */
    public void add(HttpServletRequest request, UsersEntity entity) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            userDao.save(entity);

            transaction.commit();
            log.info("Save user to DB (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**
     * Changes the status of the User
     * @param request - current http request
     * @param login to be updeted
     * @param isBlocked - status to be setted
     */
    public void setBlockUser(HttpServletRequest request, String login, boolean isBlocked) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            UsersEntity entity = userDao.getByLogin(login);
            if (entity != null) {
                entity.setIsBlocked(isBlocked);
                userDao.update(entity);
            }

            transaction.commit();
            log.info("Get user by login (commit)");
            log.info("Update user (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        }  finally {
            util.releaseSession(currentSession);
        }
    }

    /**
     * Gets Users by their status
     * @param request - current http request
     * @param isBlocked - status to be searched
     * @return Users by their status
     */
    public List<UsersEntity> getUsersByBlock(HttpServletRequest request, boolean isBlocked) {
        Session currentSession = null;
        Transaction transaction = null;
        List<UsersEntity> allByBlock = new ArrayList<>();
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            allByBlock = userDao.getAllByBlockStatus(isBlocked);
            transaction.commit();
            log.info("Get all users by block status (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        }  finally {
            util.releaseSession(currentSession);
        }
        return allByBlock;
    }

    /**
     * Checks if all register data is entered
     * @param regData - all data to be checked
     * @param repeatPassword to compare with password
     * @return true or false
     */
    public boolean isAllRegisterDataEntered (UsersEntity regData, String repeatPassword) {
        return !"".equals(regData.getFirstName()) &&
                !"".equals(regData.getLastName()) &&
                !"".equals(regData.getLogin()) &&
                !"".equals(regData.getPassword()) &&
                !"".equals(repeatPassword);
    }

    /**
     * Creates and gives a new User
     * @param result - result
     * @param firstName - firstName
     * @param lastName - lastName
     * @param login - login
     * @param password - password
     * @param isAdmin - isAdmin
     * @param isBlocked - isBlocked
     * @return a new User
     */
    public UsersEntity setAllDataOfEntity(UsersEntity result, String firstName, String lastName, String login, String password,
                                       boolean isAdmin, boolean isBlocked) {
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setLogin(login);
        result.setPassword(password);
        result.setIsAdmin(isAdmin);
        result.setIsBlocked(isBlocked);
        return result;
    }
}
