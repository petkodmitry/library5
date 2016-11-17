package com.petko.vo;

import com.petko.entities.PlaceOfIssue;

import java.util.Date;

public class AnyStatusOrdersList {
    private int orderId;
    private String login;
    private int bookId;
    private String title;
    private String author;
    private PlaceOfIssue place;
    private Date startDate;
    private Date endDate;

    public AnyStatusOrdersList(int orderId, String login, int bookId, PlaceOfIssue place, Date startDate, Date endDate) {
        this.orderId = orderId;
        this.login = login;
        this.bookId = bookId;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getLogin() {
        return login;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public PlaceOfIssue getPlace() {
        return place;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
