package kz.jazzsoft.bnd.event.ui.view;

import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import kz.jazzsoft.bnd.event.localization.LocaleView;
import kz.jazzsoft.bnd.event.ui.table.EventTable;
import kz.jazzsoft.bnd.event.ui.table.containers.Dbconfig;
import kz.jazzsoft.bnd.event.ui.table.containers.PgSQLOperations;
import kz.jazzsoft.bnd.event.ui.table.containers.Sqlcontainer;
import kz.jazzsoft.bnd.event.ui.view.tree.GetMonths;
import kz.jazzsoft.bnd.event.ui.view.tree.LeftTree;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventsView extends LocaleView{
    
	
	Dbconfig dbconfig = new Dbconfig();
	PgSQLOperations pgSQL;    
	LeftTree tree;
	
    EventTable eventTable;
    HorizontalLayout mainLayout, topLayout, archiveLayout, bottomLayout;
    VerticalLayout verticalLayout;
    Button show, archive, restore, remove, setting, update;
    
    private final String DB_URL = dbconfig.getDBURL();
    private final String DB_URL_Archive = dbconfig.getDBArchiveURL();
    private List<String> columns;
    
    
    public EventsView() {
    	initialDatabase();
    	pgSQL = new PgSQLOperations();
    	dbconfig = new Dbconfig();
    	tree = new LeftTree();
    	tree.setDirectory(pgSQL.directory());
    	
    	mainLayout = new HorizontalLayout();
        verticalLayout = new VerticalLayout();
        archiveLayout = new HorizontalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        mainLayout.setSpacing(true);
        
        show = new Button();
        show.setStyleName(Reindeer.BUTTON_LINK);
        show.setIcon(new ThemeResource("icon/right-32X32.png"));
        
        archive = new Button("Archive");
        restore = new Button("Restore");;
        archive.setWidth("130px");
        restore.setWidth("130px");
        
        bottomLayout = new HorizontalLayout();
        bottomLayout.setSpacing(true);
        
        remove = new Button();
        setting = new Button();
        update = new Button();
        bottomLayout.addComponent(remove);
        bottomLayout.addComponent(setting);
        bottomLayout.addComponent(update);
        
        Select select = createLocalizationSelect();
        tree.refresh();
        
        mainLayout.addComponent(tree);
        mainLayout.addComponent(verticalLayout);
        mainLayout.setExpandRatio(verticalLayout, 1.5f);
        
        topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.addComponent(show);
        topLayout.setComponentAlignment(show, Alignment.TOP_LEFT);
        topLayout.addComponent(select);
        topLayout.setComponentAlignment(select, Alignment.TOP_RIGHT);
        this.addComponent(topLayout);
        archiveLayout.setSpacing(true);
        archiveLayout.addComponent(archive);
        archiveLayout.addComponent(new Label(" "));
        archiveLayout.addComponent(restore);
        
        this.addComponent(mainLayout);
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(archiveLayout);
        verticalLayout.addComponent(eventTable.getFilterTable());
        verticalLayout.addComponent(bottomLayout);
        
   
        /**
         * starts add to Buttons ClickListener 
         */       
        show.addListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tree.isVisible() == false)  { tree.setVisible(true);   show.setIcon(new ThemeResource("icon/left-32X32.png")); }				
				else						   { tree.setVisible(false);  show.setIcon(new ThemeResource("icon/right-32X32.png"));  }		
			}
		});
        
        archive.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {			
				pgSQL.backup();
				tree.refresh();
			}
		});
        
        restore.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event){ 				
				String selected = (String) tree.getValue();
				if(selected==null)
				{
					getApplication().getMainWindow().showNotification(
							"WARNING", "Archive is not selected",
							Notification.TYPE_WARNING_MESSAGE
							);
				}
				else
				{
					if(selected.equals(tree.getCurrentDay())) 	
					{ 	
						changeDatabase(DB_URL);   
					}		
					else
					{ 						
						pgSQL.restore(selected);
						changeDatabase(DB_URL_Archive);		
					}
	
					eventTable.initializeColumns(   setLocaleValue("event.eventType"), 
													setLocaleValue("event.componentName"), 
													setLocaleValue("event.moduleName"), 
													setLocaleValue("event.eventDescription"), 
													setLocaleValue("event.user"),
													setLocaleValue("event.dateTime"), 
													setLocaleValue("event.resource"));	
				}
			}
		});
  
        remove.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				boolean result = eventTable.removeEvent(EventsView.this.getApplication());
				System.out.println("remove event is: "+result);
			}
		});
        
        setting.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				eventTable.getEvent(EventsView.this.getApplication());
			}
		});
        
        update.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) 
			{
				eventTable.updateTable();
				eventTable.initializeColumns(   setLocaleValue("event.eventType"), 
												setLocaleValue("event.componentName"), 
												setLocaleValue("event.moduleName"), 
												setLocaleValue("event.eventDescription"), 
												setLocaleValue("event.user"),
												setLocaleValue("event.dateTime"), 
												setLocaleValue("event.resource"));	
			}
		});
  
        tree.addListener(new ItemClickEvent.ItemClickListener() {
						
			public void itemClick(ItemClickEvent event) 
			{
				String selected = (String) tree.getValue();
				if(event.isDoubleClick())
				{
					if(selected!=null){
						if(selected.equals(tree.getCurrentDay())) 
						{ 	
							changeDatabase(DB_URL);
						}		
						else
						{ 						
							pgSQL.restore(selected);
							changeDatabase(DB_URL_Archive);		
						}
	
						eventTable.initializeColumns(   setLocaleValue("event.eventType"), 
														setLocaleValue("event.componentName"), 
														setLocaleValue("event.moduleName"), 
														setLocaleValue("event.eventDescription"), 
														setLocaleValue("event.user"),
														setLocaleValue("event.dateTime"), 
														setLocaleValue("event.resource"));	
					}
					
				}
				
			}
		});
    }


    @Override
    public void update() {
        addLanguage("ru", setLocaleValue("language.ru"));
        addLanguage("kz", setLocaleValue("language.kz"));
        addLanguage("en", setLocaleValue("language.en"));
		eventTable.initializeColumns(   setLocaleValue("event.eventType"), 
										setLocaleValue("event.componentName"), 
										setLocaleValue("event.moduleName"), 
										setLocaleValue("event.eventDescription"), 
										setLocaleValue("event.user"),
										setLocaleValue("event.dateTime"), 
										setLocaleValue("event.resource"));
        
        archive.setCaption(setLocaleValue("button.archive"));
        restore.setCaption(setLocaleValue("button.restore"));
                
        
     
        remove.setCaption(setLocaleValue("button.remove"));
        setting.setCaption(setLocaleValue("button.show"));
        update.setCaption(setLocaleValue("button.update"));
        
		GetMonths.all_months.put("01", setLocaleValue("summary.table.january") );
		GetMonths.all_months.put("02", setLocaleValue("summary.table.february") );
		GetMonths.all_months.put("03", setLocaleValue("summary.table.march") );
		GetMonths.all_months.put("04", setLocaleValue("summary.table.april") );
		GetMonths.all_months.put("05", setLocaleValue("summary.table.may") );
		GetMonths.all_months.put("06", setLocaleValue("summary.table.june") );
		GetMonths.all_months.put("07", setLocaleValue("summary.table.july") );
		GetMonths.all_months.put("08", setLocaleValue("summary.table.august") );
		GetMonths.all_months.put("09", setLocaleValue("summary.table.september") );
		GetMonths.all_months.put("10", setLocaleValue("summary.table.october") );
		GetMonths.all_months.put("11", setLocaleValue("summary.table.november") );
		GetMonths.all_months.put("12", setLocaleValue("summary.table.december") );
		tree.updateItems(setLocaleValue("event.current"), setLocaleValue("event.restore"));
		
    }
    
    private void initialDatabase(){
    	Sqlcontainer db = new Sqlcontainer(DB_URL);
    	eventTable = new EventTable(db);
    }
  
    /**
     * Change DB name, it uses when press unzip button
     * DB names bnd_event and bnd_event_archive
     */ 
    private void changeDatabase(String dbName){
    	columns = eventTable.getCollapsedColumn();
    	Sqlcontainer db = new Sqlcontainer(dbName);
    	verticalLayout.removeComponent(eventTable.getFilterTable());
    	verticalLayout.removeComponent(bottomLayout);
    	
    	eventTable = new EventTable(db);
    	eventTable.setCollapsedColumn(columns);
    	
    	verticalLayout.addComponent(eventTable.getFilterTable());
    	verticalLayout.addComponent(bottomLayout);
    }

}
