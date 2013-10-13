package cz.robotron.rf.ui;

import java.sql.SQLException;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.robotron.rf.DataBlock;
import cz.robotron.rf.DataBlockColumn;
import cz.robotron.rf.constants.SortOrderEnum;
import cz.robotron.rf.dml.MetadataProvider;
import cz.robotron.rf.dml.Record;
import cz.robotron.rf.events.ContentChangedEvent;
import cz.robotron.rf.events.SelectionChangedEvent;
import cz.robotron.rf.events.SortOrderChangedEvent;

public class TableViewer extends Composite {

    private static final String COLUMN_NAME = "columnName";
    Table                       _table;
    DataBlock                   _dataBlock;
    private MetadataProvider    _metadataProvider;
    private EventBus            _eventBus;
    private Color               _alternateRowColor;
    private Menu                _headerMenu;
    private MenuItem            _hideMenuItem;

    public void setDataBlock(DataBlock dataBlock) throws SQLException {

        assert _dataBlock != null;
        assert dataBlock.getMetadataProvider() != null;
        assert _table != null;

        _dataBlock = dataBlock;
        _metadataProvider = dataBlock.getMetadataProvider();
        _eventBus = new EventBus();

        // establish communication line with underlying datablock 
        observeOn(_dataBlock);
        _dataBlock.observeOn(this);

        createTableColumns();
        refreshContent();
        registerSelectionListener();
        registerHeaderMenu();

    }

    private void observeOn(Object object) {
        getEventBus().register(object);
    }

    private EventBus getEventBus() {
        if (_eventBus == null)
            _eventBus = new EventBus();
        return _eventBus;
    }

    @Subscribe
    public void handleQueryExecutedEvent(ContentChangedEvent event) {
        refreshContent();
    }

    @Subscribe
    public void handleSelectionChangedEvent(SelectionChangedEvent event) {
        Integer index = event.getSelectionIndex();

        if (index > 0 || index != _table.getSelectionIndex()) {
            _table.setSelection(index);
        }
    }

    private void registerSelectionListener() {

        // Delete existing selection listeners
        Listener[] listeners = _table.getListeners(SWT.Selection);
        if (listeners != null) {
            for (Listener listener : listeners) {
                _table.removeListener(SWT.Selection, listener);
            }
        }

        // Add selection listener    
        _table.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                String string = "";
                TableItem[] selection = _table.getSelection();

                if (selection != null) {
                    List<Integer> list = Lists.newArrayList();
                    for (int i = 0; i < selection.length; i++)
                        list.add(_table.indexOf(selection[i]));
                    getEventBus().post(new SelectionChangedEvent(list));

                }

            }
        });
    }

    public TableViewer(Composite parent) throws SQLException {
        this(parent, null);
    }

    public TableViewer(Composite parent, DataBlock dataBlock) throws SQLException {
        super(parent, SWT.NONE);
        setLayout(new GridLayout(1, false));
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        GridLayout layout = (GridLayout) this.getLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        _table = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        _table.setHeaderVisible(true);
        _table.setLinesVisible(false);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        _table.setLayoutData(data);

        if (dataBlock != null)
            setDataBlock(dataBlock);
    }

    private MenuItem createHeaderMenuItem(String name, Listener listener) {
        MenuItem menuItem = new MenuItem(_headerMenu, SWT.PUSH);
        menuItem.setText(name);
        if (listener != null)
            menuItem.addListener(SWT.Selection, listener);
        return menuItem;
    }

    private void createHeaderMenuSeparator() {
        MenuItem menuItem = new MenuItem(_headerMenu, SWT.SEPARATOR);
    }

    private void registerHeaderMenu() {
        _headerMenu = new Menu(_table.getShell(), SWT.POP_UP);

        _hideMenuItem = createHeaderMenuItem("Hide", new Listener() {
            @Override
            public void handleEvent(Event event) {
                TableColumn column = (TableColumn) _hideMenuItem.getData();
                column.setWidth(0);
                column.setResizable(false);
                
            }
        });        // Add menu for the column

        createHeaderMenuSeparator();

        TableColumn[] columns = _table.getColumns();
        for (TableColumn tableColumn : columns) {
            if (tableColumn.getResizable())
                createColumnOnOffMenuItem(tableColumn);
        }

        createHeaderMenuSeparator();
        createHeaderMenuItem("Options", null);        // Add menu for the column

        addMenuListener();

    }

    // setup table columns
    private void createTableColumns() {

        _table.setRedraw(false);

        // Delete the existing columns
        while (_table.getColumnCount() > 0) {
            _table.getColumns()[0].dispose();
        }

        // Add new columns
        List<DataBlockColumn> columns = _metadataProvider.getColumns();

        String columnName = null;
        for (DataBlockColumn column : columns) {
            TableColumn tableColumn = new TableColumn(_table, SWT.NONE);
            columnName = column.getName();

            tableColumn.setText(columnName);
            tableColumn.setResizable(true);
            tableColumn.setWidth(200);

            tableColumn.setData(COLUMN_NAME, columnName);
            addColumnSortListener(tableColumn); // Add sorting options
            //createMenuItem(tableColumn);        // Add menu for the column
        }

        _table.setRedraw(true);
    }

    private void addMenuListener() {

        _table.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Display display = _table.getDisplay();
                Point pt = display.map(null, _table, new Point(event.x, event.y));
                Rectangle clientArea = _table.getClientArea();
                boolean header = clientArea.y <= pt.y && pt.y < (clientArea.y + _table.getHeaderHeight());

                int x1 = 0;
                int x2 = 0;
                int a1 = _table.getHorizontalBar().getSelection();
                for (TableColumn column : _table.getColumns()) {
                    x2 = x1 + column.getWidth();
                    if (pt.x >= x1 - a1 && pt.x <= x2 - a1) {

                        _hideMenuItem.setText("Hide column [" + column.getText() + "] " + a1);
                        _hideMenuItem.setData(column);
                        break;
                    }
                    x1 = x2 + 1;
                }
                
                _table.setMenu(_headerMenu);
            }
        });

        /* IMPORTANT: Dispose the menus (only the current menu, set with setMenu(), will be automatically disposed) */
        _table.addListener(SWT.Dispose, new Listener() {
            @Override
            public void handleEvent(Event event) {
                _headerMenu.dispose();
            }
        });
    }

    private void createColumnOnOffMenuItem(final TableColumn column) {
        final MenuItem menuItem = new MenuItem(_headerMenu, SWT.CHECK);
        menuItem.setText(column.getText());
        menuItem.setSelection(column.getResizable());
        menuItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (menuItem.getSelection()) {
                    column.setWidth(150);
                    column.setResizable(true);
                } else {
                    column.setWidth(0);
                    column.setResizable(false);
                }
            }
        });
    }

    /**
     * @param tableColumn
     */
    private void addColumnSortListener(TableColumn tableColumn) {
        Listener sortListener = new Listener() {

            @Override
            public void handleEvent(Event event) {

                TableColumn sortColumn = _table.getSortColumn();
                TableColumn currentColumn = (TableColumn) event.widget;
                String columnName = null;
                SortOrderEnum sortOrder = null;

                int sortDirection = _table.getSortDirection();

                if ((sortColumn == null) || (sortColumn != currentColumn)) {
                    _table.setSortColumn(currentColumn);
                    sortDirection = SWT.UP;

                } else {

                    if (sortDirection == SWT.UP)
                        sortDirection = SWT.DOWN;
                    else if (sortDirection == SWT.DOWN) {
                        sortDirection = SWT.NONE;
                        _table.setSortColumn(null);
                    }

                }

                _table.setSortDirection(sortDirection);

                if (sortDirection == SWT.UP || sortDirection == SWT.DOWN) {
                    columnName = (String) currentColumn.getData(COLUMN_NAME);
                    sortOrder = (sortDirection == SWT.UP) ? SortOrderEnum.ASC : SortOrderEnum.DESC;
                }

                getEventBus().post(new SortOrderChangedEvent(columnName, sortOrder));

            }
        };
        tableColumn.addListener(SWT.Selection, sortListener);
    }

    private void refreshContent() {

        List<Record> records = _dataBlock.getRecords();
        int rowIndex = 0;
        if (records != null) {

            _table.setRedraw(false);

            // delete content
            boolean emptyTable = _table.getItemCount() == 0;
            _table.removeAll();

            // add content
            for (Record record : records) {
                TableItem item = new TableItem(_table, SWT.None);
                rowIndex++;
                if (_alternateRowColor != null)
                    if (rowIndex % 2 == 1)
                        item.setBackground(_alternateRowColor);

                item.setText(record.getData());

            }

            if (emptyTable) {
                for (TableColumn column : _table.getColumns()) {
                    column.pack();
                }
            }
            _table.setRedraw(true);
        }
    }

    public Color getAlternateRowColor() {
        return _alternateRowColor;
    }

    public void setAlternateRowColor(Color alternateRowColor) {
        _alternateRowColor = alternateRowColor;
    }
}
