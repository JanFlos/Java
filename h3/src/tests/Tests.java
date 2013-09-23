package tests;

import h2.DataBlock;
import h2.QueryDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import dml.CommandBuilder;
import dml.Metadata;
import dml.TableColumn;

public class Tests {

    private static ApplicationContext _appContext;


    @BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        if (_appContext == null) {
            _appContext = new ApplicationContext();

            Class.forName("oracle.jdbc.OracleDriver");

            /* 
            System.setProperty("oracle.net.tns_admin", "c:\\Oracle\\Dev10g\\NETWORK\\ADMIN");
            _connection = DriverManager.getConnection("jdbc:oracle:thin:@ece", "ec_calc", "calc");
            */
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//vm/ecr4pres", "ec_calc", "calc");

            _appContext.setConnection(connection);
            _appContext.setEventBus(new EventBus());
        }
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        _appContext.done();
    }

    @Ignore
    @Test
    public void BuilderTest() {

        // Insert without returning
        Metadata metadata = getDMLMetadata(false);
        out(CommandBuilder.insertCommand(metadata));

        // Insert with returning
        metadata = getDMLMetadata(true);
        out(CommandBuilder.insertCommand(metadata));

        // Update statement
        out(CommandBuilder.updateCommand(metadata));

        out(CommandBuilder.deleteCommand(metadata));
        // out(DMLBuilder.lockCommand("berechnungen", ImmutableList.of("name",
        // "ber_id", "bezeichnung")));

    }

    @Ignore
    @Test
    public void test() throws SQLException {
        QueryDataSource queryDataSource = new QueryDataSource("TTEST");
        Metadata metadata = new Metadata(_appContext.getConnection(), queryDataSource);
        List<String> pkCols = metadata.getPrimaryKeyColumns();

    }


    @Ignore
    @Test
    public void dataBlockTest() throws SQLException {

        DataBlock dataBlock = DataBlock.createDataBlock(_appContext, "TTEST");
        dataBlock.createRecord();
        dataBlock.setItems(null, "Test", 1, "kuna", 25);
        dataBlock.post();
        dataBlock.setItem(1, "Test-Update");
        dataBlock.post();

    }

    @Test
    public void queryTest() throws SQLException {
        DataBlock dataBlock = DataBlock.createDataBlock(_appContext, "TTEST");
        int recordCount = dataBlock.executeQuery();
        System.out.println(recordCount);

    }

    private Metadata getDMLMetadata(boolean returningCols) {
        if (returningCols)
            return new Metadata("ttest",
                                ImmutableList.of("id", "name", "tag"),
                                ImmutableList.of(new TableColumn("id",
                                                                 Types.NUMERIC)),
                                ImmutableList.of("tag"));
        return new Metadata("ttest", ImmutableList.of("id", "name", "tag"));
    }



    private void out(String text) {
        System.out.println(text);

    }

}
