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

    /**
     * @return seminarId
     */
    @Id
    @Column(name = "sid", nullable = false, unique = true)
    public int getSeminarId() {
        return seminarId;
    }

    /**
     * Sets seminarId
     * @param seminarId - seminarId
     */
    public void setSeminarId(int seminarId) {
        this.seminarId = seminarId;
    }

    /**
     * @return subject
     */
    @Basic
    @Column(name = "subject", length = 50)
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject
     * @param subject - subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return seminarDate
     */
    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "sdate", nullable = false)
    public Date getSeminarDate() {
        return seminarDate;
    }

    /**
     * Sets seminarDate
     * @param startdate - seminarDate
     */
    public void setSeminarDate(Date startdate) {
        this.seminarDate = startdate;
    }

    /**
     * @return users
     */
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "users_seminars"
            , joinColumns = @JoinColumn(name = "sid2")
            , inverseJoinColumns = @JoinColumn(name = "uid2")
    )
    public Set<UsersEntity> getUsers() {
        return users;
    }

    /**
     * Sets users
     * @param users - users
     */
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
