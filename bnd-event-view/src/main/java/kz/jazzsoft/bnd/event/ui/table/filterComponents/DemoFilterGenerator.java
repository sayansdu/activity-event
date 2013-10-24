package kz.jazzsoft.bnd.event.ui.table.filterComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kz.jazzsoft.bnd.event.ui.table.containers.Sqlcontainer;

import org.tepi.filtertable.FilterGenerator;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class DemoFilterGenerator implements FilterGenerator, Serializable {
	
	Sqlcontainer sqlcontainer;
	public ComboBox combo;
	private Label show_all;
	private Label userFieldPrompt;
	TextField field;
	
	public DemoFilterGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public DemoFilterGenerator(Sqlcontainer sqlcontainer) {
		this.sqlcontainer = sqlcontainer;
	}
	
	public Filter generateFilter(Object propertyId, Object value) {
		
		if ("id".equals(propertyId)) {
			/* Create an 'equals' filter for the ID field */
			if (value != null && value instanceof String) {
				try {
					return new Compare.Equal(propertyId,
							Integer.parseInt((String) value));
				} catch (NumberFormatException ignored) {
					// If no integer was entered, just generate default filter
				}
			}
		} else if ("checked".equals(propertyId)) 
		{
			if (value != null && value instanceof Boolean) {
				if (Boolean.TRUE.equals(value)) {
					return new Compare.Equal(propertyId, value);
				} else {
					return new Or(new Compare.Equal(propertyId, true),
							new Compare.Equal(propertyId, false));
				}
			}
		}
		else if("ref_user".equals(propertyId))
		{
			if(value.toString().trim().equals("")){
				return new Not(new IsNull("ref_user"));
			}

			List<Filter> filters = new ArrayList<Filter>();
			long id = 0;
			String input = value.toString().trim();
			SQLContainer container = sqlcontainer.getUsersContainer();

			
			container.addContainerFilter(new Like("login", ("%"+input+"%") ));
			for (Object ob : container.getItemIds()) {
				
				id =  (Long) container.getContainerProperty(ob, "id").getValue() ;
				filters.add(new Compare.Equal(propertyId, id));	
			}
			
			if(filters.isEmpty()){
				return new IsNull("ref_user");
			}
			
			return new Or(filters.toArray(new Filter[]{}));
		}
		else if("ref_event_type".equals(propertyId))
		{
			if(value.equals(show_all.getValue()))
				return new Not(new IsNull("ref_event_type"));
			
			long id = 0;
			
			for (Object eventType : sqlcontainer.getEventTypesContainer().getItemIds()) 
			{
				String name = (String) sqlcontainer.getEventTypesContainer().getContainerProperty(eventType, "name").getValue();
				if(name.equals(value.toString()))
				{
					id = (Long) sqlcontainer.getEventTypesContainer().getContainerProperty(eventType, "id").getValue();
					break;
				}
			}
			return new Compare.Equal("ref_event_type", id);
		}
		// For other properties, use the default filter
		return null;
	}

	
	public Filter generateFilter(Object propertyId, Field originatingField) {
		// Use the default filter
		return null;
	}


	public void filterRemoved(Object propertyId) {

	}

	public void filterAdded(Object propertyId,
			Class<? extends Filter> filterType, Object value) {
	}

	
	public Filter filterGeneratorFailed(Exception reason, Object propertyId,
			Object value) {
		/* Return null -> Does not add any filter on failure */
		return null;
	}


	@Override
	public AbstractField getCustomFilterComponent(Object propertyId) {
		if("ref_event_type".equals(propertyId))
		{
			if(show_all==null)
			{
				show_all = new Label();
				show_all.setValue("Show all");
			}
			combo = new ComboBox();
			combo.addItem(show_all.getValue());
			combo.addItem("ERROR");
			combo.addItem("HINT");
			combo.addItem("INFO");
			combo.addItem("WARNING");
			combo.addItem("CRITICAL");
			combo.setNullSelectionAllowed(false);
			combo.setImmediate(true);
			combo.setMultiSelect(false);
			combo.setTextInputAllowed(false);
			return combo;
		}
		if("ref_user".equals(propertyId))
		{
			if(userFieldPrompt==null)
			{
				userFieldPrompt = new Label();
				userFieldPrompt.setValue("Search");
			}
			field = new TextField();
			field.setInputPrompt(userFieldPrompt.getValue().toString());
			field.setImmediate(true);
			field.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
			field.setTextChangeTimeout(300);
			return field;
		}
		return null;
	}
	
	public void setUserInputPrompt(String value)
	{
		userFieldPrompt = new Label();
		userFieldPrompt.setValue(value);
		field.requestRepaintRequests();
	}
	
	public void setLabelValue(String value) {
		show_all = new Label();
		show_all.setValue(value);
		combo.requestRepaintRequests();
	}
	
}