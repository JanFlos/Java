package cz.robotron.rf.tests;

import java.sql.SQLException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import cz.robotron.rf.IDataBlock;
import cz.robotron.rf.ui.TableViewer;

public class UiTestHelper {

    static IDataBlock     _master;
    static IDataBlock     _detail;
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

    public static UiTestHelper master(IDataBlock dataBlock) {
        _master = dataBlock;
        return null;
    }

    public static UiTestHelper detail(IDataBlock dataBlock) {
        _detail = dataBlock;
        return null;
    }

    public static UiTestHelper block(IDataBlock dataBlock) {
        _master = dataBlock;
        return null;
    }
}
