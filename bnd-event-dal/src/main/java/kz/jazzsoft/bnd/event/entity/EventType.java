package kz.jazzsoft.bnd.event.entity;

import kz.jazzsoft.bnd.core.entity.ABaseEntity;
import kz.jazzsoft.bnd.core.entity.IListableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 11:45
 * Copyright © LLP JazzSoft
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bnd_event_type")
public class EventType extends ABaseEntity implements IListableEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private Long code;

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

    @Override
    public String getValue() {
        return getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
