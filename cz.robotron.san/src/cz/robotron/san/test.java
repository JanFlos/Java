package cz.robotron.san;

import org.eclipse.e4.ui.workbench.renderers.swt.CTabRendering;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;

public class test {

    protected Shell _shell;
    private Table   _table;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            test window = new test();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        _shell.open();
        _shell.layout();
        while (!_shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        _shell = new Shell();
        _shell.setSize(450, 300);
        _shell.setText("SWT Application");
        _shell.setLayout(new GridLayout(1, false));

        CTabFolder tabFolder = new CTabFolder(_shell, SWT.NONE);
        tabFolder.marginWidth = -5;
        tabFolder.marginHeight = -5;
        tabFolder.setBorderVisible(true);
        tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        tabFolder.setMRUVisible(true);
        GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
        gd_tabFolder.widthHint = 316;
        tabFolder.setLayoutData(gd_tabFolder);
        tabFolder.setRenderer(new CTabRendering(tabFolder));
        tabFolder.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));

        CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
        tbtmNewItem.setText("New Item");

        CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
        tabItem_1.setText("New Item");

        CTabItem tabItem_2 = new CTabItem(tabFolder, SWT.NONE);
        tabItem_2.setText("New Item");

        _table = new Table(tabFolder, SWT.FULL_SELECTION);
        tabItem_2.setControl(_table);
        _table.setHeaderVisible(true);
        _table.setLinesVisible(true);

        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
        tabItem.setText("New Item");

        CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
        tbtmNewItem_1.setText("New Item");

    }
}
