package tests;

import java.sql.SQLException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import cz.robotron.rcplight.DataBlock;
import cz.robotron.rcplight.ui.TableViewer;

public class UiTestHelper {

    static DataBlock     _master;
    static DataBlock     _detail;
    private static Color _alternateRowColor;

    public static RGB hex2Rgb(String colorStr) {
        return new RGB(
                       Integer.valueOf(colorStr.substring(1, 3), 16),
                       Integer.valueOf(colorStr.substring(3, 5), 16),
                       Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static void run() throws SQLException {

        Shell shell = TestUtils.getShell();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        shell.setLayout(layout);

        //        Device device = TestUtils.getDisplay().getCurrent();
        //        _alternateRowColor = new Color(device, hex2Rgb("#F5FFFA"));

        if (_master != null) {

            TableViewer tableViewerMaster = new TableViewer(shell, _master);
            tableViewerMaster.setAlternateRowColor(_alternateRowColor);

            _master.executeQuery();
            _master.firstRecord();
        }

        if (_detail != null) {
            TableViewer tableViewerDetail = new TableViewer(shell, _detail);
        }

        shell.pack();
        shell.setSize(shell.getBounds().width, 480);

        // start Ui
        TestUtils.startUI();

        if (_alternateRowColor != null)
            _alternateRowColor.dispose();
    }

    public static UiTestHelper master(DataBlock dataBlock) {
        _master = dataBlock;
        return null;
    }

    public static UiTestHelper detail(DataBlock dataBlock) {
        _detail = dataBlock;
        return null;
    }

    public static UiTestHelper block(DataBlock dataBlock) {
        _master = dataBlock;
        return null;
    }
}
