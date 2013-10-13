/** 
 * Copyright (c) Robotron Datenbanksoftware GmbH and/or its affiliates. All rights reserved.
 */
package cz.robotron.rf.tests;

import java.sql.Connection;
import java.sql.SQLException;
import com.google.common.eventbus.EventBus;

/**
 * @author Jan Flos
 *
 */
public class ApplicationContext {

    Connection _connection;
    EventBus   _eventBus;

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return _connection;
    }

    /**
     * @return the eventBus
     */
    public EventBus getEventBus() {
        return _eventBus;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        _connection = connection;
    }

    /**
     * @param eventBus the eventBus to set
     */
    public void setEventBus(EventBus eventBus) {
        _eventBus = eventBus;
    }

    /**
     * @throws SQLException 
     * 
     */
    public void done() throws SQLException {
        if (_connection != null && !_connection.isClosed())
            _connection.close();

    }

}
