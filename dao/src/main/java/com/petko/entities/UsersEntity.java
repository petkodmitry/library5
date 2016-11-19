package com.petko.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
public class UsersEntity extends Entity {
    private int userId;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Boolean isAdmin;
    private Boolean isBlocked;
    private Set<SeminarsEntity> seminars = new HashSet<>();

    @Id
    @Column(name = "uid", nullable = false, unique = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int uid) {
        this.userId = uid;
    }

    @Basic
    @Column(name = "fname", nullable = true, length = 15)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fname) {
        this.firstName = fname;
    }

    @Basic
    @Column(name = "lname", nullable = true, length = 20)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lname) {
        this.lastName = lname;
    }

    @Basic
    @Column(name = "login", nullable = false, length = 20, unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "psw", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String psw) {
        this.password = psw;
    }

    @Basic
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isadmin", nullable = true)
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isadmin) {
        this.isAdmin = isadmin;
    }

    @Basic
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isblocked", nullable = true)
    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isblocked) {
        this.isBlocked = isblocked;
    }

    @ManyToMany/*(targetEntity = SeminarsEntity.class)*/
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "users_seminars"
            , joinColumns = @JoinColumn(name = "uid2")
            , inverseJoinColumns = @JoinColumn(name = "sid2")
    )
    public Set<SeminarsEntity> getSeminars() {
        return seminars;
    }

    public void setSeminars(Set<SeminarsEntity> seminars) {
        this.seminars = seminars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (userId != that.userId) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (isAdmin != null ? !isAdmin.equals(that.isAdmin) : that.isAdmin != null) return false;
        if (isBlocked != null ? !isBlocked.equals(that.isBlocked) : that.isBlocked != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (isAdmin != null ? isAdmin.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String allSeminars = "";
        for (SeminarsEntity meet : seminars) {
            allSeminars = allSeminars.concat(meet + ", ");
        }
        return String.format("User [userId=%d, firstName=%s, lastName=%s, login=%s, password=%s, isAdmin=%b, isBlocked=%b, seminars=%s]",
                userId, firstName, lastName, login, password, isAdmin, isBlocked, allSeminars);
    }
}
