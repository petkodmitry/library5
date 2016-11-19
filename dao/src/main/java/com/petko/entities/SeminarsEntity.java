package com.petko.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
//import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "seminars")
public class SeminarsEntity extends Entity {
    private int seminarId;
    private String subject;
    private Date seminarDate;
    private Set<UsersEntity> users = new HashSet<>();

    @Id
    @Column(name = "sid", nullable = false, unique = true)
    public int getSeminarId() {
        return seminarId;
    }

    public void setSeminarId(int seminarId) {
        this.seminarId = seminarId;
    }

    @Basic
    @Column(name = "subject", length = 50)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "sdate", nullable = false)
    public Date getSeminarDate() {
        return seminarDate;
    }

    public void setSeminarDate(Date startdate) {
        this.seminarDate = startdate;
    }

    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "users_seminars"
            , joinColumns = @JoinColumn(name = "sid2")
            , inverseJoinColumns = @JoinColumn(name = "uid2")
    )
    public Set<UsersEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UsersEntity> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeminarsEntity that = (SeminarsEntity) o;

        if (seminarId != that.seminarId) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (seminarDate != null ? !seminarDate.equals(that.seminarDate) : that.seminarDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = seminarId;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (seminarDate != null ? seminarDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String allUsers = "";
        for (UsersEntity empl : users) {
            allUsers = allUsers.concat(empl.getUserId() + ", ");
        }
        return "SeminarsEntity{" +
                "seminarId=" + seminarId +
                ", subject='" + subject + '\'' +
                ", sDate='" + seminarDate + '\'' +
                ", users='" + allUsers + '\'' +
                '}';
    }
}
