package cz.robotron.rf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.robotron.rf.dml.MetadataProvider;
import cz.robotron.rf.dml.Parameter;
import cz.robotron.rf.dml.Record;
import cz.robotron.rf.dml.RecordSaver;
import cz.robotron.rf.dml.RecordSelector;
import cz.robotron.rf.events.ContentChangedEvent;
import cz.robotron.rf.events.SelectionChangedEvent;
import cz.robotron.rf.events.SortOrderChangedEvent;

/**
 * @author Jan Flos
 * 
 */
public class DataBlock {

    private QueryDataSource   _queryDataSource;
    private ChangeTracker     _changeTracker;
    private MetadataProvider  _metadataProvider;
    private List<Record>      _records;
    private Record            _currentRecord;
    private RecordSaver       _recordSaver;
    private RecordSelector    _recordSelector;
    private List<Relation>    _detailRelations;
    private Relation          _masterRelation;
    private EventBus          _eventBus;

    private static Connection _connection;

    private boolean           _defferedCoordination;
    private boolean           _enterQueryState;

    //private List<Parameter>   _queryParameters;

    @Inject
    public DataBlock(Connection connection) throws SQLException {
        _connection = connection;
    }

    /**
     * Sets the Table name or the query used to load data
     */
    private void setDataSource(
        String dataSource,
        String dmlTarget) throws SQLException
    {
        _queryDataSource = new QueryDataSource(dataSource, dmlTarget);

        _metadataProvider = null;
        _records = null;
        _currentRecord = null;
        _recordSaver = null;
        _recordSelector = null;

    }

    public void setDataSource(String dataSource) throws SQLException {
        setDataSource(dataSource, null);
    }

    public static DataBlock createDataBlock(Connection connection, String dataSource) throws SQLException {
        DataBlock result = new DataBlock(connection);
        result.setDataSource(dataSource);
        return result;
    }

    /**
     * Creates a new record in the block
     * @throws SQLException 
     */

    public void createRecord() throws SQLException {

        assert _enterQueryState != true;

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

        if (!_enterQueryState)
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

    /*
        public static DataBlock createDataBlock(ApplicationContext appContext, String queryDataSource, String dmlTarget) throws SQLException {
            DataBlock result = new DataBlock();
            result.setDataSource(queryDataSource, dmlTarget);
            appContext.getEventBus().register(result); // Register the datablock on event bus

            return result;
        }

        public static DataBlock createDataBlock(ApplicationContext appContext, String tableName) throws SQLException {
            DataBlock result = new DataBlock();
            result.setDataSource(tableName);
            return result;
        }
    */
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

        _records = queryRecords();

        // Sending this event on Event bus to inform all Observers
        if (_eventBus != null) {
            _eventBus.post(new ContentChangedEvent(this));

        }

        // Jump to first record when enter query started
        if (_records.size() > 0 && _enterQueryState)
            firstRecord();

        _enterQueryState = false;

        return _records.size();

    }

    public List<Record> queryRecords() throws SQLException {

        RecordSelector recordSelector = getRecordSelector();

        // Handle query by Example mode
        if (_enterQueryState) {
            List<Parameter> parameters = buildQueryByExampleParameters();
            recordSelector.setOneTimeParameters(parameters);
        }

        return recordSelector.executeQuery();


    }

    private List<Parameter> buildQueryByExampleParameters() {
        List<Parameter> parameters = Lists.newArrayList();

        Object[] data = _currentRecord.getData();
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                String name = _metadataProvider.getColumnName(i);
                parameters.add(new Parameter(name, i, data[i]));
            }
        }
        return parameters;
    }

    private List<Relation> getRelations() {
        if (_detailRelations == null)
            _detailRelations = Lists.newArrayList();
        return _detailRelations;
    }

    /**
    * @param detail
    * @param joinCondition
    */

    public void addDetailDataBlock(DataBlock detail, String joinCondition) {

        assert detail != null;
        assert joinCondition != null;

        List<Relation> relations = getRelations();

        Relation relation = new Relation(detail, joinCondition);
        relations.add(relation);
        detail.setMasterRelation(this, joinCondition);

        QueryDataSource dataSource = detail.getQueryDataSource();
        dataSource.setWhereClause(relation.getCondition()); // Where condition must be set on datasource query
    }

    private void setMasterRelation(DataBlock dataBlock) {
        setMasterRelation(dataBlock, null);
    }

    /**
     * @param dataBlock
     */

    public void setMasterRelation(DataBlock dataBlock, String joinCondition) {

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

            dataBlock.setQueryParameters(queryParameters);

            // Set the query parameters according to master
            if (!dataBlock.isDefferedCoordination())
                dataBlock.executeQuery(); // Requery the detailblock
        }
    }

    public void synchronizeSelection(SelectionChangedEvent selectionChange) throws SQLException {

        setRecord(selectionChange.getSelectionIndex());

    }

    @Subscribe
    public void handleSelectionChangedEvent(SelectionChangedEvent event) throws SQLException {
        synchronizeSelection(event);
    }

    @Subscribe
    public void handleSortOrderChangedEvent(SortOrderChangedEvent event) throws SQLException {
        setOrderBy(event.getSortedColumn());
        executeQuery();
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

            if (_detailRelations != null) {
                for (Relation relation : _detailRelations) {
                    queryMasterDetail(relation);
                }
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

    public QueryDataSource getQueryDataSource() {
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
            _recordSelector = new RecordSelector(getConnection(), _queryDataSource.getCanonizedDataSource());
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

    public void setOrderBy(String... sortedColumn) {
        assert _queryDataSource != null;
        _queryDataSource.setOrderByClause(sortedColumn);
    }

    public static DataBlock createDataBlock(IEclipseContext context, String dataSource) throws SQLException {
        DataBlock result = ContextInjectionFactory.make(DataBlock.class, context);
        result.setDataSource(dataSource);

        return result;
    }

    public String getName() {
        String result = _queryDataSource.getDMLTarget();
        if (result == null)
            result = _queryDataSource.getDataSource();
        return result;
    }

    /*
     * Try to find a record, if not the create
     */

    public Record findFirstRecord(String praedikat, Object... values) throws SQLException {

        assert praedikat != null;

        RecordFinder recordFinder = new RecordFinder(_connection, getRecordSelector());
        return recordFinder.findFirstRecord(praedikat, values);

    }

    public String getQueryDataSourceText() {
        return _queryDataSource.getDataSource();
    }

    public DataBlock addDetailDataBlock(String dataSource) throws SQLException {

        DataBlock detail = createDataBlock(_connection, dataSource);

        List<Relation> relations = getRelations();

        Relation relation = new Relation(detail);
        _detailRelations.add(relation);
        detail.setMasterRelation(this);

        return detail;

    }

    public DataBlock addDetailDataBlock(String dataSource, String joinCondition) throws SQLException {

        DataBlock detail = createDataBlock(_connection, dataSource);
        addDetailDataBlock(detail, joinCondition);
        return detail;

    }

    public void setQueryParameters(List<Parameter> queryParameters) throws SQLException {
        _recordSelector.setQueryParameters(queryParameters);

    }

    public void tryCreateRecord(String praedikat, Object... values) throws SQLException {


        enterQuery();
        setItem(praedikat, values[0] );
        List<Record> records = queryRecords();

        if (records.size() == 0)
            createRecord();
        else
            _currentRecord = records.get(0);

        _enterQueryState = false;
    }

    /**
     * Put the Datablock into enter query state subsequent set on items means setting of the query parameters
     * @throws SQLException 
     */
    public void enterQuery() throws SQLException {
        _enterQueryState = true;
        Object[] data = new Object[getMetadataProvider().getColumnCount()];
        Record record = Record.newQueriedRecord(data);
        _currentRecord = record;
    }

    public Record getCurrentRecord() {
        return _currentRecord;
    }
}
