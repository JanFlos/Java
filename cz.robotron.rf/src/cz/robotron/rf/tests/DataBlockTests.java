package cz.robotron.rf.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.eclipse.swt.widgets.TableColumn;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import cz.robotron.rf.DataBlockColumn;
import cz.robotron.rf.DataBlockFactory;
import cz.robotron.rf.IDataBlock;
import cz.robotron.rf.QueryDataSource;
import cz.robotron.rf.dml.CommandBuilder;
import cz.robotron.rf.dml.MetadataProvider;
import cz.robotron.rf.ui.TableViewer;

public class DataBlockTests {

    private static ApplicationContext _appContext;
    private static Connection         _connection;
    private static DataBlockFactory   _factory;
    private static DmlExecutor        _dml;

    @BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        _appContext = TestUtils.getAppContext();
        _connection = _appContext.getConnection();
        _factory = new DataBlockFactory(_connection);
        _dml = new DmlExecutor(_connection);
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

        _dml.execute("delete from TTEST");

        IDataBlock dataBlock = _factory.createDataBlock("TTEST");

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
        _dml.execute("delete from TTEST");
        _dml.execute("insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");

        IDataBlock dataBlock = _factory.createDataBlock("TTEST");
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

        _dml.execute("delete from TTEST");
        _dml.execute("insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");
        _dml.execute("insert into TTS_DETAIL (tts_id, text) values (1, 'detail') ");

        IDataBlock master = _factory.createDataBlock("TTEST");
        IDataBlock detail = _factory.createDataBlock("TTS_DETAIL");
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

        _dml.execute("delete from TTEST");
        _dml.execute("insert into TTEST (id, name, tag) values (1, 'kuna','tag') ");
        _dml.execute("insert into TTS_DETAIL (tts_id, text) values (1, 'detail') ");

        IDataBlock master = _factory.createDataBlock("TTEST");
        IDataBlock detail = master.addDetailDataBlock("select * from TTS_DETAIL where tts_id = :id");

        master.executeQuery();
        master.firstRecord();
        detail.firstRecord();

        assertEquals(master.getItem(1), "kuna");
        assertEquals(detail.getItem(1), "detail");

    }

    @Test
    public void saveColumnWidths() throws SQLException {

        IDataBlock dbUiTables = _factory.createDataBlock("UI_TABLES");
        IDataBlock dbBerechnungen = _factory.createDataBlock("BERECHNUNGEN");
        IDataBlock dbColumns = dbUiTables.addDetailDataBlock("UIT_COLUMNS", "UIT_ID = :ID");

        dbUiTables.findOrCreate("id", "UIT_TABLES");
        TableViewer viewer = new TableViewer(null);
        viewer.setDataBlock(dbBerechnungen);

        TableColumn[] columns = viewer.getColumns();
        int width = 0;
        String name;
        for (TableColumn tableColumn : columns) {
            width = tableColumn.getWidth();
            name  = (String) tableColumn.getData();
            dbColumns.findOrCreate("name", name);
            dbColumns.setItem("width", width);
        }
        dbColumns.post();
    }

    /**
     * Sort order by test
     * @throws SQLException 
     */
    @Test
    @Ignore
    public void sortTest() throws SQLException {
        IDataBlock dataBlock = _factory.createDataBlock("BERECHNUNGEN");
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

        IDataBlock dataBlock = _factory.createDataBlock("TTEST");

        dataBlock.findOrCreate("name = ? and id = ?", "havran", 2);

    }
}
