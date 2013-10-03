package kz.jazzsoft.bnd.event.controller;

import java.io.IOException;

import kz.jazzsoft.bnd.event.adapter.Util;
import kz.jazzsoft.bnd.event.service.EventService;
import kz.jazzsoft.bnd.event.service.UserService;
import kz.jazzsoft.bnd.event.util.QueueConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 17.01.13 11:08
 * Copyright ¬© LLP JazzSoft
 */
@Controller
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    QueueConfig queueConfig;   

    Util util = new Util();

    /**
     * обработка запроса /sendData
     *
     * @param data - входные данные (POST-запрос)
     * @return строку с данными события
     */
    @RequestMapping(value = "/sendData", method = RequestMethod.POST)
    public
    @ResponseBody
    void sendData(@RequestBody byte[] data) {
        try {
        	String json = util.bytesToString(data);
            eventService.sendMessage(queueConfig.getName(), json);
        } catch (JMSException e) {
            System.out.println("ошибка при добавление события /sendData");
            e.printStackTrace();
        }
    }

    /**
     * обработка запроса /sendUserData
     *
     * @param data - входные данные (POST-запрос)
     * @return строку с данными пользователя
     */
    @RequestMapping(value = "/sendUserData", method = RequestMethod.POST)
    public
    @ResponseBody
    void sendUserData(@RequestBody byte[] data) {
        try {
            userService.saveUser(util.bytesToString(data));
        } catch (JMSException e) {
            System.out.println("ошибка при добавление пользователя /sendUserData");
            e.printStackTrace();
        }
        catch (IOException e) {
        	e.printStackTrace();
		}
    }
    
    @RequestMapping(value = "/output", method = RequestMethod.POST)
    @ResponseBody
    public String updateUser(@RequestBody String userName) {
    	System.out.println("sdh fkhsd sdh fksddfh kjsdf kjasdl fasdjdsfklsjdfh jkf sd");
        System.out.println("Received string: "+userName);
        return "Response";
    }
    
}
