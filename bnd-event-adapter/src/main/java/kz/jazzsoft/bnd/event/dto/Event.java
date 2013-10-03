package kz.jazzsoft.bnd.event.dto;

import java.util.Date;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 23.01.13 10:39
 * Copyright © LLP JazzSoft
 */
public class Event {
    private Long eventType;
    private String componentName;
    private String moduleName;
    private String description;
    private Long user;
    private Date dateTime;
    private String resource;
    
    public Long getEventType() {
        return eventType;
    }

    public void setEventType(Long eventType) {
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

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
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

	@Override
	public String toString() {
		return "Event [eventType=" + eventType
				+ ", componentName=" + componentName + ", moduleName="
				+ moduleName + ", description=" + description + ", user="
				+ user + ", dateTime=" + dateTime + ", resource=" + resource
				+ "]";
	}


}
