/**
 * 
 */
package h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import tests.ApplicationContext;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import dml.MetadataProvider;
import dml.Parameter;
import dml.Record;
import dml.RecordSaver;
import dml.RecordSelector;
import events.QueryExecutedEvent;
import events.SelectionChangedEvent;

/**
 * @author Jan Flos
 * 
 */
public class DataBlock {

    public static DataBlock createDataBlock() {
        return new DataBlock();
    }

    private QueryDataSource  _queryDataSource;
    private ChangeTracker    _changeTracker;
    private MetadataProvider _metadataProvider;
    private List<Record>     _records;
    private Record           _currentRecord;
    private RecordSaver      _recordSaver;
    private RecordSelector   _recordSelector;
    private List<Relation>   _detailRelations;
    private Relation         _masterRelation;
    private EventBus         _eventBus;
    private Connection       _connection;
    private boolean          _defferedCoordination;

    /**
    * 
    */
    private DataBlock() {}

    /**
     * Sets the Table name or the query used to load data
     */
    private void setDataSource(
        Connection connection,
        String dataSource,
        String dmlTarget) throws SQLException
    {
        _queryDataSource = new QueryDataSource(dataSource, dmlTarget);
        _connection = connection;

        _metadataProvider = null;
        _records = null;
        _currentRecord = null;
        _recordSaver = null;
        _recordSelector = null;

    }

    public void setDataSource(Connection connection,
        String dataSource) throws SQLException
    {
        setDataSource(connection, dataSource, null);
    }

    /**
     * Creates a new record in the block
     * @throws SQLException 
     */
    public void createRecord() throws SQLException {

        Object[] data = new Object[getMetadataProvider().getColumnCount()];
        Record record = Record.newQueriedRecord(data);

        _currentRecord = record;

        if (_records == null)
            _records = Lists.newArrayList();

        _records.add(record);
        getChangeTracker().recordAdded(record);

    }

    /**
     * Changes the current record item value
     */
    public void setItem(String columnName, Object value) {
        assert _metadataProvider != null;

        int columnIndex = _metadataProvider.getColumnIndex(columnName);
        setItem(columnIndex, value);

    }

    /**
     * Changes the current record item value
     */
    public void setItem(int columnIndex, Object value) {
        assert _currentRecord != null;

        _currentRecord.setValue(columnIndex, value);
        getChangeTracker().recordUpdated(_currentRecord);
    }

    public Object getItem(int columnIndex) {
        assert _currentRecord != null;

        return _currentRecord.getValue(columnIndex);

    }

    /**
     * Posts changed records to database
     * @throws SQLException 
     */
    public void post() throws SQLException {

        getRecordSaver().post(getChangeTracker().getChangedRecords());

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

    /**
     * @return 
     * @throws SQLException 
     * 
     */
    public int executeQuery() throws SQLException {
        RecordSelector recordSelector = getRecordSelector();
        _records = recordSelector.executeQuery();

        // Sending this event on Event bus to inform all Observers
        if (_eventBus != null) {
            _eventBus.post(new QueryExecutedEvent(this));

        }

        return _records.size();

    }

    /**
    * @param detail
    * @param joinCondition
    */
    public void addDetailBlock(DataBlock detail, String joinCondition) {

        assert detail != null;
        assert joinCondition != null;

        if (_detailRelations == null)
            _detailRelations = Lists.newArrayList();

        Relation relation = new Relation(detail, joinCondition);
        _detailRelations.add(relation);
        detail.setMasterRelation(this, joinCondition);

        detail.getQueryDataSource().setWhereCondition(relation.getCondition()); // Where condition must be set on datasource query
    }

    /**
     * @param dataBlock
     */
    private void setMasterRelation(DataBlock dataBlock, String joinCondition) {

        observeOn(dataBlock); // Observe changes von data block
        _masterRelation = new Relation(dataBlock, joinCondition);

    }

    public List<Parameter> buildParameterList(List<String> columnNames) throws SQLException {
        List<Parameter> result = Lists.newArrayList();
        int index;

        Object value;
        int i = 0;
        for (String boundColumnName : columnNames) {
            index = getMetadataProvider().getColumnIndex(boundColumnName);
            value = getItem(index);
            result.add(new Parameter(boundColumnName, i++, value));
        }
        return result;
    }

    /**
     * Queries all details, if not coordination deffered 
     */
    public void queryMasterDetail(Relation relation) throws SQLException {

        assert relation != null;

        List<Parameter> queryParameters = buildParameterList(relation.getBoundColumnNames());
        DataBlock dataBlock = relation.getDataBlock();

        // set the parameter values
        if (queryParameters != null) {

            for (Parameter parameter : queryParameters) {
                dataBlock.setQueryParameter(parameter);
            }

            // Set the query parameters according to master
            if (!dataBlock.isDefferedCoordination())
                dataBlock.executeQuery(); // Requery the detailblock
        }
    }

    /**
     * Sets the paramaeter for parametrised queries e.g. Master-Detail 
     */
    private void setQueryParameter(Parameter parameter) throws SQLException {
        RecordSelector recordSelector = getRecordSelector();
        recordSelector.setParameter(parameter);

    }

    public void synchronizeSelection(SelectionChangedEvent selectionChange) throws SQLException {

        setRecord(selectionChange.getSelectionIndex());

    }

    @Subscribe
    public void handleSelectionChangedEvent(SelectionChangedEvent event) throws SQLException {
        synchronizeSelection(event);
    }

    /**
     * Jump to first selected record 
     * @throws SQLException 
     */
    public void firstRecord() throws SQLException {
        setRecord(0);

    }

    public void lastRecord() throws SQLException {
        if (_records != null && _records.size() > 0) {
            setRecord(_records.size() - 1);
        }
    }

    public void nextRecord() throws SQLException {
        if (_records != null && _currentRecord != null) {
            int recordCount = _records.size();
            int currentIndex = _records.indexOf(_currentRecord);
            currentIndex++;
            if (currentIndex + 1 <= recordCount)
                setRecord(currentIndex);
        }
    }

    private void setRecord(int index) throws SQLException {

        assert index >= 0;

        Record record = _records.get(index);

        assert record != null;

        if (_currentRecord != record || _currentRecord == null) {

            _currentRecord = record;

            for (Relation relation : _detailRelations) {
                queryMasterDetail(relation);
            }

            // Sending this event on Event bus to inform all Observers
            if (_eventBus != null) {
                _eventBus.post(new SelectionChangedEvent(index));

            }
        }

    }

    public MetadataProvider getMetadataProvider() throws SQLException {
        if (_metadataProvider == null)
            _metadataProvider = new MetadataProvider(getConnection(), getQueryDataSource());
        return _metadataProvider;
    }

    private QueryDataSource getQueryDataSource() {
        assert _queryDataSource != null : "Query data source must be specified";
        return _queryDataSource;
    }

    public List<Record> getRecords() {
        return _records;

    }

    public void observeOn(Object object) {
        getEventBus().register(object);
    }

    public boolean isDefferedCoordination() {
        return _defferedCoordination;
    }

    public void setDefferedCoordination(boolean defferedCoordination) {
        _defferedCoordination = defferedCoordination;
    }

    private EventBus getEventBus() {
        if (_eventBus == null)
            _eventBus = new EventBus();

        return _eventBus;
    }

    public RecordSelector getRecordSelector() throws SQLException {

        if (_recordSelector == null)
            _recordSelector = new RecordSelector(getConnection(), getMetadataProvider());
        return _recordSelector;
    }

    public Connection getConnection() {
        assert _connection != null;
        return _connection;
    }

    public RecordSaver getRecordSaver() throws SQLException {

        if (_recordSaver == null)
            _recordSaver = new RecordSaver(getConnection(), getMetadataProvider());

        return _recordSaver;
    }

    public ChangeTracker getChangeTracker() {
        if (_changeTracker == null)
            _changeTracker = new ChangeTracker();

        return _changeTracker;
    }

}
