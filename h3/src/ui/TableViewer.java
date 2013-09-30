package ui;

import h2.DataBlock;
import h2.DataBlockColumn;
import java.sql.SQLException;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import dml.MetadataProvider;
import dml.Record;
import events.SelectionChange;

public class TableViewer {

    Table                    _table;
    DataBlock                _dataBlock;
    private MetadataProvider _metadataProvider;
    private EventBus         _eventBus;

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
    public void synchronizeSelection(SelectionChange event) {
        _table.setSelection(event.getSelectionIndex());
    }

    private void registerSelectionListener() {

        // Delete existing selection listeners
        Listener[] listeners = _table.getListeners(SWT.Selection);
        if ( listeners!= null) {
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
                    getEventBus().post(new SelectionChange(list));

                }
                    

                
                
            }
        });
    }

    public TableViewer(Composite parent) throws SQLException {
        this(parent, null);
    }

    public TableViewer(Composite parent, DataBlock dataBlock) throws SQLException {
        _table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        _table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        _table.setLayoutData(data);

        if (dataBlock != null)
            setDataBlock(dataBlock);
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

        for (DataBlockColumn column : columns) {
            TableColumn tableColumn = new TableColumn(_table, SWT.NONE);
            tableColumn.setText(column.getName());
            tableColumn.setResizable(true);
            tableColumn.setWidth(100);
        }

        _table.setRedraw(true);
    }

    private void refreshContent() {

        _table.setRedraw(false);

        List<Record> records = _dataBlock.getRecords();
        for (Record record : records) {
            TableItem item = new TableItem(_table, SWT.None);
            item.setText(record.getData());

        }

        for (TableColumn column : _table.getColumns()) {
            column.pack();
        }

        _table.setRedraw(true);
    }

}
