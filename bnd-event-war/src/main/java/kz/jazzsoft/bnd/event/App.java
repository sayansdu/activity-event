package kz.jazzsoft.bnd.event;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.ResourceBundleI18N;
import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import kz.jazzsoft.bnd.eas.adapter.CheckUserRightsAdapter;
import kz.jazzsoft.bnd.event.ui.view.EventsView;
import kz.jazzsoft.bnd.event.ui.window.MainWindow;

import java.util.Locale;

@SuppressWarnings("serial")
public class App extends Application {
	CheckUserRightsAdapter userCheck;
	EventsView mainEventView;
	
    @Override
    public void init() { 
        I18N i18N = new ResourceBundleI18N("/kz/jazzsoft/bnd/event/i18n/messages", new Locale("en"), new Locale("kz"), new Locale("ru"));
        i18N.setCurrentLocale(new Locale("ru"));
        setTheme("bnd-event");
        
        mainEventView = new EventsView();
        
        Window mainWindow = new MainWindow("Журналирование", i18N);
        this.setMainWindow(mainWindow);
        mainWindow.addComponent(mainEventView);
        
        VerticalLayout vertical = new VerticalLayout();
        vertical.setMargin(true);
        vertical.setWidth("100%");
        Label permission = new Label("У вас нет прав для просмотра этой страницы");
        permission.setStyleName(Reindeer.LABEL_H2);
        vertical.addComponent(permission);
        vertical.setComponentAlignment(permission, Alignment.MIDDLE_CENTER);
        
        userCheck = new CheckUserRightsAdapter();
//        if(userCheck.checkUserRights("lemeshenko", "sd", new String[]{ "14", "11" }) )
//        	getContent().addComponent(getView(EventsView.class));
//        else
//       	getContent().addComponent(vertical);
        
    }
    
}
