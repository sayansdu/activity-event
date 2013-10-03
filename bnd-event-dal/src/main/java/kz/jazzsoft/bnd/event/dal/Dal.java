package kz.jazzsoft.bnd.event.dal;

import kz.jazzsoft.bnd.core.dao.BaseDao;
import kz.jazzsoft.bnd.core.entity.ABaseEntity;
import kz.jazzsoft.bnd.event.entity.Event;
import kz.jazzsoft.bnd.event.entity.EventType;
import kz.jazzsoft.bnd.event.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 11:50
 * Copyright © LLP JazzSoft
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Dal {
    @Autowired
    BaseDao dao;

    
    /**
     * Список типов событий
     *
     * @return типы событий
     */
    public List<EventType> getEventType() {
        return dao.findAll(EventType.class);
    }

    /**
     * Поиск типа событий по коду
     *
     * @param code - код типа событий
     * @return тип события
     */
    public List<EventType> getEventTypeByCode(long code) {
        List<EventType> types = getEventType();
        List<EventType> type = new ArrayList<EventType>();
        for (EventType eventType : types) {
			if(eventType.getCode()==code){
				type.add(eventType);
				return type;
			}
		}
        return type;
    }

    /**
     * Сохраняю события/пользователей
     */
    public <T extends ABaseEntity> void saveOrUpdate(T entity) {
        dao.saveOrUpdate(entity);
    }

    /**
     * Вытаскиваю пользователя по его идентификатору
     *
     * @param userId - идентификатор пользователя
     * @return пользователя
     */
    public List<User> getUserByUserId(Long userId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return dao.select(User.class, "FROM User u where u.userId=:userId", params);
    }

    /**
     * Вытаскиваю события по дате
     *
     * @param begin - начальное значение
     * @param end   - конечное значение
     * @return лист событий
     */
    public List<Event> getEventBetween(Object begin, Object end) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("begin", begin);
        params.put("end", end);
        return dao.select(Event.class, "FROM Event e where e.dateTime between :begin and :end", params);
    }

    /**
     * Удаляю события
     *
     * @param events - лист событий
     */
    public void removeList(List<Event> events) {
        if (events.size() > 0) {
            for (Event event : events) {
                dao.remove(event);
            }
        }
    }

    /**
     * Список пользователей
     *
     * @return лист пользователей
     */
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }
   

}
