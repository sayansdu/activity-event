package kz.jazzsoft.bnd.event.ui.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

import kz.jazzsoft.bnd.event.localization.LocaleView;
import kz.jazzsoft.bnd.event.ui.table.EventTable;
import kz.jazzsoft.bnd.event.ui.table.containers.Sqlcontainer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventsView extends LocaleView{
    
	
    Dbconfig dbconfig = new Dbconfig();
	
    EventTable eventTable;
    HorizontalLayout mainLayout, topLayout, archiveLayout, bottomLayout;
    VerticalLayout verticalLayout;
    Button show, archive, restore, remove, setting, update;
    Tree tree;
    
    String directory = directory(), current_day="Current Date", restored_day="Archives";
    
    private final String DB_URL = dbconfig.getDBURL();
    private final String DB_URL_Archive = dbconfig.getDBArchiveURL();
    
    
    public EventsView() {
    	initialDatabase();
    	mainLayout = new HorizontalLayout();
        verticalLayout = new VerticalLayout();
        archiveLayout = new HorizontalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        mainLayout.setSpacing(true);
        
        tree = new Tree();
        tree.setWidth("200px");
        tree.setHeight("100%");
        tree.setVisible(false);
    	tree.addItem(current_day);
    	tree.addItem(restored_day);
    	
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
        
        
        mainLayout.addComponent(tree);
        mainLayout.addComponent(verticalLayout);
        mainLayout.setExpandRatio(verticalLayout, 1.5f);
   
        Select select = createLocalizationSelect();
        treeRefresh(tree, directory);
        
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
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH-mm-ss");
				String file_name  = sdf.format(new Date()).toString();				
				File path = new File(directory+"/"+file_name);
				if(!path.exists()) path.mkdirs();
			
				backup(path.getAbsolutePath() , file_name);
				treeRefresh(tree, directory);
			}
		});
        
        restore.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event){ 				
				String selected = (String) tree.getValue();
				if(selected.equals(current_day)) 	changeDatabase(DB_URL);				
				else 									restore(selected);

				eventTable.initializeColumns(setLocaleValue("event.eventType"), setLocaleValue("event.componentName"), 
				setLocaleValue("event.moduleName"), setLocaleValue("event.eventDescription"), setLocaleValue("event.user"),
				setLocaleValue("event.dateTime"), setLocaleValue("event.resource"));
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
			public void buttonClick(ClickEvent event) {
				eventTable.updateTable();
				eventTable.initializeColumns(setLocaleValue("event.eventType"), setLocaleValue("event.componentName"), 
				setLocaleValue("event.moduleName"), setLocaleValue("event.eventDescription"), setLocaleValue("event.user"),
				setLocaleValue("event.dateTime"), setLocaleValue("event.resource"));
				
			}
		});
    }


    @Override
    public void update() {
        addLanguage("ru", setLocaleValue("language.ru"));
        addLanguage("kz", setLocaleValue("language.kz"));
        addLanguage("en", setLocaleValue("language.en"));
        eventTable.initializeColumns(setLocaleValue("event.eventType"), setLocaleValue("event.componentName"), 
				setLocaleValue("event.moduleName"), setLocaleValue("event.eventDescription"), setLocaleValue("event.user"),
				setLocaleValue("event.dateTime"), setLocaleValue("event.resource") );
        
        archive.setCaption(setLocaleValue("button.archive"));
        restore.setCaption(setLocaleValue("button.restore"));
                
        tree.removeAllItems();
        tree.addItem(current_day = setLocaleValue("event.current"));
        tree.addItem(restored_day = setLocaleValue("event.restore"));
        treeRefresh(tree, directory);
        
        remove.setCaption(setLocaleValue("button.remove"));
        setting.setCaption(setLocaleValue("button.show"));
        update.setCaption(setLocaleValue("button.update"));
		
    }
    
    /**
     * Add new item to tree
     */
    private void treeRefresh(Tree tree, String directory){
    	File files = new File(directory);
    	if(files.exists()){
    		for (File file : files.listFiles()) {
    			if(file.getName().startsWith("."))
    				continue;
    			tree.addItem(file.getName());
				tree.setParent(file.getName(), restored_day);
			}
    	}
    	else 
    		System.out.println("File don't exist");
    	    return;
    }
    
    private String directory(){
		String userHome=System.getProperty("user.home");
		return userHome+"/bnd/event/archives";
    }

    /**
     * unzip backup and dearchive iy to bnd_event2 DB by pg_restore 
     */
    private void restore(String file_name){	
    	
        try{
        	Process p;
        	ProcessBuilder pb;
        	
        	pb = new ProcessBuilder( 
        	    dbconfig.getPG_restore() ,
        	    "--ignore-version",
        	    "--host", dbconfig.getDBIP(),
        	    "--port", dbconfig.getDBPort(),
        	    "--username", dbconfig.getUserName(),
        	    "-d", dbconfig.getDBArchiveName(),
        	    "--verbose",
        	    "--clean",
        	    
        	    directory+"/"+file_name+"/"+file_name+".backup" );
        	pb.redirectErrorStream(true);
        	p = pb.start();
        	InputStream is = p.getInputStream();
        	InputStreamReader isr = new InputStreamReader(is);
        	BufferedReader br = new BufferedReader(isr);
        	String ll;
        	while ((ll = br.readLine()) != null) {
        	 System.out.println(ll);
        	}    
        	
        	changeDatabase(DB_URL_Archive);
        }
        catch(IOException x)
            {
                System.err.println("Could not invoke browser, command=");
                System.err.println("Caught: " + x.getMessage());
            }
    }
 
    /**
     * Archives current DB and save it to directory we specified 
     * It creates process to archive and use pg_dumb file
     */ 
    private void backup(String directory, String file_name){
        try{
        	Process p;
        	ProcessBuilder pb;
        	pb = new ProcessBuilder( 
        	    dbconfig.getPG_dump(),
        	    "--host", dbconfig.getDBIP() ,
        	    "--port", dbconfig.getDBPort(),
        	    "--username", dbconfig.getUserName(),
        	    "--verbose",
        	    "-F", "c",
        	    "-w",
        	    "-f", directory+"/"+file_name+".backup",
        	    dbconfig.getDBName());
        	pb.redirectErrorStream(true);
        	p = pb.start();
        	InputStream is = p.getInputStream();
        	InputStreamReader isr = new InputStreamReader(is);
        	BufferedReader br = new BufferedReader(isr);
        	String ll;
        	while ((ll = br.readLine()) != null) {
        	 System.out.println(ll);
        	}                    
        	
        }
        catch(IOException x)
            {
                System.err.println("Could not invoke browser, command=");
                System.err.println("Caught: " + x.getMessage());
            }
    }

    /**
     * Set initial DB name to bnd_event 
     */ 
    private void initialDatabase(){
    	Sqlcontainer db = new Sqlcontainer(DB_URL);
    	eventTable = new EventTable(db);
    }
  
    /**
     * Change DB name, it uses when press unzip button
     * DB name bnd_event and bnd_event2
     */ 
    private void changeDatabase(String dbName){
    	Sqlcontainer db = new Sqlcontainer(dbName);
    	verticalLayout.removeComponent(eventTable.getFilterTable());
    	verticalLayout.removeComponent(bottomLayout);
    	eventTable = new EventTable(db);
    	
    	verticalLayout.addComponent(eventTable.getFilterTable());
    	verticalLayout.addComponent(bottomLayout);
    }

}
