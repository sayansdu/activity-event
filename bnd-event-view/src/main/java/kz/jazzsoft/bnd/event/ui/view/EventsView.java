package kz.jazzsoft.bnd.event.ui.view;

import java.util.Date;
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
    VerticalLayout verticalLayout, tableLayout;
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
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        
        tableLayout = new VerticalLayout();
        tableLayout.addComponent(eventTable.getFilterTable());
        tableLayout.setWidth("100%");
        
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
        remove.setIcon(new ThemeResource("icon/delete.png"));
        remove.setStyleName(Reindeer.BUTTON_LINK);
        setting = new Button();
        setting.setIcon(new ThemeResource("icon/setting_tools.png"));
        setting.setStyleName(Reindeer.BUTTON_LINK);
        update = new Button();
        update.setIcon(new ThemeResource("icon/update.png"));
        update.setStyleName(Reindeer.BUTTON_LINK);
        
        Label space = new Label("&nbsp", Label.CONTENT_XHTML);
        space.setWidth("10px");
        
        bottomLayout.addComponent(remove);
        bottomLayout.addComponent(space);
        bottomLayout.addComponent(setting);
        bottomLayout.addComponent(space);
        bottomLayout.addComponent(update);
        bottomLayout.addComponent(space);
        bottomLayout.setHeight("40px");
        bottomLayout.setComponentAlignment(remove, Alignment.TOP_RIGHT);
        bottomLayout.setComponentAlignment(setting, Alignment.TOP_RIGHT);
        bottomLayout.setComponentAlignment(update, Alignment.TOP_RIGHT);
        
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
        verticalLayout.addComponent(tableLayout);
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
				changeDatabase(DB_URL);
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
					System.out.println("restore start: "+(new Date()));
					if(selected.equals(tree.getCurrentDay())) 	
					{ 	
						changeDatabase(DB_URL);   
					}		
					else
					{ 						
						pgSQL.restore(selected);
						changeDatabase(DB_URL_Archive);		
					}
	
					localeTableColumn();
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
				eventTable.getEvent(EventsView.this.getApplication(), eventTable.getFilterTable().getValue());
			}
		});
        
        update.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) 
			{
				changeDatabase(DB_URL);
				localeTableColumn();
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
						localeTableColumn();	
						
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
        
        localeTableColumn();
        
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
    	tableLayout.removeComponent(eventTable.getFilterTable());
    	
    	eventTable.restoreTable(dbName);
    	eventTable.setCollapsedColumn(columns);
    	tableLayout.addComponent(eventTable.getFilterTable());
    }

    private void localeTableColumn(){
		eventTable.initializeColumns(   
				setLocaleValue("event.eventType"), 
				setLocaleValue("event.componentName"), 
				setLocaleValue("event.moduleName"), 
				setLocaleValue("event.eventDescription"), 
				setLocaleValue("event.user"),
				setLocaleValue("event.dateTime"), 
				setLocaleValue("event.resource"),
				setLocaleValue("filter.search"),
				setLocaleValue("filter.start.day"),
				setLocaleValue("filter.end.day"),
				setLocaleValue("filter.set"),
				setLocaleValue("filter.clear"),
				setLocaleValue("filter.show"),
				setLocaleValue("window.setting"),
				setLocaleValue("window.delete"),
				setLocaleValue("button.ok"),
				setLocaleValue("button.cancel"),
				setLocaleValue("close"),
				setLocaleValue("dialog.delete"),
				setLocaleValue("page.items"),
				setLocaleValue("page.page")
			);
    }
}
