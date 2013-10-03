package kz.jazzsoft.bnd.event.entity;

import java.util.Date;
//import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 12:23
 * Copyright © LLP JazzSoft
 */

public class Event {

	private Long id;
	
    private EventType eventType;

    private String componentName;

    private String moduleName;

    private String description;

    private User user;

    private Date dateTime;

    private String resource;

    public Long getId(){
    	return id;
    }
    
    public void setId(long id){
    	this.id = id;
    }
    
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
