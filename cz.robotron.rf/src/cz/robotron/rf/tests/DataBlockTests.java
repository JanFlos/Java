package cz.robotron.rf.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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

    @Test
    public void testCommandBuilder() {

        String command;
        
        // Insert without returning
        MetadataProvider metadata = getDMLMetadata(false);
        command = CommandBuilder.insertCommand(metadata);
        assertEquals(command, "{call insert into ttest (id, name, tag) values (?, ?, ?)}");

        // Insert with returning
        metadata = getDMLMetadata(true);
        command = CommandBuilder.insertCommand(metadata);
        assertEquals(command, "{call insert into ttest (id, name) values (?, ?) returning id into ?}");

        // Update statement
        command = CommandBuilder.updateCommand(metadata);
        assertEquals(command, "{call update ttest set name = ? where id = ?}");

        command = CommandBuilder.deleteCommand(metadata);
        assertEquals(command, "{call delete from ttest where id = ?}");

    }

    @Test
    public void testPrimaryKeyCols() throws SQLException {
        QueryDataSource queryDataSource = new QueryDataSource("TTEST");
        MetadataProvider metadata = new MetadataProvider(_appContext.getConnection(), queryDataSource);
        List<String> pkCols = metadata.getPrimaryKeyColumnNames();
        assertEquals(pkCols.toString(), "[id]");
    }

    @Test
    public void testCRUD() throws SQLException {
        int count;
        
        TestUtils.executeDML(_connection, "delete from TTEST");

        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "TTEST");

        dataBlock.createRecord();
        dataBlock.setItems(0, "Test1", "Tag");
        dataBlock.post();

        count = TestUtils.selectCount(_connection, "select count(*) from ttest where name = 'Test1'");
        assertEquals(count, 1);

        Object id = dataBlock.getItem(0);
        assertNotNull(id);

        dataBlock.setItem(1, "Test2");
        dataBlock.post();

        count = TestUtils.selectCount(_connection, "select count(*) from ttest where name = 'Test1'");
        assertEquals(count, 0);

        count = TestUtils.selectCount(_connection, "select count(*) from ttest where name = 'Test2'");
        assertEquals(count, 1);
        
    }

    @Test
    public void queryTest() throws SQLException {
        TestUtils.executeDML(_connection, "delete from TTEST");
        TestUtils.executeDML(_connection, "insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");

        DataBlock dataBlock = DataBlock.createDataBlock(_connection, "TTEST");
        int recordCount = dataBlock.executeQuery();
        dataBlock.firstRecord();
        assertEquals(recordCount, 1);
        assertEquals(dataBlock.getItem(0).toString(), "1");
        assertEquals(dataBlock.getItem(1), "kuna");
        assertEquals(dataBlock.getItem(2), "tag");

    }

    /**
     * Master Detail Test
     */
    @Test
    public void queryMasterDetailTest1() throws SQLException {

        TestUtils.executeDML(_connection, "delete from TTEST");
        TestUtils.executeDML(_connection, "insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");
        TestUtils.executeDML(_connection, "insert into TTS_DETAIL (tts_id, text) values (1, 'detail') ");

        DataBlock master = DataBlock.createDataBlock(_connection, "TTEST");
        DataBlock detail = DataBlock.createDataBlock(_connection, "TTS_DETAIL");
        master.addDetail(detail, "tts_id = :id");

        master.executeQuery();
        master.firstRecord();
        detail.firstRecord();

        assertEquals(master.getItem(1), "kuna");
        assertEquals(detail.getItem(1), "detail");

    }

    /**
     * Master Detail Test
     */
    @Test
    public void queryMasterDetailTest2() throws SQLException {

        TestUtils.executeDML(_connection, "delete from TTEST");
        TestUtils.executeDML(_connection, "insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");
        TestUtils.executeDML(_connection, "insert into TTS_DETAIL (tts_id, text) values (1, 'detail') ");

        DataBlock master = DataBlock.createDataBlock(_connection, "TTEST");
        DataBlock detail = master.addDetailDataBlock("select * from TTS_DETAIL where tts_id = :id");

        master.executeQuery();
        master.firstRecord();
        detail.firstRecord();

        assertEquals(master.getItem(1), "kuna");
        assertEquals(detail.getItem(1), "detail");

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

        dataBlock.findOrCreate("name = ? and id = ?", "havran", 2);

    }
}
