package kz.jazzsoft.bnd.event.util;

import java.io.IOException;

import kz.jazzsoft.bnd.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 29.01.13 15:04
 * Copyright © LLP JazzSoft
 */
@Component
public class QueueListener {
    @Autowired
    EventService eventService;

    @Autowired
    QueueConfig queueConfig;

    /**
     * Сохраняю очередь
     */
    @PostConstruct
    private void start() {
        try {
            eventService.listen(queueConfig.getName(), new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try 
                    {
                        eventService.saveEvent(message);
                    } 
                    catch (JMSException e) 
                    {
                        System.out.println("Ошибка при сохранении события");
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                    	e.printStackTrace();
            		}
                }
            });
        } catch (JMSException e) {
            System.out.println("Ошибка с очередью. Не могу найти очередь");
            e.printStackTrace();
        }
    }
}
