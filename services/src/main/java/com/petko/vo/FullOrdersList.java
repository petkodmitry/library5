package com.petko.vo;

import java.util.Date;

public class FullOrdersList {
    private int orderId;
    private String login;
    private boolean isBlocked;
    private int bookId;
    private String title;
    private String author;
//    private PlaceOfIssue place;
    private String place;
    private Date startDate;
    private Date endDate;
    private int delayDays;

    public FullOrdersList(int orderId, String login, int bookId, /*PlaceOfIssue*/String place, Date startDate, Date endDate) {
        this.orderId = orderId;
        this.login = login;
        this.bookId = bookId;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDelayDays(int delayDays) {
        this.delayDays = delayDays;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getLogin() {
        return login;
    }

    public boolean isBlocked() {
        return isBlocked;
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

    public String getPlace() {
        return place;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getDelayDays() {
        return delayDays;
    }
}
