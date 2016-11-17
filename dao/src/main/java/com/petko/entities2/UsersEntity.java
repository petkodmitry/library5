package com.petko.entities2;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Table;

@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "users"/*, schema = "library5", catalog = ""*/)
public class UsersEntity extends Entity {
    private int uid;
    private String fname;
    private String lname;
    private String login;
    private String psw;
    private Boolean isadmin;
    private Boolean isblocked;

    @Id
    @Column(name = "uid", nullable = false)
    public int getUserId() {
        return uid;
    }

    public void setUserId(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "fname", nullable = true, length = 15)
    public String getFirstName() {
        return fname;
    }

    public void setFirstName(String fname) {
        this.fname = fname;
    }

    @Basic
    @Column(name = "lname", nullable = true, length = 20)
    public String getLastName() {
        return lname;
    }

    public void setLastName(String lname) {
        this.lname = lname;
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
    @Column(name = "psw", nullable = false, length = 20)
    public String getPassword() {
        return psw;
    }

    public void setPassword(String psw) {
        this.psw = psw;
    }

    @Basic
//    @Type(type = "yes_no")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isadmin", nullable = true)
    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    @Basic
//    @Type(type = "yes_no")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isblocked", nullable = true)
    public Boolean getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(Boolean isblocked) {
        this.isblocked = isblocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (uid != that.uid) return false;
        if (fname != null ? !fname.equals(that.fname) : that.fname != null) return false;
        if (lname != null ? !lname.equals(that.lname) : that.lname != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (psw != null ? !psw.equals(that.psw) : that.psw != null) return false;
        if (isadmin != null ? !isadmin.equals(that.isadmin) : that.isadmin != null) return false;
        if (isblocked != null ? !isblocked.equals(that.isblocked) : that.isblocked != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (fname != null ? fname.hashCode() : 0);
        result = 31 * result + (lname != null ? lname.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (psw != null ? psw.hashCode() : 0);
        result = 31 * result + (isadmin != null ? isadmin.hashCode() : 0);
        result = 31 * result + (isblocked != null ? isblocked.hashCode() : 0);
        return result;
    }
}
