package kz.jazzsoft.bnd.event.ui.table;

import java.sql.SQLException;

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
	
    String eventType="ref_event_type", componentName="component_name", moduleName="module_name", 
      	  description="description", user="ref_user", dateTime="date_time", resource="resource";
    
    private FilterTable filterTable;
    
	private Sqlcontainer sqlContanier;
	private IndexContainer iContainer;

    public Window actionWindow;
    
    /**
     * Get SqlContainer (SqlContainer is need to communicate with DB)
     * ABaseview is need to show notifications( Warning, Error )
     */ 
	public EventTable(Sqlcontainer dbhelper){
		filterTable = new FilterTable();
		sqlContanier = dbhelper;
		sqlContanier.initContainers();
		iContainer = new IndexContainer(sqlContanier);
		buildFilterTable();
	}
	
    /**
     * Create and configure FilterTable
     */ 
	 public FilterTable buildFilterTable() {	
    	filterTable.setSizeFull(); 	
    	filterTable.setFilterBarVisible(true);
    	filterTable.setSelectable(true);
		filterTable.setImmediate(true);
		filterTable.setMultiSelect(false);
		filterTable.setColumnCollapsingAllowed(true);
		filterTable.setColumnReorderingAllowed(true);		
		
		filterTable.setContainerDataSource(iContainer.getContainer());
		
		filterTable.setFilterDecorator(new DemoFilterDecorator());
		filterTable.setFilterGenerator(new DemoFilterGenerator());
		
		filterTable.setVisibleColumns(new String[] {eventType, componentName, moduleName, 
		      	  description, user, dateTime, resource });

		filterTable.setColumnHeaders(new String[] {"Event Type", "Component Name", "Module Name", 
				"Description", "User", "Date Time", "Resource"});		
		
		filterTable.setColumnCollapsed(description, true);
		filterTable.setColumnCollapsed(resource, true);
		filterTable.setColumnCollapsed(moduleName, true);
		
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
				          Label l = new Label();
				          long eventTypeId = (Long) filterTable.getItem(itemId).getItemProperty(
				                "ref_event_type").getValue();
				          l.setValue(sqlContanier.getEventTypeById(eventTypeId));
				          l.setSizeUndefined();
				          return l;
				      }
				    
					return null;
				}
			});
			
			filterTable.addGeneratedColumn("ref_user", new ColumnGenerator() {
				
				@Override
				public Object generateCell(CustomTable source, Object itemId,Object columnId) {
					if (filterTable.getItem(itemId).getItemProperty("ref_user").getValue() != null) {
				          Label l = new Label();
				          int userId = (Integer) filterTable.getItem(itemId).getItemProperty(
				                "ref_user").getValue();
				          l.setValue(sqlContanier.getUserById(userId));
				          l.setSizeUndefined();
				          return l;
				      }
				    
					return null;
				}
			});
	}
    
    /**
     * Method to change column header value
     * Usually used when change localization value
     * 
     */  
    public void initializeColumns(String eventType, String componentName, String moduleName, 
			String description, String user, String dateTime, String resource){
		
		filterTable.setColumnHeader(this.eventType, eventType);
		filterTable.setColumnHeader(this.componentName, componentName);
		filterTable.setColumnHeader(this.moduleName, moduleName);
		filterTable.setColumnHeader(this.user, user);
		filterTable.setColumnHeader(this.description, description);
		filterTable.setColumnHeader(this.dateTime, dateTime);
		filterTable.setColumnHeader(this.resource, resource);
		
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
			Button ok = new Button("Ok");
			Button cancel = new Button("Cancel");
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
				
					filterTable.removeItem(target);					
					filterTable.commit();
					sqlContanier.getContainer().removeItem(target);
					try 
					{
						sqlContanier.getContainer().commit();
					} 
					catch (UnsupportedOperationException e) {e.printStackTrace();} 
					catch (SQLException e) {				 e.printStackTrace();}
					
					filterTable.select(null);
					app.getMainWindow().removeWindow(actionWindow);
				}
			});
		}
    	return true;
    }

    public void getEvent(final Application app){
    	Object target = filterTable.getValue();
    	if(target == null){
			app.getMainWindow().showNotification(
					"WARNING", "Table row is not selected",
					Notification.TYPE_WARNING_MESSAGE
					);
		}
		else{
			FormLayout form = new FormLayout();
			form.setMargin(true);
			form.setSpacing(true);
			
			TextField event_type_field = new TextField(eventType);
			TextField component_name_field = new TextField(componentName);
			TextField module_name_field = new TextField(moduleName);
			TextArea description_field = new TextArea(description);
			TextField user_field = new TextField(user);
			TextField date_time_field = new TextField(dateTime);
			TextField resource_field = new TextField(resource);
			
			event_type_field.setValue(filterTable.getItem(target).getItemProperty(eventType).getValue());
			component_name_field.setValue(filterTable.getItem(target).getItemProperty(componentName).getValue());
			module_name_field.setValue(filterTable.getItem(target).getItemProperty(moduleName).getValue());
			description_field.setValue(filterTable.getItem(target).getItemProperty(description).getValue());
			user_field.setValue(filterTable.getItem(target).getItemProperty(user).getValue());
			date_time_field.setValue(filterTable.getItem(target).getItemProperty(dateTime).getValue());
			resource_field.setValue(filterTable.getItem(target).getItemProperty(resource).getValue());
			
			event_type_field.setColumns(15);
			component_name_field.setColumns(15);
			module_name_field.setColumns(15);
			user_field.setColumns(15);
			description_field.setColumns(15);
			date_time_field.setColumns(15);
			resource_field.setColumns(15);
			
			form.addComponent(event_type_field);
			form.addComponent(component_name_field);
			form.addComponent(module_name_field);
			form.addComponent(description_field);
			form.addComponent(user_field);
			form.addComponent(date_time_field);
			form.addComponent(resource_field);
			
			actionWindow = new Window("Event Setting");
			actionWindow.setWidth("400px");
			actionWindow.setResizable(false);
			
			VerticalLayout vl = new VerticalLayout();
			HorizontalLayout hr = new HorizontalLayout();
			Button cancel = new Button("Close");
			vl.setMargin(true);
			vl.setSpacing(true);
			vl.addComponent(form);
			vl.addComponent(hr);						
			
			hr.addComponent(cancel);
			hr.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
			vl.setComponentAlignment(hr, Alignment.MIDDLE_RIGHT);
			
			actionWindow.setContent(vl);
			actionWindow.center();
			app.getMainWindow().addWindow(actionWindow);		
			cancel.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					app.getMainWindow().removeWindow(actionWindow);
				}
			});
		}
    }
 
    public void updateTable(){
    	filterTable.removeAllItems();
    	sqlContanier.initContainers();
		iContainer = new IndexContainer(sqlContanier);
		buildFilterTable();
    }
}


