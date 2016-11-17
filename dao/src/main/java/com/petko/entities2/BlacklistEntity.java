package com.petko.entities2;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "blacklist"/*, schema = "library5", catalog = ""*/)
public class BlacklistEntity extends Entity {
    private int lid;
    private String login;
    private Boolean isblocked;

    @Id
    @Column(name = "lid", nullable = false)
    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
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
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isblocked", nullable = false)
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

        BlacklistEntity that = (BlacklistEntity) o;

        if (lid != that.lid) return false;
        if (isblocked != that.isblocked) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lid;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (isblocked != null ? isblocked.hashCode() : 0);
        return result;
    }
}
