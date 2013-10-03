package tests;

import h2.DataBlock;
import java.sql.SQLException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import ui.TableViewer;

public class UiTestHelper {

    static DataBlock _master;
    static DataBlock _detail;

    public static void run() throws SQLException {

        Shell shell = TestUtils.getShell();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        shell.setLayout(layout);

        _master.executeQuery();
        _master.firstRecord();

        TableViewer tableViewerMaster = new TableViewer(shell, _master);
        TableViewer tableViewerDetail = new TableViewer(shell, _detail);


        shell.pack();
        shell.setSize(shell.getBounds().width, 480);

        // start Ui
        TestUtils.startUI();
    }

    public static UiTestHelper master(DataBlock dataBlock) {
        _master = dataBlock;
        return null;
    }

    public static UiTestHelper detail(DataBlock dataBlock) {
        _detail = dataBlock;
        return null;
    }
}
