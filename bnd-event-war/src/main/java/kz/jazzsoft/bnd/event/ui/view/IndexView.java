package kz.jazzsoft.bnd.event.ui.view;

import com.vaadin.ui.*;
import kz.jazzsoft.bnd.core.ui.view.LocalizationView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 10.01.13 15:12
 * Copyright © LLP JazzSoft
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IndexView extends LocalizationView {
    VerticalLayout verticalLayout;
    Label welcome;
    Select select;
    Button view, sender;
    
    @Override
    public void init() {
        verticalLayout = new VerticalLayout();
        welcome = new Label();
        Panel panel = new Panel();
        view = new Button("Переход к событиям");
        sender = new Button("Переход к сендеру");

        panel.setStyleName("bnd-event-welcomePanel");
        welcome.setStyleName("bnd-event-welcome");
        welcome.setHeight("50px");

        panel.addComponent(welcome);
        select = createLocalizationSelect();
        verticalLayout.addComponent(select);
        verticalLayout.addComponent(view);
        verticalLayout.addComponent(sender);
        verticalLayout.addComponent(panel);
        verticalLayout.setComponentAlignment(select, Alignment.MIDDLE_RIGHT);
        this.addComponent(verticalLayout);
    }

    @Override
    public void onShow() {
        getApplication().getMainWindow().setTheme("bnd-event");
        view.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                open(getApplication().getView(EventsView.class));
            }
        });

        sender.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                open(getApplication().getView(SenderView.class));
            }
        });
    }

    @Override
    public void update() {
        welcome.setValue(setLocaleValue("index.welcome"));
        addLanguage("kz", setLocaleValue("language.kz"));
        addLanguage("ru", setLocaleValue("language.ru"));
        addLanguage("en", setLocaleValue("language.en"));

    }
}
