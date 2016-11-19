package com.petko.services;

import com.petko.DaoException;
import com.petko.ExceptionsHandler;
import com.petko.constants.Constants;
import com.petko.dao.BookDao;
import com.petko.dao.OrderDao;
import com.petko.entities.*;
import com.petko.managers.PoolManager;
import com.petko.utils.HibernateUtilLibrary;
import com.petko.vo.FullOrdersList;
import com.petko.vo.OrderForMyOrdersList;
import com.petko.vo.AnyStatusOrdersList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class OrderService implements Service<OrdersEntity>{
    private static OrderService instance;
    private static Logger log = Logger.getLogger(OrderService.class);
    private static OrderDao orderDao = OrderDao.getInstance();
    private static BookDao bookDao = BookDao.getInstance();
    private static HibernateUtilLibrary util = HibernateUtilLibrary.getHibernateUtil();

    private OrderService() {}

    public static synchronized OrderService getInstance() {
        if(instance == null){
            instance = new OrderService();
        }
        return instance;
    }

    /*public List<OrderForMyOrdersList> getOrdersByLoginAndStatusOLD(HttpServletRequest request, String login, OrderStatus orderStatus) {
        List<OrderForMyOrdersList> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            Set<OrdersEntity> orderEntityList = null;
            Set<OrdersEntity> listByStatus = null;
//            Set<OrdersEntity> orderEntityList = OrderDaoOLD.getInstance().getAllByUser(connection, login);
//            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
            orderEntityList.retainAll(listByStatus);

            for (OrdersEntity entity: orderEntityList) {
                OrderForMyOrdersList orderView = new OrderForMyOrdersList(entity.getOrderId(), entity.getBookId(),
                        entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                BooksEntity bookEntity = null;
//                BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
                orderView.setTitle(bookEntity.getTitle());
                orderView.setAuthor(bookEntity.getAuthor());
                result.add(orderView);
            }
        } catch (*//*DaoException |*//* SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }*/

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

            List<OrdersEntity> orderEntityList = orderDao.getOrdersByLoginAndStatus(login, orderStatus);

            if (!orderEntityList.isEmpty()) {
                Set<Integer> bookIds = new HashSet<>();
                for (OrdersEntity entity : orderEntityList) {
                    bookIds.add(entity.getBookId());
                }
                List<BooksEntity> booksEntities = bookDao.getAllByCoupleIds(bookIds);
                Map<Integer, BooksEntity> booksMap = new HashMap<>();
                for (BooksEntity book : booksEntities) {
                    booksMap.put(book.getBookId(), book);
                }

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

    public List<AnyStatusOrdersList> getOrdersByStatus(HttpServletRequest request, OrderStatus orderStatus) {
        List<AnyStatusOrdersList> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            Set<OrdersEntity> listByStatus = null;
//            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());

            for (OrdersEntity entity: listByStatus) {
                AnyStatusOrdersList orderView = new AnyStatusOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
                        entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                BooksEntity bookEntity = null;
//                BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
                orderView.setTitle(bookEntity.getTitle());
                orderView.setAuthor(bookEntity.getAuthor());
                result.add(orderView);
            }
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }

    public List<FullOrdersList> getExpiredOrders(HttpServletRequest request) {
        List<FullOrdersList> result = new ArrayList<>();
        OrderStatus orderStatus = OrderStatus.ON_HAND;
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            Set<OrdersEntity> listByStatus = null;
//            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, orderStatus.toString());
            Date currentDate = new Date(Calendar.getInstance().getTime().getTime());

            for (OrdersEntity entity: listByStatus) {
                long oneDay = 24L * 60L * 60L * 1_000L;
                int delayDays = (int) ((currentDate.getTime() - entity.getEndDate().getTime())/oneDay);
                if (delayDays > 0) {
                    FullOrdersList orderView = new FullOrdersList(entity.getOrderId(), entity.getLogin(), entity.getBookId(),
                            entity.getPlaceOfIssue(), entity.getStartDate(), entity.getEndDate());
                    BooksEntity bookEntity = null;
                    UsersEntity userEntity = null;
//                    BooksEntity bookEntity = BookDaoOLD.getInstance().getById(connection, entity.getBookId());
//                    UsersEntity userEntity = UserDaoOLD.getInstance().getByLogin(connection, entity.getLogin());
                    orderView.setBlocked(userEntity.getIsBlocked());
                    orderView.setTitle(bookEntity.getTitle());
                    orderView.setAuthor(bookEntity.getAuthor());
                    orderView.setDelayDays(delayDays);
                    result.add(orderView);
                }
            }
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return result;
    }

    /*public void closeOrderOLD(HttpServletRequest request, String login, int orderID) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            OrdersEntity entity = null;
//            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
            if ((login == null) ||
                    (entity.getLogin().equals(login) && entity.getStatus().equals(OrderStatus.ORDERED))) {
//                OrderDaoOLD.getInstance().changeStatusOfOrder(connection, orderID, OrderStatus.CLOSED);
//                OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, new Date(Calendar.getInstance().getTime().getTime()));
            }
            // if User brought book to the Library, we mark Book as free
            if (login == null && entity.getStatus().equals(OrderStatus.ON_HAND)) {
                BookService.getInstance().setBookBusy(request, entity.getBookId(), false);
            }
        } catch (*//*DaoException |*//* SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }*/

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
                bookDao.saveOrUpdate(book);
                log.info("Get book by id (commit)");
                log.info("saveOrUpdate book (commit)");
//                bookDao.setBookBusy(request, entity.getBookId(), false);
            }
            if ((login == null) ||
                    (entity.getLogin().equals(login) && OrderStatus.ORDERED.toString().equals(entity.getStatus()))) {
                entity.setStatus(OrderStatus.CLOSED.toString());
                entity.setEndDate(new Date(Calendar.getInstance().getTime().getTime()));
                orderDao.saveOrUpdate(entity);
                log.info("saveOrUpdate order (commit)");
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

    public void orderToHomeOrToRoom(HttpServletRequest request, String login, int bookID, boolean isToHome) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            Set<OrdersEntity> orderEntityList = null;
//            Set<OrdersEntity> orderEntityList = OrderDaoOLD.getInstance().getAllByUser(connection, login);
            Set<OrdersEntity> orderEntityList2 = new HashSet<>(orderEntityList);

            Set<OrdersEntity> listByStatus = null;
//            Set<OrdersEntity> listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, OrderStatus.ORDERED.toString());
            orderEntityList.retainAll(listByStatus);

//            listByStatus = OrderDaoOLD.getInstance().getAllByStatus(connection, OrderStatus.ON_HAND.toString());
            orderEntityList2.retainAll(listByStatus);
            orderEntityList.addAll(orderEntityList2);

            Set<OrdersEntity> listByBookId = null;
//            Set<OrdersEntity> listByBookId = OrderDaoOLD.getInstance().getAllByBookId(connection, bookID);
            orderEntityList.retainAll(listByBookId);
            if (orderEntityList.isEmpty()) {
                long delay = 0L;
                PlaceOfIssue place = PlaceOfIssue.READING_ROOM;
                if (isToHome) {
                    delay = 30L * 24L * 60L * 60L * 1_000L;
                    place = PlaceOfIssue.HOME;
                }
                Date startDate = new Date(Calendar.getInstance().getTime().getTime());
                Date endDate = new Date(startDate.getTime() + delay);
                OrdersEntity newEntity = null;
//                OrdersEntity newEntity = OrderDaoOLD.getInstance().createNewEntity(login, bookID, OrderStatus.ORDERED, place,
//                        startDate, endDate);
//                OrderDaoOLD.getInstance().add(connection, newEntity);
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ на эту книгу имеется и активен");
            }
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public void prolongOrder(HttpServletRequest request, String login, int orderID) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            OrdersEntity entity = null;
//            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
            if (entity.getLogin().equals(login) && entity.getStatus().equals(OrderStatus.ON_HAND)) {
                // time interval from now till the end date of the order. In case not to allow a user indefinitely prolong his order
                int interval = 5;
                long gap = 30L * 24L * 60L * 60L * 1_000L;
                long delay = interval * 24L * 60L * 60L * 1_000L;
                Date endDate = null;
//                Date endDate = entity.getEndDate();
                Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
                long difference = endDate.getTime() - currentDate.getTime();
                if (difference > 0 && (difference - delay) <= interval) {
//                    OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, new Date(endDate.getTime() + gap));
                } else {
                    request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Заказ не должен быть просрочен, " +
                            "и время до его окончания не должно превышать " + interval + " дней");
                }
            }
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public void provideBook(HttpServletRequest request, int orderID) {
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
            OrdersEntity entity = null;
//            OrdersEntity entity = OrderDaoOLD.getInstance().getById(connection, orderID);
            if (BookService.getInstance().isBusy(request, entity.getBookId())) {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Эта книга уже выдана!");
                return;
            }
            if (entity.getStatus().equals(OrderStatus.ORDERED)) {
                long delay = 0L;
                if (PlaceOfIssue.HOME.equals(entity.getPlaceOfIssue())) {
                    delay = 30L * 24L * 60L * 60L * 1_000L;
                }
                Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
                Date newEndDate = new Date(currentDate.getTime() + delay);
//                OrderDaoOLD.getInstance().changeStatusOfOrder(connection, orderID, OrderStatus.ON_HAND);
//                OrderDaoOLD.getInstance().changeEndDateOfOrder(connection, orderID, newEndDate);
                BookService.getInstance().setBookBusy(request, entity.getBookId(), true);
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE_ATTRIBUTE, "Проверьте статус заказа!");
            }
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
    }

    public OrdersEntity getById(HttpServletRequest request, int orderID) {
        OrdersEntity answer = null;
        Connection connection = null;
        try {
            connection = PoolManager.getInstance().getConnection();
//            answer = OrderDaoOLD.getInstance().getById(connection, orderID);
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
        }
        return answer;
    }

    public void add(OrdersEntity entity) {

    }

    public List<OrdersEntity> getAll() {
        return null;
    }

    public OrdersEntity getByLogin(String login) {
        return null;
    }

    public void update(OrdersEntity entity) {}

    public void delete(int id) {}
}
