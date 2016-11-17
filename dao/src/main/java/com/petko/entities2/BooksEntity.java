package com.petko.entities2;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "books"/*, schema = "library5", catalog = ""*/)
public class BooksEntity extends Entity {
    private int bid;
    private String title;
    private String author;
    private Boolean isbusy;

    @Id
    @Column(name = "bid", nullable = false)
    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "author", nullable = true, length = 20)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isbusy", nullable = true)
    public Boolean getIsbusy() {
        return isbusy;
    }

    public void setIsbusy(Boolean isbusy) {
        this.isbusy = isbusy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooksEntity that = (BooksEntity) o;

        if (bid != that.bid) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (isbusy != null ? !isbusy.equals(that.isbusy) : that.isbusy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bid;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (isbusy != null ? isbusy.hashCode() : 0);
        return result;
    }
}
