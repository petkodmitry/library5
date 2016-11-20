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

public class UserService implements Service<UsersEntity> {
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

    private void addToActiveUsers(String login) {
        /**
         * adds into List<String> active users
         */
        ActiveUsers.addUser(login);
    }

    private void removeFromActiveUsers(String login) {
        /**
         * removes from List<String> active users
         */
        ActiveUsers.removeUser(login);
    }

    /**
     * closes request session and removes user from active users list
     * @param request - request to be closed
     * @param login - user to be logOut
     */
    public void logOut(HttpServletRequest request, String login) {
        if (login != null) removeFromActiveUsers(login);
        request.getSession().invalidate();
    }

//    public boolean isLoginSuccessOLD(HttpServletRequest request, String login, String password) {
//        if (login == null || password == null) return false;
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
//            if (UserDaoOLD.getInstance().isLoginSuccess(connection, login, password)) {
//                connection.commit();
//                addToActiveUsers(login);
//                return true;
//            } else {
//                connection.rollback();
//                return false;
//            }
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return false;
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

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

    public boolean isAdminUser(HttpServletRequest request, String login) {
//        if (login == null) return true;
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

//    public Set<UserEntityOLD> getAllOLD(HttpServletRequest request) {
//        Connection connection = null;
//        Set<UserEntityOLD> result = new HashSet<>();
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            connection.setAutoCommit(false);
//            Set<String> allLogins = UserDaoOLD.getInstance().getAllLogins(connection);
//            for (String login : allLogins) {
//                result.add(UserDaoOLD.getInstance().getByLogin(connection, login));
//            }
//            if (allLogins.size() == result.size()) connection.commit();
//            else {
//                result = Collections.emptySet();
//                connection.rollback();
//            }
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptySet();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

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

//    public boolean isLoginExistsOLD(HttpServletRequest request, String login) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            String entityLogin = UserDaoOLD.getInstance().getByLogin(connection, login).getLogin();
//            if (login.equals(entityLogin)) return true;
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return false;
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return false;
//    }

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

    public boolean isAllPasswordRulesFollowed(String password, String repeatPassword) {
        /**
         * check for equality and the length
         */
        return password.equals(repeatPassword) && password.length() >= 8;
    }

//    public void addNewEntityToDataBaseOLD(HttpServletRequest request, String name, String lastName, String login, String password,
//                                       boolean isAdmin, boolean isBlocked) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            UserEntityOLD entity = UserDaoOLD.getInstance().createNewEntity(name, lastName, login, password, isAdmin, isBlocked);
//            UserDaoOLD.getInstance().add(connection, entity);
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

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
        }  finally {
            util.releaseSession(currentSession);
        }
    }

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

    /**/
//    public Set<UserEntityOLD> getUsersByBlockOLD(HttpServletRequest request, boolean isBlocked) {
//        Connection connection = null;
//        Set<UserEntityOLD> allByBlock = new HashSet<>();
//        try {
//            connection = PoolManager.getInstance().getConnection();
////            connection.setAutoCommit(false);
//            allByBlock = UserDaoOLD.getInstance().getAllByBlock(connection, isBlocked);
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptySet();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return allByBlock;
//    }

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

    public boolean isAllRegisterDataEntered (UsersEntity regData, String repeatPassword) {
        return !"".equals(regData.getFirstName()) &&
                !"".equals(regData.getLastName()) &&
                !"".equals(regData.getLogin()) &&
                !"".equals(regData.getPassword()) &&
                !"".equals(repeatPassword);
    }

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

    /*public UsersEntity createNewEntity(String firstName, String lastName, String login, String password,
                                       boolean isAdmin, boolean isBlocked) {
        UsersEntity result = new UsersEntity();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setLogin(login);
        result.setPassword(password);
        result.setIsAdmin(isAdmin);
        result.setIsBlocked(isBlocked);
        return result;
    }*/

    public void add(UsersEntity entity) {}

    public List<UsersEntity> getAll() {
        return null;
    }

    public UsersEntity getByLogin(String login) {
        return null;
    }

    public void update(UsersEntity entity) {}

    public void delete(int id) {

    }
}
