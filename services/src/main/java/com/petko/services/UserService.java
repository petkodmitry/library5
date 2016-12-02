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
     * @return List of all Users
     */
    public List<UsersEntity> getAll(HttpServletRequest request) {
        List<UsersEntity> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        HttpSession httpSession = request.getSession();
        String page = request.getParameter("page");

        // for sorting
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null) sortBy = (String) httpSession.getAttribute("sortBy");
        String orderType = request.getParameter("orderType");
        if (orderType == null) orderType = (String) httpSession.getAttribute("orderType");

        // for filtering
        Map<String, String> filters = (Map<String, String>) httpSession.getAttribute("filters");
        if (filters == null) filters = new HashMap<>();
        Map<String, String[]> parMap = request.getParameterMap();
        for (String parameter : parMap.keySet()) {
            if (parameter.endsWith("Filter")) {
                String paramToPut = parameter.substring(0, parameter.indexOf("Filter"));
                String paramValue = parMap.get(parameter)[0];
                if (!"".equals(paramValue)) filters.put(paramToPut, paramValue);
            }
        }
        String filterRemove = request.getParameter("filterRemove");
        if (filterRemove != null) filters.remove(filterRemove);
        httpSession.setAttribute("filters", filters);

        // go ahead
        String perPageString = request.getParameter("perPage");
        Integer newPerPage = perPageString != null ? Integer.parseInt(perPageString) : null;
        Integer oldPerPage = (Integer) httpSession.getAttribute("max");
        Integer newMax;
        if (newPerPage != null) {
            newMax = newPerPage;
        }
        else newMax = oldPerPage != null ? oldPerPage : 5;

        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            int firstInt;
            if (page == null) {
                Long total = userDao.getTotal();
                log.info("getTotal users (commit)");
                httpSession.setAttribute("total", total);
                firstInt = 0;
            } else {
                Integer pageInt = Integer.parseInt(page);
                Integer newPageInt = getPageDueToNewPerPage(request, httpSession, pageInt, newPerPage, oldPerPage);
                if (sortBy != null && orderType != null) {
                    httpSession.setAttribute("sortBy", sortBy);
                    httpSession.setAttribute("orderType", orderType);
                }
                firstInt = (newPageInt - 1) * newMax;
            }
            httpSession.setAttribute("totalToShow", userDao.getAll(0,  ((Long) httpSession.getAttribute("total")).intValue(), sortBy, orderType, filters).size());
            result = userDao.getAll(firstInt, newMax, sortBy, orderType, filters);
            httpSession.setAttribute("max", newMax);
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
     * Re-estimates current page due to perPage parameter changes
     * @param request - current httpRequest
     * @param session - current httpSession
     * @param page - current page (not re-estimated)
     * @param newPerPage - a new perPage parameter
     * @param oldPerPage - perPage parameter which is saved in httpSession
     * @return the correct page due to perPage changes
     */
    private Integer getPageDueToNewPerPage(HttpServletRequest request, HttpSession session, Integer page,
                                           Integer newPerPage, Integer oldPerPage) {
        Integer result;
        Integer totalToShow = (Integer) session.getAttribute("totalToShow");
        if ((newPerPage == null || oldPerPage == null) || (newPerPage.equals(oldPerPage))) {
            result = page;
        } else if (newPerPage > oldPerPage) {
            result = changeAndGiveCurrentPage(page, totalToShow, newPerPage, oldPerPage, true);
        } else {
            result = changeAndGiveCurrentPage(page, totalToShow, newPerPage, oldPerPage, false);
        }
        request.setAttribute("page", result);
        return result;
    }

    /**
     * Re-estimates current page due to perPage parameter changes
     * @param page - current page (not re-estimated)
     * @param totalToShow - the whole amount of records (including filters)
     * @param newPerPage - a new perPage parameter
     * @param oldPerPage - perPage parameter which is saved in httpSession
     * @param isMoreRecords - is new perPage bigger than current
     * @return the correct page due to perPage changes
     */
    private Integer changeAndGiveCurrentPage(Integer page, Integer totalToShow, Integer newPerPage,
                                             Integer oldPerPage, boolean isMoreRecords) {
        Integer result;
        int temp = page * oldPerPage / newPerPage;
        int rest = page * oldPerPage % newPerPage;
        if (isMoreRecords) {
            result = rest != 0 ? temp + 1 : temp;
        } else result = rest != 0 ? temp - 1 : temp;
        rest =  totalToShow % newPerPage;
        Integer endPage = rest != 0 ? (totalToShow - rest) / newPerPage + 1 : totalToShow / newPerPage;
        if (endPage < result) result = endPage;
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
            if (login != null && login.equals(entityLogin)) result = true;

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
        return password != null && password.equals(repeatPassword) && password.length() >= 8;
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
        return regData != null &&
                regData.getFirstName() != null && !"".equals(regData.getFirstName()) &&
                regData.getLastName() != null && !"".equals(regData.getLastName()) &&
                regData.getLogin() != null && !"".equals(regData.getLogin()) &&
                regData.getPassword() != null && !"".equals(regData.getPassword()) &&
                repeatPassword != null && !"".equals(repeatPassword);
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
