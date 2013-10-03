package kz.jazzsoft.bnd.event.ui.table.filterComponents;

import info.joseluismartin.dao.Page;

import java.io.Serializable;

import org.tepi.filtertable.FilterGenerator;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.Notification;

public class DemoFilterGenerator implements FilterGenerator, Serializable {

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
		} else if ("checked".equals(propertyId)) {
			if (value != null && value instanceof Boolean) {
				if (Boolean.TRUE.equals(value)) {
					return new Compare.Equal(propertyId, value);
				} else {
					return new Or(new Compare.Equal(propertyId, true),
							new Compare.Equal(propertyId, false));
				}
			}
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
		// TODO Auto-generated method stub
		return null;
	}
}