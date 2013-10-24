package kz.jazzsoft.bnd.event.ui.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.jazzsoft.bnd.event.ui.table.containers.IndexContainer;
import kz.jazzsoft.bnd.event.ui.table.containers.Sqlcontainer;
import kz.jazzsoft.bnd.event.ui.table.filterComponents.DemoFilterDecorator;
import kz.jazzsoft.bnd.event.ui.table.filterComponents.DemoFilterGenerator;

import org.tepi.filtertable.FilterTable;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomTable.ColumnGenerator;
import com.vaadin.ui.Window.Notification;

/**
 * class EventTable is responsible for Table and it's configuration 
 */ 
public class EventTable{
	
    public String [] column = {"ref_event_type", "component_name", "module_name", 
      	  "description", "ref_user", "date_time", "resource"};
    public List<String> collapsedColumn;
    
    private FilterTable filterTable;
    private DemoFilterDecorator filterDecorator;
    private DemoFilterGenerator filterGenerator;
	private Sqlcontainer sqlContanier;
	private Button.ClickListener upListener;
	private Button.ClickListener downListener;

    public Window actionWindow;
    public EventDialog eventDialog;
    
    Map<String, String> words;
    
    /**
     * Get SqlContainer (SqlContainer is need to communicate with DB)
     * ABaseview is need to show notifications( Warning, Error )
     */ 
	public EventTable(Sqlcontainer dbhelper){
		filterTable = new FilterTable();
		sqlContanier = dbhelper;
		sqlContanier.initContainers();

		buildFilterTable();
	}
	
    /**
     * Create and configure FilterTable
     */ 
	 public FilterTable buildFilterTable() {	
		 
		 filterTable.setWidth("100%");
    	//filterTable.setSizeFull(); 	
		 filterTable.setFilterBarVisible(true);
		 filterTable.setSelectable(true);
		 filterTable.setImmediate(true);
		 filterTable.setMultiSelect(false);
		 filterTable.setColumnCollapsingAllowed(true);
		 filterTable.setColumnReorderingAllowed(true);		
		 filterTable.setPageLength(15);
		 filterTable.setContainerDataSource(sqlContanier.getContainer());
		changeColumn();
			
		filterGenerator = new DemoFilterGenerator(sqlContanier);
		filterDecorator = new DemoFilterDecorator();
		filterTable.setFilterGenerator(filterGenerator);
		filterTable.setFilterDecorator(filterDecorator);
		
		filterTable.setVisibleColumns(column);

		filterTable.setColumnHeaders(new String[] {"Event Type", "Component Name", "Module Name", 
				"Description", "User", "Date Time", "Resource"});		
		
		filterTable.setColumnCollapsed(column[3], true);
		filterTable.setColumnCollapsed(column[6], true);
		filterTable.setColumnCollapsed(column[2], true);
	
		words = new  HashMap<String, String>();
		eventDialog = new EventDialog();
		
		return filterTable;
	 }
	 
	/**
	 * Change ref_event_type and ref_user column type from int to String
	 * By id value of event_type and get it's String value
	 */    
	private void changeColumn() {
			filterTable.addGeneratedColumn("ref_event_type", new ColumnGenerator() {
				
				@Override
				public Object generateCell(CustomTable source, Object itemId,Object columnId) {
					if (filterTable.getItem(itemId).getItemProperty("ref_event_type").getValue() != null) {
				          String l = new String();
				          long eventTypeId = (Long) filterTable.getItem(itemId).getItemProperty(
				                "ref_event_type").getValue();
				          l = (sqlContanier.getEventTypeById(eventTypeId));
				          
				          return l;
				      }
				    
					return null;
				}
			});
			
			filterTable.addGeneratedColumn("ref_user", new ColumnGenerator() {
				
				@Override
				public Object generateCell(CustomTable source, Object itemId,Object columnId) {
					if (filterTable.getItem(itemId).getItemProperty("ref_user").getValue() != null) {
						  String l = new String();
				          long userId = (Long) filterTable.getItem(itemId).getItemProperty(
				                "ref_user").getValue();
				          l = (sqlContanier.getUserById(userId));
				          return l;
				      }
				    
					return null;
				}
			});
	}
    
    /**
     * Method to change column header value
     * Use to change localization value
     * 
     */  
    public void initializeColumns(String eventType, String componentName, String moduleName, 
			String description, String user, String dateTime, String resource,
			String filter_search, String filter_startDay, String filter_endDay, 
			String filter_set, String filter_clear, String filter_show,
			String setting, String delete, String ok, String cancel, String close,
			String dialog_delete, String page_item, String page_page){

	    	filterTable.setColumnHeader(column[0], eventType);
	    	filterTable.setColumnHeader(this.column[1], componentName);
	    	filterTable.setColumnHeader(this.column[2], moduleName);
	    	filterTable.setColumnHeader(this.column[3], description);
			filterTable.setColumnHeader(this.column[4], user);
			filterTable.setColumnHeader(this.column[5], dateTime);
			filterTable.setColumnHeader(this.column[6], resource);
			
			filterDecorator.setSearch(filter_search);
			filterDecorator.setStart(filter_startDay);
			filterDecorator.setEnd(filter_endDay);
			filterDecorator.setSet(filter_set);
			filterDecorator.setClear(filter_clear);
			filterGenerator.combo.setInputPrompt(filter_search);
			filterGenerator.setLabelValue(filter_show);
			filterGenerator.setUserInputPrompt(filter_search);
			filterTable.resetFilters();
			
			words.put("setting", setting);
			words.put("delete", delete);
			words.put("ok", ok);
			words.put("cancel", cancel);
			words.put("close", close);
			words.put("dialog_delete", dialog_delete);
			words.put("page.items", page_item);
			words.put("page.page", page_page);
		}
    
    /**
     * Return FilterTable 
     */ 
    public FilterTable getFilterTable() {
		return filterTable;
	}

    /**
     * Remove item from Table
     * And configure them
     */    
    public boolean removeEvent(final Application app){
    	final Object target = filterTable.getValue();
    	if(target == null){
			app.getMainWindow().showNotification(
					"WARNING", "Table row is not selected",
					Notification.TYPE_WARNING_MESSAGE
					);
		}
		else{
			actionWindow = new Window("Event Setting");
			actionWindow.setWidth("280px");
			
			VerticalLayout vl = new VerticalLayout();
			HorizontalLayout hr = new HorizontalLayout();
			Button ok = new Button(words.get("ok"));
			Button cancel = new Button(words.get("cancel"));
			ok.setWidth("90px");
			cancel.setWidth("90px");
			Label label = new Label("Do you really want to delete this Item?");
			
			vl.setMargin(true);
			vl.setSpacing(true);
			vl.addComponent(label);
			vl.addComponent(hr);
			vl.setWidth("100%");
			hr.setWidth("100%");
			
			hr.setSpacing(true);
			hr.addComponent(ok);
			hr.setComponentAlignment(ok, Alignment.BOTTOM_RIGHT);
			hr.addComponent(new Label(""){
				{	setWidth("70%");	}
			});
			hr.addComponent(cancel);
			hr.setComponentAlignment(cancel, Alignment.BOTTOM_LEFT);
			actionWindow.setContent(vl);
			actionWindow.center();
			actionWindow.setResizable(false);
			actionWindow.setImmediate(true);
			
			app.getMainWindow().addWindow(actionWindow);						
			cancel.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					app.getMainWindow().removeWindow(actionWindow);
				}
			});
			
			ok.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					System.out.println("target: "+target);
					filterTable.removeItem(target);					
					filterTable.commit();
					sqlContanier.getContainer().removeItem(target);
					try {
						sqlContanier.getContainer().commit();
					} catch (UnsupportedOperationException e) {

						e.printStackTrace();
					} catch (SQLException e) {

						e.printStackTrace();
					}
					filterTable.select(null);
					app.getMainWindow().removeWindow(actionWindow);
				}
			});
		}
    	return true;
    }

    public void getEvent(final Application app, final Object target){
    	if(target == null)
		{
			app.getMainWindow().showNotification(
					"WARNING", "Table row is not selected",
					Notification.TYPE_WARNING_MESSAGE
					);
			return;
		}
		
    	changeDialog(target);
		eventDialog.setWindowCaption(words.get("setting"));
		eventDialog.setButtonCaption(words.get("close"));

		eventDialog.getButton().addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				app.getMainWindow().removeWindow(eventDialog);
				
			}
		});
		
		eventDialog.getEvent();
		app.getMainWindow().addWindow(eventDialog);
    }
 
    private void changeDialog(final Object target)
    {
    	eventDialog.setTarget(target);
    	eventDialog.setEventType(filterTable.getColumnHeader(column[0]), sqlContanier.getEventTypeById( (Long) filterTable.getItem(target).getItemProperty(column[0]).getValue() ));
		eventDialog.setComponentName(filterTable.getColumnHeader(column[1]), filterTable.getItem(target).getItemProperty(column[1]).getValue());
		eventDialog.setModuleName(filterTable.getColumnHeader(column[2]), filterTable.getItem(target).getItemProperty(column[2]).getValue());
		eventDialog.setDescription(filterTable.getColumnHeader(column[3]), filterTable.getItem(target).getItemProperty(column[3]).getValue());
		eventDialog.setUser(filterTable.getColumnHeader(column[4]), sqlContanier.getUserById( (Long) filterTable.getItem(target).getItemProperty(column[4]).getValue() ));
		eventDialog.setDateTime(filterTable.getColumnHeader(column[5]), filterTable.getItem(target).getItemProperty(column[5]).getValue());
		eventDialog.setResource(filterTable.getColumnHeader(column[6]), filterTable.getItem(target).getItemProperty(column[6]).getValue());

		upListener = new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("up button pressed");
				eventDialog.getUp().removeAllValidators();
				
				Object itemId = filterTable.prevItemId(target);
				if(filterTable.firstItemId().equals(itemId)){
					eventDialog.showNotification(
							"WARNING", "You already select first item",
							Notification.TYPE_WARNING_MESSAGE
							);
					return;
				}
				eventDialog.getUp().removeListener(this);
				eventDialog.getDown().removeListener(downListener);
				changeDialog(itemId);				
			}
		};
		
		downListener = new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("down button pressed");
				Object itemId = filterTable.nextItemId(target);
				if(filterTable.lastItemId().equals(itemId)){
					eventDialog.showNotification(
							"WARNING", "You already select last item",
							Notification.TYPE_WARNING_MESSAGE
							);
					return;
				}
				eventDialog.getUp().removeListener(upListener);
				eventDialog.getDown().removeListener(this);
				changeDialog(itemId);
				
			}
		};
		
		eventDialog.getUp().addListener(upListener);
		eventDialog.getDown().addListener(downListener);
		
    }

    public void restoreTable(String dbName){
    	
    	filterTable.removeGeneratedColumn("ref_event_type");
    	filterTable.removeGeneratedColumn("ref_user");
    	
    	sqlContanier = new Sqlcontainer(dbName);
    	sqlContanier.initContainers();
		buildFilterTable();
    }
    
    public List<String> getCollapsedColumn()
    {
    	List<String> columns = new ArrayList<String>();
    	for (String string : column) {
			if(filterTable.isColumnCollapsed(string))
			{
				columns.add(string);
			}
		}
    	
    	return columns;
    }
    
    public void setCollapsedColumn(List<String> columns)
    {
    	for (String string : column) {
			filterTable.setColumnCollapsed(string, false);
		}
    	for (String string : columns) {
			filterTable.setColumnCollapsed(string, true);
		}
    }
    
    
    
}


