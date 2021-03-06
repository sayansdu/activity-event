package kz.jazzsoft.bnd.event.service;

import java.io.IOException;
import java.util.List;

import kz.jazzsoft.bnd.event.adapter.Util;
import kz.jazzsoft.bnd.event.dal.Dal;
import kz.jazzsoft.bnd.event.dto.User;
import kz.jazzsoft.bnd.event.Names;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 28.01.13 10:13
 * Copyright © LLP JazzSoft
 */
@Service(Names.Bean.USER_SERVICE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserService {

    Dal dao = new Dal();

    /**
     * Сохраняю пользователя
     *
     * @param json - строка с данными пользователя
     * @throws JMSException
     * @throws IOException 
     */
    public void saveUser(String json) throws JMSException, IOException {
        Util util = new Util();
        User deserialize = util.fromJSON(json, User.class);
        
        if(deserialize.getLogin()==null){
        	System.out.println("user login is null");
        	return;
        }
        String login = deserialize.getLogin();
        
        if(deserialize.getUserId()==null){
        	System.out.println("user id is null");
        	return;
        }
        long userId = (long) deserialize.getUserId();
      
        List<kz.jazzsoft.bnd.event.entity.User> users = dao.getUsers();
        if(!checkUser(users, userId)){        	
            kz.jazzsoft.bnd.event.entity.User user = new kz.jazzsoft.bnd.event.entity.User();  
            user.setLogin(login);
            user.setUserId(userId);
            dao.saveUser(user);  
            return;
        }
        
        System.out.println("user already exist");
    }
    
    private static boolean checkUser(List<kz.jazzsoft.bnd.event.entity.User> users, long userId){
        for (kz.jazzsoft.bnd.event.entity.User user2 : users) {
			if (user2.getUserId()==userId) {
				return true;
			}
		}
        return false;
    }
    
}
