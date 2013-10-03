package kz.jazzsoft.bnd.event.entity;

import kz.jazzsoft.bnd.core.entity.ABaseEntity;

import javax.persistence.*;
import java.util.Date;
//import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 12:23
 * Copyright © LLP JazzSoft
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bnd_event")
public class Event extends ABaseEntity {
    @ManyToOne
    @JoinColumn(name = "ref_event_type")
    private EventType eventType;
    @Column(name = "component_name")
    private String componentName;
    @Column(name = "module_name")
    private String moduleName;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "ref_user")
    private User user;
    @Column(name = "date_time")
    private Date dateTime;
    @Column(name = "resource")
    private String resource;

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
