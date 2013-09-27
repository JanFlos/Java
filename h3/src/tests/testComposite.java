package tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class testComposite extends Composite {
    private final Table _table;
    private final Table _table_1;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public testComposite(Composite parent, int style) {
        super(parent, style);
        FormLayout formLayout = new FormLayout();
        setLayout(formLayout);

        _table = new Table(this, SWT.FULL_SELECTION);
        FormData fd_table = new FormData();
        fd_table.top = new FormAttachment(0, 10);
        fd_table.left = new FormAttachment(0, 10);
        fd_table.right = new FormAttachment(0, 183);
        fd_table.bottom = new FormAttachment(30, 73);

        //fd_table.bottom = new FormAttachment(0, 316);
        //fd_table.right = new FormAttachment(0, 674);
        _table.setLayoutData(fd_table);
        _table.setHeaderVisible(true);
        _table.setLinesVisible(true);

        _table_1 = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        FormData fd_table_1 = new FormData();
        //fd_table_1.bottom = new FormAttachment(_table, 0, SWT.BOTTOM);
        //fd_table_1.top = new FormAttachment(_table, 0, SWT.TOP);
        fd_table_1.right = new FormAttachment(_table, 203, SWT.RIGHT);
        fd_table_1.left = new FormAttachment(_table, 6);
        _table_1.setLayoutData(fd_table_1);
        _table_1.setHeaderVisible(true);
        _table_1.setLinesVisible(true);

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
