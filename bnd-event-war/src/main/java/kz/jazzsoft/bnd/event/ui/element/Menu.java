package kz.jazzsoft.bnd.event.ui.element;

import kz.jazzsoft.bnd.core.ui.view.ABaseView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by: dmitriy.lemeshenko
 * Created: 11.01.13 10:46
 * Copyright © LLP JazzSoft
 */
@Component("eventMenu")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Menu extends ABaseView{
    @Override
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onShow() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
