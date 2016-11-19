package com.petko.entitiesOLD;

import com.petko.entities.OrderStatus;
import com.petko.entities.PlaceOfIssue;

import java.sql.Date;

public class OrderEntityOLD extends EntityOLD {
    private int orderId;
    private String login;
    private int bookId;
    private OrderStatus status;
    private PlaceOfIssue placeOfIssue;
    private Date startDate;
    private Date endDate;

    @Override
    public String toString() {
        return String.format("Order [orderId=%d, login=%s, bookId=%d, status=%s, startDate=%s, endDate=%s]",
                orderId, login, bookId, status, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntityOLD)) return false;

        OrderEntityOLD entity = (OrderEntityOLD) o;

        if (getOrderId() != entity.getOrderId()) return false;
        if (getBookId() != entity.getBookId()) return false;
        if (getLogin() != null ? !getLogin().equals(entity.getLogin()) : entity.getLogin() != null) return false;
        if (getStatus() != entity.getStatus()) return false;
        if (getPlaceOfIssue() != entity.getPlaceOfIssue()) return false;
        if (getStartDate() != null ? !getStartDate().equals(entity.getStartDate()) : entity.getStartDate() != null)
            return false;
        return getEndDate() != null ? getEndDate().equals(entity.getEndDate()) : entity.getEndDate() == null;

    }

    @Override
    public int hashCode() {
        int result = getOrderId();
        result = 31 * result + (getLogin() != null ? getLogin().hashCode() : 0);
        result = 31 * result + getBookId();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getPlaceOfIssue() != null ? getPlaceOfIssue().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        return result;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PlaceOfIssue getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(PlaceOfIssue placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
