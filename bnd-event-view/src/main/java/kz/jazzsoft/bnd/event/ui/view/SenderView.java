package kz.jazzsoft.bnd.event.ui.view;

import com.vaadin.ui.*;
import kz.jazzsoft.bnd.core.entity.IListableEntity;
import kz.jazzsoft.bnd.core.ui.element.dialog.component.JComponent;
import kz.jazzsoft.bnd.core.ui.view.LocalizationView;
import kz.jazzsoft.bnd.core.util.Util;
import kz.jazzsoft.bnd.event.dal.Dal;
import kz.jazzsoft.bnd.event.dto.User;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Date;
import java.util.List;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 18.01.13 9:43
 * Copyright © LLP JazzSoft
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SenderView extends LocalizationView {
    @Autowired
    Dal dao;

    @Autowired
    Util util;

    JComponent eventType, componentName, moduleName, eventDescription, user, dateTime, resource;
    JComponent userLogin, userId;
    Button sender, sendUser;

    @Override
    public void update() {
        addLanguage("kz", setLocaleValue("language.kz"));
        addLanguage("ru", setLocaleValue("language.ru"));
        addLanguage("en", setLocaleValue("language.en"));

        eventType.setCaption(setLocaleValue("event.eventType"));
        componentName.setCaption(setLocaleValue("event.componentName"));
        moduleName.setCaption(setLocaleValue("event.moduleName"));
        eventDescription.setCaption(setLocaleValue("event.eventDescription"));
        user.setCaption(setLocaleValue("event.user"));
        dateTime.setCaption(setLocaleValue("event.dateTime"));
        resource.setCaption(setLocaleValue("event.resource"));

        sender.setCaption(setLocaleValue("sender.send"));

    }

    /**
     * Вьюха для добавления пользователей и событий.
     * Все сохраняется через сервисы
     * TODO как будет передаваться id пользователя, данную вьюху можно будет удалить
     */
    @Override
    public void init() {

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

        Select select = createLocalizationSelect();
        this.addComponent(select);
        this.setComponentAlignment(select, Alignment.MIDDLE_RIGHT);

        sender = new Button();
        sender.setWidth("200px");

        Panel panel = new Panel();
        panel.addComponent(eventType);
        panel.addComponent(componentName);
        panel.addComponent(moduleName);
        panel.addComponent(eventDescription);
        panel.addComponent(user);
        panel.addComponent(dateTime);
        panel.addComponent(resource);

        this.addComponent(panel);
        this.addComponent(sender);
        this.setComponentAlignment(sender, Alignment.MIDDLE_CENTER);
        userLogin = new JComponent(new TextField(), JComponent.Position.LEFT);
        userId = new JComponent(new TextField(), JComponent.Position.LEFT);

        userLogin.setCaption("Логин");
        userId.setCaption("Идентификатор пользователя");

        userLogin.setWidth("200px");
        userId.setWidth("200px");

        Panel panel1 = new Panel();
        panel1.addComponent(userId);
        panel1.addComponent(userLogin);

        sendUser = new Button("Сохраняем пользователя");
        sendUser.setWidth("200px");

        this.addComponent(panel1);
        this.addComponent(sendUser);
        this.setComponentAlignment(sendUser, Alignment.MIDDLE_CENTER);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onShow() {
        /**
         * Заполняю селект типами событий
         */
        List<?> types = dao.getEventType();
        eventType.loadDic((List<IListableEntity>) types);

        /**
         * Сохраняю событие
         */
        sender.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Util util = new Util();
                kz.jazzsoft.bnd.event.dto.Event event = new kz.jazzsoft.bnd.event.dto.Event();

                event.setEventType(400L);
                event.setComponentName(componentName.getValue().toString());
                event.setModuleName(moduleName.getValue().toString());
                event.setDescription(eventDescription.getValue().toString());
                event.setUser(Long.valueOf(user.getValue().toString()));
                event.setResource(resource.getValue().toString());
                event.setDateTime(new Date());
                try {
                    send(util.toJSON(event));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                getApplication().getMainWindow().showNotification("Добавили событие!!!!");
            }
        });

        /**
         * Сохраняю пользователя
         */
        sendUser.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Util util = new Util();
                User userDTO = new User();
                userDTO.setUserId(Long.parseLong(userId.getValue().toString()));
                userDTO.setLogin(userLogin.getValue().toString());
                try {
                    sendUser(util.toJSON(userDTO));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                getApplication().getMainWindow().showNotification("Добавили пользователя!!!!");
            }
        });
    }

    /**
     * Отправляю данные события на сервис
     *
     * @param data - строка с данными
     * @throws Exception
     */
    public void send(String data) throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("http://localhost:8082/event/rest/sendData");
            }
        };

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(routeBuilder);
        context.start();
        ProducerTemplate pt = context.createProducerTemplate();
        pt.sendBody("direct:start", data);

    }

    /**
     * Отправляю данные пользователя на сервис
     *
     * @param data - строка с данными
     * @throws Exception
     */
    public void sendUser(String data) throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("http://localhost:8082/event/rest/sendUserData");
            }
        };

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(routeBuilder);
        context.start();
        ProducerTemplate pt = context.createProducerTemplate();
        pt.sendBody("direct:start", data);
    }
}
