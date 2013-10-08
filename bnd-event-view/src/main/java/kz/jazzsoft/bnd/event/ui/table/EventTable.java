package kz.jazzsoft.bnd.event.ui.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			
			filterTable.setVisibleColumns(column);

			filterTable.setColumnHeaders(new String[] {"Event Type", "Component Name", "Module Name", 
					"Description", "User", "Date Time", "Resource"});		
			
			if(collapsedColumn==null || collapsedColumn.size()==0)
			{
				filterTable.setColumnCollapsed(column[3], true);
				filterTable.setColumnCollapsed(column[6], true);
				filterTable.setColumnCollapsed(column[2], true);
			}
			else
			{
				for (String string : collapsedColumn) {
					filterTable.setColumnCollapsed(string, true);
				}
			}
			
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
		
		filterTable.setColumnHeader(column[0], eventType);
		filterTable.setColumnHeader(this.column[1], componentName);
		filterTable.setColumnHeader(this.column[2], moduleName);
		filterTable.setColumnHeader(this.column[3], description);
		filterTable.setColumnHeader(this.column[4], user);
		filterTable.setColumnHeader(this.column[5], dateTime);
		filterTable.setColumnHeader(this.column[6], resource);
		
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
			
			TextField event_type_field = new TextField(column[0]);
			TextField component_name_field = new TextField(column[1]);
			TextField module_name_field = new TextField(column[2]);
			TextArea description_field = new TextArea(column[3]);
			TextField user_field = new TextField(column[4]);
			TextField date_time_field = new TextField(column[5]);
			TextField resource_field = new TextField(column[6]);
			
			event_type_field.setValue(filterTable.getItem(target).getItemProperty(column[0]).getValue());
			component_name_field.setValue(filterTable.getItem(target).getItemProperty(column[1]).getValue());
			module_name_field.setValue(filterTable.getItem(target).getItemProperty(column[2]).getValue());
			description_field.setValue(filterTable.getItem(target).getItemProperty(column[3]).getValue());
			user_field.setValue(filterTable.getItem(target).getItemProperty(column[4]).getValue());
			date_time_field.setValue(filterTable.getItem(target).getItemProperty(column[5]).getValue());
			resource_field.setValue(filterTable.getItem(target).getItemProperty(column[6]).getValue());
			
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
    	collapsedColumn = getCollapsedColumn();
    	filterTable.removeAllItems();
    	sqlContanier.initContainers();
		iContainer = new IndexContainer(sqlContanier);
		buildFilterTable();
    }
    
    public List<String> getCollapsedColumn()
    {
    	List<String> columns = new ArrayList<String>();
    	for (String string : column) {
			if(filterTable.isColumnCollapsed(string))
				columns.add(string);
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


