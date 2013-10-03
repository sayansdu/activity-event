package kz.jazzsoft.bnd.event.service;

import kz.jazzsoft.bnd.core.util.Util;
import kz.jazzsoft.bnd.event.dal.Dal;
import kz.jazzsoft.bnd.event.entity.Event;
import kz.jazzsoft.bnd.event.entity.EventType;
import kz.jazzsoft.bnd.event.Names;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.jms.*;
import javax.swing.Spring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 17.01.13 11:40
 * Copyright ¬© LLP JazzSoft
 */
@Service(Names.Bean.EVENT_SERVICE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventService {
    private String url = "tcp://localhost:61616"; // Адрес очереди по умолчанию
    private String login = null;//Логин по умолчанию
    private String password = null;//Пароль по умолчанию
    @Autowired
    Dal dao;

    public void setURL(String url) {
        this.url = url;
    }

    public void setUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * получает события из рест сервиса и отправление в очередь BND_EVENT 
     *
     * @param queue   - имя очереди
     * @param message - сообщения
     * @throws JMSException
     */
    public void sendMessage(String queue, String message) throws JMSException {
        ConnectionFactory connectionFactory;
        if (login != null && password != null)
            connectionFactory = new ActiveMQConnectionFactory(login, password, url);
        else connectionFactory = new ActiveMQConnectionFactory(url);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageProducer producer = session.createProducer(destination);
        TextMessage msg = session.createTextMessage(message);

        producer.send(msg);
        connection.close();
    }

    /**
     * сохроняет событие из очереди в базу
     *
     * @param msg - сообщения из очереди в котором хранится ивэнт
     * @throws JMSException
     */
    public void saveEvent(Message msg) throws JMSException {
        Util util = new Util();
        TextMessage text = (TextMessage) msg;
        kz.jazzsoft.bnd.event.dto.Event deserialize = util.fromJSON(text.getText(), kz.jazzsoft.bnd.event.dto.Event.class);

        Event event = new Event();
        
        if( !(deserialize.getComponentName() instanceof String) || deserialize.getComponentName()==null){
        	System.out.println("eventComponent is not valid or null");
        	event.setComponentName("unknown");
        }
        else{
        	if(deserialize.getComponentName().length()<50)
        		event.setComponentName(deserialize.getComponentName());     
        	else{
        		System.out.println("eventComponent value >50 ");
        		event.setComponentName(deserialize.getComponentName().substring(0, 50));
        	}
        }
        event.setDateTime(new Date());
        
        if(deserialize.getDescription()==null || !(deserialize.getDescription() instanceof String)){
        	System.out.println("eventDescription is null or eventDescription type is not text");
        	return;
        }
        else{
        	if(deserialize.getDescription().length() <250)
            	event.setDescription(deserialize.getDescription());
            else{
               	System.out.println("eventDescription value > 250 ");
            	event.setDescription(deserialize.getDescription().substring(0, 250));
            }        	
        }	
        
        if(deserialize.getEventType()==null || !(deserialize.getEventType() instanceof Long)){
        	System.out.println("eventType is null or eventType or type is not number");
        	return;
        }
        List<EventType> eventTypes = dao.getEventTypeByCode((long) deserialize.getEventType());
        if (eventTypes.size() > 0)
            event.setEventType(eventTypes.get(0));
        
        if( !(deserialize.getModuleName() instanceof String) || deserialize.getModuleName()==null ){
        	System.out.println("eventModule is not text or null");
        	event.setModuleName("unknown");
        }
        else{
        	if(deserialize.getModuleName().length() < 50)
        	event.setModuleName(deserialize.getModuleName());
        	else{
           		System.out.println("eventModule value >50 ");
        		event.setModuleName(deserialize.getModuleName().substring(0, 50));
        	}        	
        }
        	
        if( !(deserialize.getResource() instanceof String) || deserialize.getResource()==null){
        	System.out.println("event resources is unknown or null");
        	event.setResource(deserialize.getResource());
        }
        else{
        	if(deserialize.getResource().length() < 50){
        		event.setResource(deserialize.getResource());
        	}
        	else{
        		System.out.println("eventResources value >50 ");
        		event.setModuleName(deserialize.getResource().substring(0, 50));
        	}
        
        }
        if(deserialize.getUser()==null && !(deserialize.getUser() instanceof Long)){
        	System.out.println("eventUser is null or not valid");
        	return;
        }
        
        if(dao.getUserByUserId((long) deserialize.getUser()).size() > 0)
        event.setUser(dao.getUserByUserId((long) deserialize.getUser()).get(0));
        else{
        	System.out.println("User don't exist, event can't be saved");
        	return;
        }
        dao.saveOrUpdate(event);
    }

    /**
     * получает сообщение из очередь BND_EVENT который приходит с интервалом 200мс и возвращает его содержание
     *
     * @param queue - Имя очереди
     * @return - содержание сообщение
     * @throws JMSException
     */
    public String getMessage(String queue) throws JMSException {
        ConnectionFactory connectionFactory;
        if (login != null && password != null) {
            connectionFactory = new ActiveMQConnectionFactory(login, password, url);
        } else {
            connectionFactory = new ActiveMQConnectionFactory(url);
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageConsumer consumer = session.createConsumer(destination);
        Message msg = consumer.receive(200);
        connection.close();

        if (msg instanceof ObjectMessage) {
            return (String) ((ObjectMessage) msg).getObject();

        } else {
            return null;
        }
    }

    /**
     * возвращает из очереди все сообщение
     *
     * @param queue - Имя очереди
     * @return - список содержание сообщении
     * @throws JMSException
     */
    public List<String> getAllMessages(String queue) throws JMSException {
        ConnectionFactory connectionFactory;
        if (login != null && password != null) {
            connectionFactory = new ActiveMQConnectionFactory(login, password, url);
        } else {
            connectionFactory = new ActiveMQConnectionFactory(url);
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageConsumer consumer = session.createConsumer(destination);
        Message msg;
        List<String> result = new ArrayList<String>();
        while ((msg = consumer.receive(200)) instanceof ObjectMessage) {
            result.add((String) ((ObjectMessage) msg).getObject());
        }
        connection.close();
        return result;
    }

    /**
     * Перехватчик сообщение из очереди
     *
     * @param queue    - Имя очереди
     * @param listener - Перехватчик сообщений
     * @throws JMSException
     */
    public void listen(String queue, MessageListener listener) throws JMSException {
        ConnectionFactory connectionFactory;
        if (login != null && password != null) {
            connectionFactory = new ActiveMQConnectionFactory(login, password, url);
        } else {
            connectionFactory = new ActiveMQConnectionFactory(url);
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(listener);
    }
}
