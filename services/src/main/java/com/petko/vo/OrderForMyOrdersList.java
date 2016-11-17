package com.petko.vo;

import com.petko.entities.PlaceOfIssue;

import java.util.Date;

public class OrderForMyOrdersList {
    private int orderId;
    private int bookId;
    private String title;
    private String author;
    private PlaceOfIssue place;
    private Date startDate;
    private Date endDate;

    public OrderForMyOrdersList(int orderId, int bookId, PlaceOfIssue place, Date startDate, Date endDate) {
        this.orderId = orderId;
        this.bookId = bookId;
//        this.title = title;
//        this.author = author;
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
