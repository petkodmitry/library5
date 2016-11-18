package com.petko.entities2;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "orders")
public class OrdersEntity extends Entity {
    private int orderId;
    private String login;
    private int bookId;
    private OrderStatus status;
    private PlaceOfIssue placeOfIssue;
    private Date startDate;
    private Date endDate;

    @Id
    @Column(name = "oid", nullable = false, unique = true)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int oid) {
        this.orderId = oid;
    }

    @Basic
    @Column(name = "login", nullable = false, length = 20)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "bid", nullable = false)
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bid) {
        this.bookId = bid;
    }

    @Basic
    @Type(type = "org.hibernate.type.StringType")
    @Column(name = "status", nullable = false, length = 20)
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Basic
    @Type(type = "org.hibernate.type.StringType")
    @Column(name = "placeofissue", nullable = false, length = 20)
    public PlaceOfIssue getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(PlaceOfIssue placeofissue) {
        this.placeOfIssue = placeofissue;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "startdate", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startdate) {
        this.startDate = startdate;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "enddate", nullable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date enddate) {
        this.endDate = enddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (orderId != that.orderId) return false;
        if (bookId != that.bookId) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (placeOfIssue != null ? !placeOfIssue.equals(that.placeOfIssue) : that.placeOfIssue != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + bookId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (placeOfIssue != null ? placeOfIssue.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Order [orderId=%d, login=%s, bookId=%d, status=%s, startDate=%s, endDate=%s]",
                orderId, login, bookId, status, startDate, endDate);
    }
}
