package tests;


import h2.DataBlock;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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

        final DataBlock dataBlock =
            DataBlock.createDataBlock(_appContext, "select ber_id, werte_von, name from berechnungen where rownum <= 100");
        dataBlock.executeQuery();

        Shell shell = TestUtils.getShell();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 4;
        layout.marginWidth = 4;
        shell.setLayout(layout);

        TableViewer tableViewer = new TableViewer(shell, dataBlock);

        Button button = new Button(shell, SWT.NONE);
        button.setText("pressme");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataBlock.nextRecord();
            }
        });

        shell.pack();
        shell.setSize(shell.getBounds().width, 480);


        TestUtils.startUI();
    }


}
