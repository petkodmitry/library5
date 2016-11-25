package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.constants.Constants;
import com.petko.dao.BookDao;
import com.petko.dao.OrderDao;
import com.petko.dao.UserDao;
import com.petko.entities.*;
import com.petko.utils.HibernateUtilLibrary;
import com.petko.vo.FullOrdersList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class OrderService {
    private static OrderService instance;
    private static Logger log = Logger.getLogger(OrderService.class);
    private static OrderDao orderDao = OrderDao.getInstance();
    private static BookDao bookDao = BookDao.getInstance();
    private static UserDao userDao = UserDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private OrderService() {
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    /*** getting orders list by login and order status
     * @param request     - current http request
     * @param login       - user, whose orders are taken
     * @param orderStatus - with which status orders are taken
     * @return the List of orders according to the conditions */
    public List<FullOrdersList> getOrdersByLoginAndStatus(HttpServletRequest request, String login, OrderStatus orderStatus) {
        List<FullOrdersList> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            // getting required List of orders, but without Book details (book ID only)
            List<OrdersEntity> orderEntityList = orderDao.getOrdersByLoginAndStatus(login, orderStatus);
            if (!orderEntityList.isEmpty()) {
                // getting IDs of all books in received List
                Set<Integer> bookIds = orderEntityList.stream().map(OrdersEntity::getBookId).collect(Collectors.toSet());
                // receiving a List of the Books by Set of IDs.
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                // creating a Map of IDs-Books, for easier way to get Book's properties by ID without DataBase queries
                Map<Integer, BooksEntity> booksMap = getMapOfIDsAndBooks(booksEntities);
                // building an FullOrdersList entity and passing it to the result List
                result = buildFullOrdersList(orderEntityList, booksMap, null, null);
                log.info("Get all books by couple ids (commit)");
            }
            transaction.commit();
            log.info("Get orders by login and status (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /*** getting expired orders list by endDate
     * @param request - current http request
     * @return the List of orders according to the conditions */
    public List<FullOrdersList> getExpiredOrders(HttpServletRequest request) {
        List<FullOrdersList> result = new ArrayList<>();
        OrderStatus orderStatus = OrderStatus.ON_HAND;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            Date today = new Date();
            Date currentDate = new Date(today.getYear(), today.getMonth(), today.getDate());
            List<OrdersEntity> listByStatus = orderDao.getOrdersByStatusAndEndDate(orderStatus, currentDate);
            if (!listByStatus.isEmpty()) {
                // getting IDs of all books in received List
                Set<Integer> bookIds = listByStatus.stream().map(OrdersEntity::getBookId).collect(Collectors.toSet());
                // getting all Logins from received List
                Set<String> allLogins = listByStatus.stream().map(OrdersEntity::getLogin).collect(Collectors.toSet());
                // receiving a List of the Books by Set of IDs.
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                // creating a Map of IDs-Books, for easier way to get Book's properties by ID without DataBase queries
                Map<Integer, BooksEntity> booksMap = getMapOfIDsAndBooks(booksEntities);
                // receiving a List of the Users by Set of Logins.
                List<UsersEntity> usersEntities = userDao.getAllByCoupleLogins(allLogins);
                // creating a Map of logins-Users, for easier way to get User's properties by Login without DataBase queries
                Map<String, UsersEntity> usersMap = getMapOfLoginsAndUsers(usersEntities);
                // building an FullOrdersList entity and passing it to the result List
                result = buildFullOrdersList(listByStatus, booksMap, usersMap, currentDate);
                log.info("Get all books by couple ids (commit)");
                log.info("Get all users by couple logins (commit)");
            }
            transaction.commit();
            log.info("Get orders by status and endDate (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            util.releaseSession(currentSession);
        }
        return result;
    }

    /*** sets Order status to CLOSED
     * @param request - current http request
     * @param login   - the method checks if (User by login) closes his order, not of other Users
     * @param orderID - ID of the order to be closed */
    public void closeOrder(HttpServletRequest request, String login, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            OrdersEntity entity = orderDao.getById(orderID);
            log.info("Get order by id (commit)");
            // if User brought book to the Library, we mark Book as free
            if (login == null && OrderStatus.ON_HAND.toString().equals(entity.getStatus())) {
                BooksEntity book = bookDao.getById(entity.getBookId());
                book.setIsBusy(false);
                bookDao.update(book);
                log.info("Get book by id (commit)");
                log.info("update book (commit)");
            }
            if ((login == null) ||
                    (entity.getLogin().equals(login) && OrderStatus.ORDERED.toString().equals(entity.getStatus()))) {
                entity.setStatus(OrderStatus.CLOSED.toString());
                entity.setEndDate(new Date(Calendar.getInstance().getTime().getTime()));
                orderDao.update(entity);
                log.info("update order (commit)");
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /*** sets Order status to ORDERED
     * @param request  - current http request
     * @param login    - action for User by login exactly
     * @param bookID   - ID of the Book to be ordered
     * @param isToHome - is order to home (true) or to reeding room (false) */
    public void orderToHomeOrToRoom(HttpServletRequest request, String login, int bookID, Boolean isToHome) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            UsersEntity userEntity = userDao.getByLogin(login);
            BooksEntity bookEntity = bookDao.getById(bookID);
            String[] statuses = {OrderStatus.ON_HAND.toString(), OrderStatus.ORDERED.toString()};
            OrdersEntity newEntity;
            // if User is in BlackList, he can't order any book
            if (userEntity == null || userEntity.getIsBlocked()) {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Вы не можете заказывать книги, находясь в 'Чёрном списке'.");
            } else {
                // if asked Book is busy - find another exemplar
                if (bookEntity != null && bookEntity.getIsBusy()) {
                    bookEntity = getIdOfVacantBookByTitleAndAuthor(bookEntity.getTitle(), bookEntity.getAuthor());
                    if (bookEntity != null) bookID = bookEntity.getBookId();
                }
                // if all the exemplars are busy - tell User about it
                if (bookEntity == null || bookEntity.getIsBusy()) {
                    request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Все экземпляры данной книги выданы.");
                    // if the User already have this Book (or another exemplar) in his active orders (Ordered or OnHand) - tell him about it
                } else if (!orderDao.getOrdersByLoginBookIdStatuses(login, bookEntity, statuses).isEmpty()) {
                    request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Ваш заказ на эту книгу имеется и активен");
                    /*** otherwise (if at least one exemplar is vacant,
                     * if the User is not blocked,
                     * if the User hasn't ordered this Book yet)
                     * create a new Order! */
                } else {
                    long delay = 0L;
                    PlaceOfIssue place = PlaceOfIssue.READING_ROOM;
                    if (isToHome) {
                        delay = 30L * 24L * 60L * 60L * 1_000L;
                        place = PlaceOfIssue.HOME;
                    }
                    Date startDate = new Date(Calendar.getInstance().getTime().getTime());
                    Date endDate = new Date(startDate.getTime() + delay);
                    newEntity = createNewEntity(login, bookID, OrderStatus.ORDERED.toString(), place.toString(),
                            startDate, endDate);
                    currentSession.save(newEntity);
                    log.info("save order (commit)");
                }
            }
            transaction.commit();
            log.info("Get user by Login (commit)");
            log.info("Get book by ID (commit)");
            log.info("Get orders by login, bookId and statuses (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /*** searches vacant Book with required title and author
     * @param title  of searched exemplars
     * @param author of searched exemplars
     * @return null or vacant Book with required title and author
     * @throws DaoException */
    private BooksEntity getIdOfVacantBookByTitleAndAuthor(String title, String author) throws DaoException {
        BooksEntity result = null;
        List<BooksEntity> list = bookDao.getBooksByTitleAndAuthorAndStatus(title, author, false);
        for (BooksEntity entity : list) {
            if (!entity.getIsBusy()) {
                result = entity;
                break;
            }
        }
        return result;
    }

    /*** sets new endDate of the Order, if the Order is not expired and current endDate not later then 'interval' days from today
     * @param request - current http request
     * @param login   - action for User by login exactly
     * @param orderID - ID of the Order to be prolonged */
    public void prolongOrder(HttpServletRequest request, String login, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            OrdersEntity entity = orderDao.getById(orderID);
            if (entity != null && entity.getLogin().equals(login) && OrderStatus.ON_HAND.toString().equals(entity.getStatus())) {
                // time interval from now till the end date of the order. In case not to allow a user indefinitely prolong his order
                int interval = 5;
                long gap = 30L * 24L * 60L * 60L * 1_000L;
                long delay = interval * 24L * 60L * 60L * 1_000L;
                Date endDate = entity.getEndDate();
                Date today = new Date();
                Date currentDate = new Date(today.getYear(), today.getMonth(), today.getDate());
                long difference = endDate.getTime() - currentDate.getTime();
                if (difference >= 0 && (difference - delay) <= interval) {
                    entity.setEndDate(new Date(endDate.getTime() + gap));
                    orderDao.update(entity);
                    log.info("update order (commit)");
                } else {
                    request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ не должен быть просрочен, " +
                            "и время до его окончания не должно превышать " + interval + " дней");
                }
            }
            transaction.commit();
            log.info("Get order by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /*** sets the Order status to ON_HAND, Book status to busy
     * @param request - current http request
     * @param orderID - ID of Order to be provided */
    public void provideBook(HttpServletRequest request, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            OrdersEntity entity = orderDao.getById(orderID);
            BooksEntity bookEntity = null;
            if (entity != null) bookEntity = bookDao.getById(entity.getBookId());
            if (bookEntity == null) {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Не удалось обратиться к книге");
            } else {
                // if Book is busy, search another exemplar
                if (bookEntity.getIsBusy()) {
                    BooksEntity freeBook = getIdOfVacantBookByTitleAndAuthor(bookEntity.getTitle(), bookEntity.getAuthor());
                    if (freeBook != null) {
                        // change Book in the Order
                        bookEntity = freeBook;
                        entity.setBookId(bookEntity.getBookId());
                    } else {
                        request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Эта книга уже выдана!");
                    }
                }
                // if another exemplar was found
                if (!bookEntity.getIsBusy()) {
                    // if status of The Order wasn't changed and is ORDERED ("Открыт")
                    if (entity.getStatus().equals(OrderStatus.ORDERED.toString())) {
                        long delay = 0L;
                        if (PlaceOfIssue.HOME.toString().equals(entity.getPlaceOfIssue())) {
                            delay = 30L * 24L * 60L * 60L * 1_000L;
                        }
                        Date today = new Date();
                        Date currentDate = new Date(today.getYear(), today.getMonth(), today.getDate());
                        Date newEndDate = new Date(currentDate.getTime() + delay);
                        entity.setStatus(OrderStatus.ON_HAND.toString());
                        entity.setEndDate(newEndDate);
                        orderDao.update(entity);
                        log.info("Update order (commit)");
                        bookEntity.setIsBusy(true);
                        bookDao.update(bookEntity);
                        log.info("Update book (commit)");
                    } else {
                        request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Проверьте статус заказа!");
                    }
                }
            }
            transaction.commit();
            log.info("Get order by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    public OrdersEntity getById(HttpServletRequest request, int orderID) {
        OrdersEntity answer = null;
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            answer = orderDao.getById(orderID);
            transaction.commit();
            log.info("Get order by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
        return answer;
    }

    public OrdersEntity createNewEntity(String login, int bookId, String status, String place, Date startDate, Date endDate) {
        OrdersEntity result = new OrdersEntity();
        result.setLogin(login);
        result.setBookId(bookId);
        result.setStatus(status);
        result.setPlaceOfIssue(place);
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        return result;
    }

    private Map<Integer, BooksEntity> getMapOfIDsAndBooks(List<BooksEntity> booksEntities) {
        Map<Integer, BooksEntity> booksMap = new HashMap<>();
        for (BooksEntity book : booksEntities) {
            booksMap.put(book.getBookId(), book);
        }
        return booksMap;
    }

    private Map<String, UsersEntity> getMapOfLoginsAndUsers(List<UsersEntity> usersEntities) {
        Map<String, UsersEntity> usersMap = new HashMap<>();
        for (UsersEntity user : usersEntities) {
            usersMap.put(user.getLogin(), user);
        }
        return usersMap;
    }

    private List<FullOrdersList> buildFullOrdersList(List<OrdersEntity> orderEntityList, Map<Integer, BooksEntity> booksMap,
                                                     Map<String, UsersEntity> usersMap, Date currentDate) {
        List<FullOrdersList> result = new ArrayList<>();
        for (OrdersEntity entity : orderEntityList) {
            int bookId = entity.getBookId();
            BooksEntity book = booksMap.get(bookId);
            String login = entity.getLogin();
            FullOrdersList orderView = new FullOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
                    entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
            if (book != null) {
                orderView.setTitle(book.getTitle());
                orderView.setAuthor(book.getAuthor());
            }
            if (usersMap != null) {
                UsersEntity user = usersMap.get(login);
                if (user != null) orderView.setBlocked(user.getIsBlocked());
            }
            if (currentDate != null) {
                long oneDay = 24L * 60L * 60L * 1_000L;
                int delayDays = (int) ((currentDate.getTime() - entity.getEndDate().getTime()) / oneDay);
                orderView.setDelayDays(delayDays);
            }
            result.add(orderView);
        }
        return result;
    }
}
