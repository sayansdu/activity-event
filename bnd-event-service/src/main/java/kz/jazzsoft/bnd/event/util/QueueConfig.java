package kz.jazzsoft.bnd.event.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 17.01.13 12:35
 * Copyright © LLP JazzSoft
 */
@Component
public class QueueConfig {
    Properties properties;

    /**
     * Конфигурация очереди
     */
    public QueueConfig() {
        try {
            properties = new Properties();
            properties.load(new ClassPathResource("query.properties").getInputStream());//файл конфигурации из ресурсов
            getClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вытаскиваю имя очереди
     *
     * @return имя очереди
     */
    public String getName() {
        return properties.getProperty("queue.name");
    }

    /**
     * Вытаскиваю url
     *
     * @return url
     */
    public String getURL() {
        return properties.getProperty("queue.url");
    }

    /**
     * Вытаскиваю логин
     *
     * @return логин
     */
    public String getLogin() {
        String login = properties.getProperty("queue.login");
        if (login.equals(null)) {
            return null;
        } else {
            return login;
        }
    }

    /**
     * Вытаскиваю пароль
     *
     * @return пароль
     */
    public String getPassword() {
        String password = properties.getProperty("queue.password");
        if (password.equals("null")) {
            return null;
        } else {
            return password;
        }
    }
}
