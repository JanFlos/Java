package tests;


import h2.DataBlock;
import java.sql.SQLException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ui.TableViewer;

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

        DataBlock dataBlock =
            DataBlock.createDataBlock(_appContext, "select ber_id, werte_von, name from berechnungen where rownum <= 100");
        dataBlock.executeQuery();

        Shell shell = TestUtils.getShell();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 4;
        layout.marginWidth = 4;
        shell.setLayout(layout);

        TableViewer tableViewer = new TableViewer(shell, dataBlock);

        shell.pack();
        shell.setSize(shell.getBounds().width, 480);


        TestUtils.startUI();
    }


}
