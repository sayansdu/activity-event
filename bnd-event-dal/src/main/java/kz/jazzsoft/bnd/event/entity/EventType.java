package kz.jazzsoft.bnd.event.entity;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 11:45
 * Copyright © LLP JazzSoft
 */

public class EventType {

	private Long id;
	
    private String name;

    private Long code;

    public Long getId(){
    	return id;
    }
    
    public void setId(long id){
    	this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getValue() {
        return getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
