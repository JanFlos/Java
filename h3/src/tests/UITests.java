package tests;


import h2.DataBlock;
import h2.DataBlockColumn;
import java.sql.SQLException;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import dml.MetadataProvider;
import dml.Record;

public class UITests {

    private static ApplicationContext _appContext;

    @BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        _appContext = TestUtils.getAppContext();

    }

    @AfterClass
    public static void tearDown() throws SQLException {
        _appContext.done();
    }

    @Test
    public void test() throws SQLException {

        DataBlock dataBlock = DataBlock.createDataBlock(_appContext, "EC_SYS.REF_PERIODEN");
        dataBlock.executeQuery();

        Shell shell = TestUtils.getShell();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 4;
        layout.marginWidth = 4;

        Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);

        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(data);

        MetadataProvider metadata = dataBlock.getMetadataProvider();
        List<DataBlockColumn> columns = metadata.getColumns();
        TableColumn tableColumn = null;

        for (DataBlockColumn column : columns) {
            tableColumn = new TableColumn (table, SWT.NONE);
            tableColumn.setText(column.getName());
            tableColumn.setResizable(true);
            tableColumn.setWidth(100);
        }


        int columnCount = metadata.getColumnCount();
        List<Record> records = dataBlock.getRecords();
        for (Record record : records) {
            TableItem item = new TableItem(table, SWT.None);
            item.setText(record.getData());
            

        }

        shell.setLayout(layout);
        TestUtils.startUI();
    }


}
