package com.petko.entities2;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "orders"/*, schema = "library5", catalog = ""*/)
public class OrdersEntity extends Entity {
    private int oid;
    private String login;
    private int bid;
    private String status;
    private String placeofissue;
    private Date startdate;
    private Date enddate;

    @Id
    @Column(name = "oid", nullable = false)
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
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
    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 20)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "placeofissue", nullable = false, length = 20)
    public String getPlaceofissue() {
        return placeofissue;
    }

    public void setPlaceofissue(String placeofissue) {
        this.placeofissue = placeofissue;
    }

    //    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "startdate", nullable = false)
    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

//    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "enddate", nullable = false)
    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (oid != that.oid) return false;
        if (bid != that.bid) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (placeofissue != null ? !placeofissue.equals(that.placeofissue) : that.placeofissue != null) return false;
        if (startdate != null ? !startdate.equals(that.startdate) : that.startdate != null) return false;
        if (enddate != null ? !enddate.equals(that.enddate) : that.enddate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = oid;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + bid;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (placeofissue != null ? placeofissue.hashCode() : 0);
        result = 31 * result + (startdate != null ? startdate.hashCode() : 0);
        result = 31 * result + (enddate != null ? enddate.hashCode() : 0);
        return result;
    }
}
