/**
 * 
 */
package cz.robotron.rf;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBlockFactory {

    private final Connection _connection;

    public DataBlockFactory(Connection connection) {
        _connection = connection;
    }

    public DataBlock createDataBlock(String dataSource) throws SQLException {
        return DataBlock.createDataBlock(_connection, dataSource);
    }

}

