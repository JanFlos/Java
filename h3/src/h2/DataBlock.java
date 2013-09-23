/**
 * 
 */
package h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import tests.ApplicationContext;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import dml.Metadata;
import dml.Record;
import dml.RecordSaver;
import dml.RecordSelector;

/**
 * @author Jan Flos
 * 
 */
public class DataBlock {

    public static DataBlock createDataBlock() {
        return new DataBlock();
    }

    private QueryDataSource     _queryDataSource;
    private final ChangeTracker _changeTracker;
    private Metadata            _metadata;
    private List<Record>        _records;
    private Record              _currentRecord;
    private RecordSaver         _recordSaver;
    private RecordSelector      _recordSelector;
    private List<Relation>      _detailBlocks;
    private List<Relation>      _masterBlocks;

    /**
    * 
    */
    private DataBlock() {
        _changeTracker = new ChangeTracker();
    }

    /**
     * Sets the Table name or the query used to load data
     * 
     * @param string
     * @param dataSourceType
     * @throws SQLException 
     */
    public void setDataSource(
        Connection connection,
        String dataSource,
        String dmlTarget) throws SQLException
    {
        _queryDataSource = new QueryDataSource(dataSource, dmlTarget);
        _metadata = new Metadata(connection, _queryDataSource);
        _recordSaver = new RecordSaver(connection, _metadata);
        _recordSelector = new RecordSelector(connection, _metadata);

    }

    public void setDataSource(Connection connection,
        String dataSource) throws SQLException
    {
        setDataSource(connection, dataSource, null);
    }

    /**
     * Creates a new record in the block
     */
    public void createRecord() {

        assert _queryDataSource != null : "QueryDatasource must be set";
        assert _metadata != null;

        Object[] data = new Object[_metadata.getColumnCount()];
        Record record = Record.newQueriedRecord(data);

        _currentRecord = record;

        if (_records == null)
            _records = Lists.newArrayList();

        _records.add(record);
        _changeTracker.recordAdded(record);

    }

    /**
     * Changes the current record item value
     */
    public void setItem(String columnName, Object value) {
        assert _metadata != null;

        int columnIndex = _metadata.getColumnIndex(columnName);
        setItem(columnIndex, value);

    }

    /**
     * Changes the current record item value
     */
    public void setItem(int columnIndex, Object value) {
        assert _currentRecord != null;
        assert _changeTracker != null;

        _currentRecord.setColumn(columnIndex, value);
        _changeTracker.recordUpdated(_currentRecord);
    }

    /**
     * Posts changed records to database
     * @throws SQLException 
     */
    public void post() throws SQLException {

        assert _recordSaver != null;
        assert _changeTracker != null;

        _recordSaver.post(_changeTracker.getChangedRecords());

    }

    /**
     * Commits all changes to the database
     */
    public void commit() {
        // TODO Auto-generated method stub

    }

    public static DataBlock createDataBlock(ApplicationContext appContext, String queryDataSource, String dmlTarget) throws SQLException {
        DataBlock result = new DataBlock();
        result.setDataSource(appContext.getConnection(), queryDataSource, dmlTarget);
        appContext.getEventBus().register(result); // Register the datablock on event bus

        return result;
    }

    public static DataBlock createDataBlock(ApplicationContext appContext, String tableName) throws SQLException {
        DataBlock result = new DataBlock();
        result.setDataSource(appContext.getConnection(), tableName);
        return result;
    }

    /**
     * @param object
     * @param string
     * @param i
     */
    public void setItems(Object... objects) {
        for (int j = 0; j < objects.length; j++) {
            setItem(j, objects[j]);
        }

    }

    @Subscribe
    public void masterChanged() {

    }

    /**
     * @return 
     * @throws SQLException 
     * 
     */
    public int executeQuery() throws SQLException {
        assert _recordSelector != null;

        _records = _recordSelector.executeQuery();
        return _records.size();

    }

    /**
    * @param detail
    * @param joinCondition
    */
    public void addDetailBlock(DataBlock detail, String joinCondition) {

        assert detail != null;
        assert joinCondition != null;

        if (_detailBlocks == null)
            _detailBlocks = Lists.newArrayList();
        

        _detailBlocks.add(new Relation(detail, joinCondition));

        detail.addMasterBlock(this, joinCondition);
    }

    /**
     * @param dataBlock
     */
    private void addMasterBlock(DataBlock dataBlock, String joinCondition) {
        if (_masterBlocks == null)
            _masterBlocks = Lists.newArrayList();

        _masterBlocks.add(new Relation(dataBlock, joinCondition));

    }

    /**
     * Jump to first selected record 
     */
    public void firstRecord() {
        // TODO Auto-generated method stub

    }

}
