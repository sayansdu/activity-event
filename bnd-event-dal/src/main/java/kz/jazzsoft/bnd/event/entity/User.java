package kz.jazzsoft.bnd.event.entity;


/**
 * Created by: dmitriy.lemeshenko
 * Created: 28.01.13 10:39
 * Copyright © LLP JazzSoft
 */

public class User {

	private Long id;
    
    private String login;

    private Long userid;

    public Long getId(){
    	return id;
    }
    
    public void setId(long id){
    	this.id = id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getUserId() {
        return userid;
    }

    public void setUserId(Long userId) {
        this.userid = userId;
    }

    @Override
    public String toString() {
        return getLogin();
    }
}
