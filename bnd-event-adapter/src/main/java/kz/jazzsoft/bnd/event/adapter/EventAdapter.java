package kz.jazzsoft.bnd.event.adapter;

import kz.jazzsoft.bnd.event.dto.Event;
import kz.jazzsoft.bnd.event.dto.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class EventAdapter {
	
    CamelSender camelSender;

    Util util = new Util();

    public enum Type {
        ERROR, HINT, INFO, WARNING, CRITICAL
    }
    
    /**
     * Отправляю событие
     *
     * @param type          - тип события
     * @param userId        - идентификатор пользователя
     * @param description   - описание
     * @param moduleName    - название модуля
     * @param componentName - название компонента
     * @throws IOException 
     */
    public void sendEvent(Type type, Long userId, String description, String moduleName, String componentName) throws IOException {
        Event event = new Event();
        Long eventType = null;
        camelSender = new CamelSender();
        switch (type) {
            case ERROR:
                eventType = 100L;
                break;
            case HINT:
                eventType = 200L;
                break;
            case INFO:
                eventType = 300L;
                break;
            case WARNING:
                eventType = 400L;
                break;
            case CRITICAL:
                eventType = 500L;
                break;
        }
        event.setComponentName(componentName);
        event.setModuleName(moduleName);
        event.setDescription(description);
        event.setUser(userId);
        event.setDateTime(new Date());
        event.setEventType(eventType);
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            event.setResource(ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Проблемы с распознование ip адреса");
            event.setResource("Unknown");
        }
        String json = util.toJSON(event);
        byte[] bytes = util.stringToBytes(json);
        try {
            camelSender.sendAsync("event", bytes );
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    /**
     * Отправляю юзера
     *
     * @param userId - идентификатор пользователя
     * @param login  - логин пользователя
     */
    public void sendUser(Long userId, String login) {
        User user = new User();
        user.setUserId(userId);
        user.setLogin(login);

        try {
            camelSender.sendAsync("user", util.stringToBytes(util.toJSON(user)));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
