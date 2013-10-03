package kz.jazzsoft.bnd.event.dto;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 23.01.13 12:29
 * Copyright © LLP JazzSoft
 */
public class User {
    private String login;
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
}
