package com.petko.services;

import com.petko.ExceptionsHandler;
import com.petko.constants.Constants;
import com.petko.entities.*;
import com.petko.managers.PoolManager;
import com.petko.vo.FullOrdersList;
import com.petko.vo.OrderForMyOrdersList;
import com.petko.vo.AnyStatusOrdersList;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class OrderService implements Service<OrdersEntity>{
    private static OrderService instance;

    private OrderService() {}

    public static synchronized OrderService getInstance() {
        if(instance == null){
            instance = new OrderService();
        }
        return instance;
    }

    public List<OrderForMyOrdersList> getOrdersByLoginAndStatus(HttpServletRequest request, String login, OrderStatus orderStatus) {
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
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
            return Collections.emptyList();
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
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

    public void closeOrder(HttpServletRequest request, String login, int orderID) {
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
        } catch (/*DaoException |*/ SQLException | ClassNotFoundException e) {
            ExceptionsHandler.processException(request, e);
        } finally {
            PoolManager.getInstance().releaseConnection(connection);
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
