package kz.jazzsoft.bnd.event.ui.view;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;
import kz.jazzsoft.bnd.core.ui.view.ABaseView;
import kz.jazzsoft.bnd.event.ui.element.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 10:47
 * Copyright © LLP JazzSoft
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MasterView extends ABaseView {
    Panel content;
    GridLayout contentWrapper;

    @Autowired
    Menu menu;

    @Override
    public void init() {
        contentWrapper = new GridLayout(1, 2);
        contentWrapper.setSizeFull();
        contentWrapper.setRowExpandRatio(1, 1);

        setStyleName("event-page");
        setSizeFull();
        content = new Panel();
        content.addStyleName(Runo.PANEL_LIGHT);
        content.addStyleName("event-content");
        content.setSizeFull();
        contentWrapper.addComponent(content, 0, 1);

        addComponent(contentWrapper);
    }

    @Override
    public void onShow() {
        contentWrapper.addComponent(menu, 0, 0);
    }
}
