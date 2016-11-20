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
import com.petko.vo.OrderForMyOrdersList;
import com.petko.vo.AnyStatusOrdersList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class OrderService implements Service<OrdersEntity>{
    private static OrderService instance;
    private static Logger log = Logger.getLogger(OrderService.class);
    private static OrderDao orderDao = OrderDao.getInstance();
    private static BookDao bookDao = BookDao.getInstance();
    private static UserDao userDao = UserDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private OrderService() {}

    public static synchronized OrderService getInstance() {
        if(instance == null){
            instance = new OrderService();
        }
        return instance;
    }

//    public List<OrderForMyOrdersList> getOrdersByLoginAndStatusOLD(HttpServletRequest request, String login, OrderStatus orderStatus) {
//        List<OrderForMyOrdersList> result = new ArrayList<>();
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            Set<OrdersEntity> orderEntityList = null;
//            Set<OrdersEntity> listByStatus = null;
////            Set<OrdersEntity> orderEntityList = OrderDaoOLD.getInstance().getAllByUser(connection, login);
////            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
//            orderEntityList.retainAll(listByStatus);
//
//            for (OrdersEntity entity: orderEntityList) {
//                OrderForMyOrdersList orderView = new OrderForMyOrdersList(entity.getOrderId(), entity.getBookId(),
//                        entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
//                BooksEntity bookEntity = null;
////                BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
//                orderView.setTitle(bookEntity.getTitle());
//                orderView.setAuthor(bookEntity.getAuthor());
//                result.add(orderView);
//            }
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptyList();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

    /**
     * getting orders list by login and order status
     * @param request - current http request
     * @param login - user, whose orders are taken
     * @param orderStatus - with which status orders are taken
     * @return the List of orders according to the conditions
     */
    public List<OrderForMyOrdersList> getOrdersByLoginAndStatus(HttpServletRequest request, String login, OrderStatus orderStatus) {
        List<OrderForMyOrdersList> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();
            /**
             * getting required List of orders, but without Book details (book ID only)
             */
            List<OrdersEntity> orderEntityList = orderDao.getOrdersByLoginAndStatus(login, orderStatus);

            if (!orderEntityList.isEmpty()) {
                /**
                 * getting IDs of all books in received List
                 */
                Set<Integer> bookIds = new HashSet<>();
                for (OrdersEntity entity : orderEntityList) {
                    bookIds.add(entity.getBookId());
                }
                /**
                 * receiving a List of the Books by Set of IDs.
                 * creating a Map of IDs-Books, for easier way to get Book's properties by ID without DataBase queries
                 */
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                Map<Integer, BooksEntity> booksMap = new HashMap<>();
                for (BooksEntity book : booksEntities) {
                    booksMap.put(book.getBookId(), book);
                }
                /**
                 * building an OrderForMyOrdersList entity and passing it to the result List
                 */
                for (OrdersEntity entity : orderEntityList) {
                    int bookId = entity.getBookId();
                    BooksEntity book = booksMap.get(bookId);
                    OrderForMyOrdersList orderView = new OrderForMyOrdersList(entity.getOrderId(), bookId,
                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                    orderView.setTitle(book.getTitle());
                    orderView.setAuthor(book.getAuthor());
                    result.add(orderView);
                }
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

//    public List<AnyStatusOrdersList> getOrdersByStatusOLD(HttpServletRequest request, OrderStatus orderStatus) {
//        List<AnyStatusOrdersList> result = new ArrayList<>();
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            Set<OrdersEntity> listByStatus = null;
////            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
//
//            for (OrdersEntity entity: listByStatus) {
//                AnyStatusOrdersList orderView = new AnyStatusOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
//                        entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
//                BooksEntity bookEntity = null;
////                BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
//                orderView.setTitle(bookEntity.getTitle());
//                orderView.setAuthor(bookEntity.getAuthor());
//                result.add(orderView);
//            }
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptyList();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

    //TODO объединить с getOrdersByLoginAndStatus()
    public List<AnyStatusOrdersList> getOrdersByStatus(HttpServletRequest request, OrderStatus orderStatus) {
        List<AnyStatusOrdersList> result = new ArrayList<>();
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            List<OrdersEntity> listByStatus = orderDao.getOrdersByLoginAndStatus(null, orderStatus);
            List<OrdersEntity> listByDate = orderDao.getOrdersByLoginAndStatus(null, orderStatus);

            if (!listByStatus.isEmpty()) {
                /**
                 * getting IDs of all books in received List
                 */
                Set<Integer> bookIds = new HashSet<>();
                for (OrdersEntity entity : listByStatus) {
                    bookIds.add(entity.getBookId());
                }
                /**
                 * receiving a List of the Books by Set of IDs.
                 * creating a Map of IDs-Books, for easier way to get Book's properties by ID without DataBase queries
                 */
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                Map<Integer, BooksEntity> booksMap = new HashMap<>();
                for (BooksEntity book : booksEntities) {
                    booksMap.put(book.getBookId(), book);
                }
                /**
                 * building an OrderForMyOrdersList entity and passing it to the result List
                 */
                for (OrdersEntity entity : listByStatus) {
                    int bookId = entity.getBookId();
                    BooksEntity book = booksMap.get(bookId);
                    AnyStatusOrdersList orderView = new AnyStatusOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                    orderView.setTitle(book.getTitle());
                    orderView.setAuthor(book.getAuthor());
                    result.add(orderView);
                }
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

    /**/
//    public List<FullOrdersList> getExpiredOrdersOLD(HttpServletRequest request) {
//        List<FullOrdersList> result = new ArrayList<>();
//        OrderStatus orderStatus = OrderStatus.ON_HAND;
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            Set<OrdersEntity> listByStatus = null;
////            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
//            Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
//
//            for (OrdersEntity entity: listByStatus) {
//                long oneDay = 24L * 60L * 60L * 1_000L;
//                int delayDays = (int) ((currentDate.getTime() - entity.getEndDate().getTime())/oneDay);
//                if (delayDays > 0) {
//                    FullOrdersList orderView = new FullOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
//                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
//                    BooksEntity bookEntity = null;
//                    UsersEntity userEntity = null;
////                    BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
////                    UsersEntity userEntity = UserDaoOLD.getInstance().getByLogin(connection, entity.getLogin());
//                    orderView.setBlocked(userEntity.getIsBlocked());
//                    orderView.setTitle(bookEntity.getTitle());
//                    orderView.setAuthor(bookEntity.getAuthor());
//                    orderView.setDelayDays(delayDays);
//                    result.add(orderView);
//                }
//            }
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//            return Collections.emptyList();
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return result;
//    }

    //TODO объединить с getOrdersByLoginAndStatus()
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
                /**
                 * getting IDs of all books in received List
                 */
                Set<Integer> bookIds = listByStatus.stream().map(OrdersEntity::getBookId).collect(Collectors.toSet());
                /*for (OrdersEntity entity : listByStatus) {
                    bookIds.add(entity.getBookId());
                }*/

                /**
                 * getting all Logins from received List
                 */
                Set<String> allLogins = listByStatus.stream().map(OrdersEntity::getLogin).collect(Collectors.toSet());

                /**
                 * receiving a List of the Books by Set of IDs.
                 * creating a Map of IDs-Books, for easier way to get Book's properties by ID without DataBase queries
                 */
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                Map<Integer, BooksEntity> booksMap = new HashMap<>();
                for (BooksEntity book : booksEntities) {
                    booksMap.put(book.getBookId(), book);
                }

                /**
                 * receiving a List of the Users by Set of Logins.
                 * creating a Map of logins-Users, for easier way to get User's properties by Login without DataBase queries
                 */
                List<UsersEntity> usersEntities = userDao.getAllByCoupleLogins(allLogins);
                Map<String, UsersEntity> usersMap = new HashMap<>();
                for (UsersEntity user : usersEntities) {
                    usersMap.put(user.getLogin(), user);
                }

                /**
                 * building an OrderForMyOrdersList entity and passing it to the result List
                 */
                long oneDay = 24L * 60L * 60L * 1_000L;
                for (OrdersEntity entity : listByStatus) {
                    int delayDays = (int) ((currentDate.getTime() - entity.getEndDate().getTime())/oneDay);
                    int bookId = entity.getBookId();
                    BooksEntity book = booksMap.get(bookId);
                    String login = entity.getLogin();
                    UsersEntity user = usersMap.get(login);
                    FullOrdersList orderView = new FullOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                    orderView.setTitle(book.getTitle());
                    orderView.setAuthor(book.getAuthor());
                    orderView.setBlocked(user.getIsBlocked());
                    orderView.setDelayDays(delayDays);
                    result.add(orderView);
                }
                log.info("Get all books by couple ids (commit)");
                log.info("Get all users by couple logins (commit)");
            }

//            Set<OrdersEntity> listByStatus = null;
////            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
//            Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
//
//            for (OrdersEntity entity: listByStatus) {
//                long oneDay = 24L * 60L * 60L * 1_000L;
//                int delayDays = (int) ((currentDate.getTime() - entity.getEndDate().getTime())/oneDay);
//                if (delayDays > 0) {
//                    FullOrdersList orderView = new FullOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
//                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
//                    BooksEntity bookEntity = null;
//                    UsersEntity userEntity = null;
////                    BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
////                    UsersEntity userEntity = UserDaoOLD.getInstance().getByLogin(connection, entity.getLogin());
//                    orderView.setBlocked(userEntity.getIsBlocked());
//                    orderView.setTitle(bookEntity.getTitle());
//                    orderView.setAuthor(bookEntity.getAuthor());
//                    orderView.setDelayDays(delayDays);
//                    result.add(orderView);
//                }
//            }

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

    /**/
//    public void closeOrderOLD(HttpServletRequest request, String login, int orderID) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            OrdersEntity entity = null;
////            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
//            if ((login == null) ||
//                    (entity.getLogin().equals(login) && entity.getStatus().equals(OrderStatus.ORDERED))) {
////                OrderDaoOLD.getInstance().changeStatusOfOrder(connection, orderID, OrderStatus.CLOSED);
////                OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, new Date(Calendar.getInstance().getTime().getTime()));
//            }
//            // if User brought book to the Library, we mark Book as free
//            if (login == null && entity.getStatus().equals(OrderStatus.ON_HAND)) {
//                BookService.getInstance().setBookBusy(request, entity.getBookId(), false);
//            }
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    public void closeOrder(HttpServletRequest request, String login, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            OrdersEntity entity = orderDao.getById(orderID);
            log.info("Get order by id (commit)");
//            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);

            // if User brought book to the Library, we mark Book as free
            if (login == null && OrderStatus.ON_HAND.toString().equals(entity.getStatus())) {
                BooksEntity book = bookDao.getById(entity.getBookId());
                book.setIsBusy(false);
                bookDao.update(book);
                log.info("Get book by id (commit)");
                log.info("update book (commit)");
//                bookDao.setBookBusy(request, entity.getBookId(), false);
            }
            if ((login == null) ||
                    (entity.getLogin().equals(login) && OrderStatus.ORDERED.toString().equals(entity.getStatus()))) {
                entity.setStatus(OrderStatus.CLOSED.toString());
                entity.setEndDate(new Date(Calendar.getInstance().getTime().getTime()));
//                orderDao.saveOrUpdate(entity);
                orderDao.update(entity);
                log.info("update order (commit)");
//                orderDao.changeStatusOfOrder(orderID, OrderStatus.CLOSED);
//                orderDao.changeEndDateOfOrder(orderID, new Date(Calendar.getInstance().getTime().getTime()));
            }

            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**/
//    public void orderToHomeOrToRoomOLD(HttpServletRequest request, String login, int bookID, boolean isToHome) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            Set<OrdersEntity> orderEntityList = null;
////            Set<OrdersEntity> orderEntityList = OrderDaoOLD.getInstance().getAllByUser(connection, login);
//            Set<OrdersEntity> orderEntityList2 = new HashSet<>(orderEntityList);
//
//            Set<OrdersEntity> listByStatus = null;
////            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, OrderStatus.ORDERED.toString());
//            orderEntityList.retainAll(listByStatus);
//
////            listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, OrderStatus.ON_HAND.toString());
//            orderEntityList2.retainAll(listByStatus);
//            orderEntityList.addAll(orderEntityList2);
//
//            Set<OrdersEntity> listByBookId = null;
////            Set<OrdersEntity> listByBookId = OrderDaoOLD.getInstance().getAllByBookId(connection, bookID);
//            orderEntityList.retainAll(listByBookId);
//            if (orderEntityList.isEmpty()) {
//                long delay = 0L;
//                PlaceOfIssue place = PlaceOfIssue.READING_ROOM;
//                if (isToHome) {
//                    delay = 30L * 24L * 60L * 60L * 1_000L;
//                    place = PlaceOfIssue.HOME;
//                }
//                Date startDate = new Date(Calendar.getInstance().getTime().getTime());
//                Date endDate = new Date(startDate.getTime() + delay);
//                OrdersEntity newEntity = null;
////                OrdersEntity newEntity = OrderDaoOLD.getInstance().createNewEntity(login, bookID, OrderStatus.ORDERED, place,
////                        startDate, endDate);
////                OrderDaoOLD.getInstance().add(connection, newEntity);
//            } else {
//                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ на эту книгу имеется и активен");
//            }
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    public void orderToHomeOrToRoom(HttpServletRequest request, String login, int bookID, boolean isToHome) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

//            List<OrdersEntity> ordersListByUserAndNotClosedStatus = orderDao.getOrdersByLoginAndStatus(login, OrderStatus.ORDERED);
//            ordersListByUserAndNotClosedStatus.addAll(orderDao.getOrdersByLoginAndStatus(login, OrderStatus.ON_HAND));

            String[] statuses = {OrderStatus.ON_HAND.toString(), OrderStatus.ORDERED.toString()};
            List<OrdersEntity> ordersList = orderDao.getOrdersByLoginBookIdStatuses(login, bookID, statuses);
            OrdersEntity newEntity = null;
            if (ordersList.isEmpty()) {
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
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ на эту книгу имеется и активен");
            }

            transaction.commit();
            log.info("Get orders by login, bookId and statuses (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

    /**/
//    public void prolongOrderOLD(HttpServletRequest request, String login, int orderID) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            OrdersEntity entity = null;
////            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
//            if (entity.getLogin().equals(login) && entity.getStatus().equals(OrderStatus.ON_HAND)) {
//                // time interval from now till the end date of the order. In case not to allow a user indefinitely prolong his order
//                int interval = 5;
//                long gap = 30L * 24L * 60L * 60L * 1_000L;
//                long delay = interval * 24L * 60L * 60L * 1_000L;
//                Date endDate = null;
////                Date endDate = entity.getEndDate();
//                Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
//                long difference = endDate.getTime() - currentDate.getTime();
//                if (difference > 0 && (difference - delay) <= interval) {
////                    OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, new Date(endDate.getTime() + gap));
//                } else {
//                    request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ не должен быть просрочен, " +
//                            "и время до его окончания не должно превышать " + interval + " дней");
//                }
//            }
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    public void prolongOrder(HttpServletRequest request, String login, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            OrdersEntity entity = orderDao.getById(orderID);
            if (entity.getLogin().equals(login) && OrderStatus.ON_HAND.toString().equals(entity.getStatus())) {
                /**
                 * time interval from now till the end date of the order. In case not to allow a user indefinitely prolong his order
                 */
                int interval = 5;
                long gap = 30L * 24L * 60L * 60L * 1_000L;
                long delay = interval * 24L * 60L * 60L * 1_000L;
                Date endDate = entity.getEndDate();

                Date today = new Date();
                Date currentDate = new Date(today.getYear(), today.getMonth(), today.getDate());

                long difference = endDate.getTime() - currentDate.getTime();
                if (difference >= 0 && (difference - delay) <= interval) {
                    entity.setEndDate(new Date(endDate.getTime() + gap));
//                    orderDao.saveOrUpdate(entity);
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

//    public void provideBookOLD(HttpServletRequest request, int orderID) {
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
//            OrdersEntity entity = null;
////            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
//            if (BookService.getInstance().isBusy(request, entity.getBookId())) {
//                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Эта книга уже выдана!");
//                return;
//            }
//            if (entity.getStatus().equals(OrderStatus.ORDERED)) {
//                long delay = 0L;
//                if (PlaceOfIssue.HOME.equals(entity.getPlaceOfIssue())) {
//                    delay = 30L * 24L * 60L * 60L * 1_000L;
//                }
//                Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
//                Date newEndDate = new Date(currentDate.getTime() + delay);
////                OrderDaoOLD.getInstance().changeStatusOfOrder(connection, orderID, OrderStatus.ON_HAND);
////                OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, newEndDate);
//                BookService.getInstance().setBookBusy(request, entity.getBookId(), true);
//            } else {
//                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Проверьте статус заказа!");
//            }
//        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//    }

    public void provideBook(HttpServletRequest request, int orderID) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = util.getSession();
            transaction = currentSession.beginTransaction();

            OrdersEntity entity = orderDao.getById(orderID);
            BooksEntity bookEntity = null;
            if (entity != null) bookEntity = bookDao.getById(entity.getBookId());
            if (entity == null || bookEntity.getIsBusy()) {
                String message;
                if (entity == null) message = "Не удалось обратиться к книге";
                else message = "Эта книга уже выдана!";
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, message);
            } else {
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

            transaction.commit();
            log.info("Get order by id (commit)");
        } catch (DaoException e) {
            transaction.rollback();
            ExceptionsHandler.processException(request, e);
        } finally {
            util.releaseSession(currentSession);
        }
    }

//    public OrdersEntity getByIdOLD(HttpServletRequest request, int orderID) {
//        OrdersEntity answer = null;
//        Connection connection = null;
//        try {
//            connection = PoolManager.getInstance().getConnection();
////            answer = OrderDaoOLD.getInstance().getById(connection, orderID);
//        } catch (DaoException | SQLException | ClassNotFoundException e) {
//            ExceptionsHandler.processException(request, e);
//        } finally {
//            PoolManager.getInstance().releaseConnection(connection);
//        }
//        return answer;
//    }

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

    public void add(OrdersEntity entity) {}

    public List<OrdersEntity> getAll() {
        return null;
    }

    public OrdersEntity getByLogin(String login) {
        return null;
    }

    public void update(OrdersEntity entity) {}

    public void delete(int id) {}
}
