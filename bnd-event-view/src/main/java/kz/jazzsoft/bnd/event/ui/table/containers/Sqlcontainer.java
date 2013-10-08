package kz.jazzsoft.bnd.event.ui.table.containers;

import java.sql.SQLException;


import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

/**
 * Lightweight container to communicate Table with DB
 */ 
public class Sqlcontainer {

	Dbconfig dbConfig = new Dbconfig();
	
	private static JDBCConnectionPool connectionPool;
	private static final String DB_DRIVER = "org.postgresql.Driver";
	private SQLContainer container;
	
    public Sqlcontainer(String DB_URL) {
		try {
			connectionPool = new SimpleJDBCConnectionPool(DB_DRIVER,
					DB_URL, dbConfig.getUserName(), dbConfig.getUserPassword(), 2, 5);
		} catch (SQLException e) {
			throw new RuntimeException("Error creating connection pool.", e);
		}
		
    	initContainers();
    }
    
    /**
     * initialize container with bnd_event DB
     */ 
    public void initContainers() {
    	QueryDelegate delegate = new TableQuery(dbConfig.getDBName(), connectionPool);
    	try {
    		container = new SQLContainer(delegate);
    		container.setAutoCommit(true);
    		
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return type by event_type id
     */ 
    public String getEventTypeById(long eventTypeId){
    	QueryDelegate delegate = new TableQuery("bnd_event_type", connectionPool);
		SQLContainer container2;
    	String type = "CRITICAL";
		try {
    		container2 = new SQLContainer(delegate); 
    		type = (String) container2.getItem(new RowId(new Object[]{eventTypeId})).getItemProperty("name").getValue();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return type;
    }
    
    /**
     * Return user name by userId
     */ 
    public String getUserById(long userId){
    	TableQuery delegate = new TableQuery("bnd_users", connectionPool);
		SQLContainer container2;
    	String login = "CRITICAL";
    	
		try {
    		container2 = new SQLContainer(delegate);
    		login = (String) container2.getItem(new RowId(new Object[]{userId})).getItemProperty("login").getValue();
 
    		
		}catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return login;
    }
    
    public SQLContainer getContainer(){
    	return container;
    }
    
}
