package kz.jazzsoft.bnd.event.ui.table.containers;

import kz.jazzsoft.bnd.event.adapter.EventAdapter;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

/**
 * Class get container for easy filtering table
 * First sqlContainer get data from DB, insert to IndexedContainer
 * To easy filter by eventType and userId
 */ 
public class IndexContainer {
	
	private IndexedContainer iContainer;
	private SQLContainer sqlContainer;
    String eventType="ref_event_type", componentName="component_name", moduleName="module_name", 
        	  description="description", user="ref_user", dateTime="date_time", resource="resource";
    
	public IndexContainer(Sqlcontainer sqlContainer) {
		this.sqlContainer = sqlContainer.getContainer();
		iContainer = new IndexedContainer();
		
		for (Object ob : this.sqlContainer.getContainerPropertyIds()) { 
			if(ob.toString().equals(eventType)){
				iContainer.addContainerProperty(ob, EventAdapter.Type.class, null);
				continue;
			}
			if(ob.toString().equals(user)){
				iContainer.addContainerProperty(ob, String.class, null);
				continue;
			}
			iContainer.addContainerProperty(ob, this.sqlContainer.getType(ob), null);
		}
		
		for (Object ob : this.sqlContainer.getItemIds()) {
			iContainer.addItem(ob);
			iContainer.getContainerProperty(ob, eventType).setValue(  getTypes(  sqlContainer.getEventTypeById( (Long) this.sqlContainer.getItem(ob).getItemProperty(eventType).getValue() )));
			iContainer.getContainerProperty(ob, componentName).setValue(this.sqlContainer.getItem(ob).getItemProperty(componentName).getValue());
			iContainer.getContainerProperty(ob, moduleName   ).setValue(this.sqlContainer.getItem(ob).getItemProperty(moduleName).getValue());
			iContainer.getContainerProperty(ob, description  ).setValue(this.sqlContainer.getItem(ob).getItemProperty(description).getValue());
			iContainer.getContainerProperty(ob, user         ).setValue(sqlContainer.getUserById( (Long) this.sqlContainer.getItem(ob).getItemProperty(user).getValue()));
			iContainer.getContainerProperty(ob, dateTime     ).setValue(this.sqlContainer.getItem(ob).getItemProperty(dateTime).getValue());
			iContainer.getContainerProperty(ob, resource     ).setValue(this.sqlContainer.getItem(ob).getItemProperty(resource).getValue());
		}
	}
	
	public Container getContainer(){
		return iContainer;
	}
	
	/**
	 * Convert to EventType string value to enum value
	 */ 
	private EventAdapter.Type getTypes(String value){
		for (EventAdapter.Type type : EventAdapter.Type.values()) {
			if(type.toString().equals(value))
				return type;
		}
		return null;
	}
}
