package kz.jazzsoft.bnd.event.ui.table.filterComponents;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;

import javax.print.attribute.ResolutionSyntax;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;



public class DemoFilterDecorator implements FilterDecorator, Serializable {

    public String getEnumFilterDisplayName(Object propertyId, Object value) {
   
        // returning null will output default value
        return null;
    }

    public Resource getEnumFilterIcon(Object propertyId, Object value) {
  
        return null;
    }

    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? "Validated" : "Not validated";
        }
        // returning null will output default value
        return null;
    }

    public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? new ThemeResource("../runo/icons/16/ok.png")
                    : new ThemeResource("../runo/icons/16/cancel.png");
        }
        return null;
    }

    public String getFromCaption() {
        return "Start date:";
    }

    public String getToCaption() {
        return "End date:";
    }

    public String getSetCaption() {
        // use default caption
        return null;
    }

    public String getClearCaption() {
        // use default caption
        return null;
    }

    public boolean isTextFilterImmediate(Object propertyId) {
        // use text change events for all the text fields
        return true;
    }

    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    public String getAllItemsVisibleString() {
        return "Show all";
    }

//    public Resolution getDateFieldResolution(Object propertyId) {
//        return Resolution.DAY;
//    }

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("fi",
                "FI"));
    }

    public boolean usePopupForNumericProperty(Object propertyId) {
        // TODO Auto-generated method stub
        return true;
    }

   
    public String getDateFormatPattern(Object propertyId) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public int getDateFieldResolution(Object propertyId) {
		// TODO Auto-generated method stub
		return 0;
	}
}