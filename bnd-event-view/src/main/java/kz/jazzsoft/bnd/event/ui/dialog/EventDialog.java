package kz.jazzsoft.bnd.event.ui.dialog;

import com.vaadin.ui.*;
import kz.jazzsoft.bnd.core.entity.IListableEntity;
import kz.jazzsoft.bnd.core.ui.element.dialog.component.ABaseDialog;
import kz.jazzsoft.bnd.core.ui.element.dialog.component.JComponent;
import kz.jazzsoft.bnd.event.adapter.EventAdapter;
import kz.jazzsoft.bnd.event.dal.Dal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 14:33
 * Copyright © LLP JazzSoft
 */
@SuppressWarnings("serial")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventDialog extends ABaseDialog {

    JComponent eventType, componentName, moduleName, eventDescription, user, dateTime, resource;

    @Autowired
    Dal dao;

    @Autowired
    EventAdapter adapter;

    /**
     * Диалог для сохранения/редактирования событий
     * TODO как будет передаваться id пользователя, диалог можно будет удалить
     */
    protected EventDialog() {
        super(kz.jazzsoft.bnd.event.entity.Event.class);
        this.setCaption("Событие");
        this.setWidth(400, UNITS_PIXELS);
        this.setHeight(550, UNITS_PIXELS);
        this.setModal(true);
        this.setResizable(false);
    }

    @Override
    public Panel init() {
        Panel panel = new Panel();

        eventType = new JComponent(new Select(), JComponent.Position.LEFT);
        componentName = new JComponent(new TextField(), JComponent.Position.LEFT);
        moduleName = new JComponent(new TextField(), JComponent.Position.LEFT);
        eventDescription = new JComponent(new TextArea(), JComponent.Position.LEFT);
        user = new JComponent(new TextField(), JComponent.Position.LEFT);
        dateTime = new JComponent(new DateField(), JComponent.Position.LEFT);
        resource = new JComponent(new TextField(), JComponent.Position.LEFT);

        eventType.setWidth("200px");
        componentName.setWidth("200px");
        moduleName.setWidth("200px");
        eventDescription.setWidth("200px");
        user.setWidth("200px");
        dateTime.setWidth("200px");
        resource.setWidth("200px");

        eventType.setDebugId("eventType.id");
        componentName.setDebugId("componentName");
        moduleName.setDebugId("moduleName");
        eventDescription.setDebugId("description");
        user.setDebugId("user.id");
        dateTime.setDebugId("dateTime");
        resource.setDebugId("resource");

        panel.addComponent(eventType);
        panel.addComponent(componentName);
        panel.addComponent(moduleName);
        panel.addComponent(eventDescription);
        panel.addComponent(user);
        panel.addComponent(dateTime);
        panel.addComponent(resource);
        panel.addComponent(new Button("Жмем", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                adapter.sendEvent(kz.jazzsoft.bnd.event.adapter.EventAdapter.Type.ERROR, 1L, "Описание", "Модуль Журналирование", "Компонент Кнопка");
            }
        }));
        return panel;
    }

    @Override
    public void onShow() {
        List<?> types = dao.getEventType();
        eventType.loadDic((List<IListableEntity>) types);
    }

    @Override
    public void update() {
        eventType.setValidation(setLocaleValue("validation.required"));

        componentName.setValidation(setLocaleValue("validation.required"));
        componentName.setValidation(2, setLocaleValue("validation.length"));

        moduleName.setValidation(setLocaleValue("validation.required"));
        moduleName.setValidation(2, 5, setLocaleValue("validation.length"));
//        moduleName.setValidation(JComponent.JValidation.NUMBERS, setLocaleValue("validation.numbers"));

        eventDescription.setValidation(setLocaleValue("validation.required"));
 //       eventDescription.setValidation(JComponent.JValidation.EMAIL, setLocaleValue("validation.email"));

//        user.setValidation(setLocaleValue("validation.required"));

        dateTime.setValidation(setLocaleValue("validation.required"));

        resource.setValidation(setLocaleValue("validation.required"));
        resource.setValidation("[1-9][0-9]{4}", setLocaleValue("validation.error"));


        eventType.setCaption(setLocaleValue("event.eventType"));
        componentName.setCaption(setLocaleValue("event.componentName"));
        moduleName.setCaption(setLocaleValue("event.moduleName"));
        eventDescription.setCaption(setLocaleValue("event.eventDescription"));
        user.setCaption(setLocaleValue("event.user"));
        dateTime.setCaption(setLocaleValue("event.dateTime"));
        resource.setCaption(setLocaleValue("event.resource"));
    }
}
