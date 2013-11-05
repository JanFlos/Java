package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class test {
    Connection _connection;

    @Before
    public void setUp() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String connectionString = "jdbc:derby:kuna.drb;create=true";
        _connection = DriverManager.getConnection(connectionString);

    }

    @After
    public void tearDown() throws Exception {
        _connection.close();
    }

    @Test
    public void test() throws SQLException {
        StringBuilder cmd = new StringBuilder();
        
        cmd.append("CREATE TABLE KUNA3(id INT, name VARCHAR(100))");
        
        Statement statement = _connection.createStatement();
        statement.executeUpdate("DROP TABLE KUNA4");
    }

    @Test
    void testFindOrCreate() {
        // dataBlock = new DataBlock();
    }
}
