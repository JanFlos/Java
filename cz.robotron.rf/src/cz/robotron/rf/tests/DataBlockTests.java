package cz.robotron.rf.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import cz.robotron.rf.DataBlock;
import cz.robotron.rf.DataBlockColumn;
import cz.robotron.rf.QueryDataSource;
import cz.robotron.rf.dml.CommandBuilder;
import cz.robotron.rf.dml.MetadataProvider;

public class DataBlockTests {

    private static ApplicationContext _appContext;
    private static Connection         _connection;

    @BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        _appContext = TestUtils.getAppContext();
        _connection = _appContext.getConnection();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        _appContext.done();
    }

    @Ignore
    @Test
    public void BuilderTest() {

        // Insert without returning
        MetadataProvider metadata = getDMLMetadata(false);
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
        MetadataProvider metadata = new MetadataProvider(_appContext.getConnection(), queryDataSource);
        List<String> pkCols = metadata.getPrimaryKeyColumnNames();

    }

    @Ignore
    @Test
    public void dataBlockTest() throws SQLException {

        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "TTEST");
        dataBlock.createRecord();
        dataBlock.setItems(null, "Test", 1, "kuna", 25);
        dataBlock.post();
        dataBlock.setItem(1, "Test-Update");
        dataBlock.post();

    }

    @Ignore
    @Test
    public void queryTest() throws SQLException {
        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "TTEST");
        int recordCount = dataBlock.executeQuery();
        System.out.println(recordCount);

    }

    /**
     * Master Detail Test
     */
    @Ignore
    @Test
    public void queryMasterDetailTest() throws SQLException {
        DataBlock master = DataBlock.createDataBlock(_connection, "TTEST");
        DataBlock detail = DataBlock.createDataBlock(_connection, "TTS_DETAIL");

        master.addDetailBlock(detail, "tts_id = :id");

        UiTestHelper.master(master).detail(detail).run();
    }

    /**
     * Sort order by test
     * @throws SQLException 
     */
    @Test
    public void sortTest() throws SQLException {
        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "BERECHNUNGEN");
        //dataBlock.setOrderBy("name", "tag");
        //dataBlock.executeQuery();
        UiTestHelper.block(dataBlock).run();

    }

    private MetadataProvider getDMLMetadata(boolean returningCols) {
        if (returningCols)
            return new MetadataProvider("ttest",
                                        ImmutableList.of("id", "name", "tag"),
                                        ImmutableList.of(new DataBlockColumn("id",
                                                                             Types.NUMERIC)),
                                        ImmutableList.of("tag"));
        return new MetadataProvider("ttest", ImmutableList.of("id", "name", "tag"));
    }

    private void out(String text) {
        System.out.println(text);

    }

    public void testFindOrCreate() throws SQLException {

        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "TTEST");

        dataBlock.findOrCreate("name = ?", "havran");

    }
}
