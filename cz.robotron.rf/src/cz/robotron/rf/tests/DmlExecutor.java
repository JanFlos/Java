package cz.robotron.rf.tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlExecutor {

    private static Connection _connection;

    public DmlExecutor(Connection connection) {
        _connection = connection;
    }

    public void execute(String statement) throws SQLException {
        PreparedStatement prepareStatement = _connection.prepareStatement(statement);
        prepareStatement.executeUpdate();
        _connection.commit();

    }

}
