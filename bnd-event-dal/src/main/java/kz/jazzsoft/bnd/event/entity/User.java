package kz.jazzsoft.bnd.event.entity;

import kz.jazzsoft.bnd.core.entity.ABaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 28.01.13 10:39
 * Copyright © LLP JazzSoft
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bnd_users")
public class User extends ABaseEntity {
    @Column(name = "login")
    private String login;
    @Column(name = "user_id")
    private Long userId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return getLogin();
    }
}
