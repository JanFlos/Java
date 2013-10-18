/**
 * 
 */
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

public class DataBlockFactory {

    private final Connection _connection;

    public DataBlockFactory(Connection connection) {
        _connection = connection;
    }

    public IDataBlock createDataBlock(String dataSource) throws SQLException {
        return DataBlock.createDataBlock(_connection, dataSource);
    }

}

/**
 * @author Jan Flos
 * 
 */
class DataBlock implements IDataBlock {

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

    @Override
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
    @Override
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
    @Override
    public void setItem(String columnName, Object value) {
        assert _metadataProvider != null;

        int columnIndex = _metadataProvider.getColumnIndex(columnName);
        setItem(columnIndex, value);

    }

    /**
     * Changes the current record item value
     */
    @Override
    public void setItem(int columnIndex, Object value) {
        assert _currentRecord != null;

        _currentRecord.setValue(columnIndex, value);
        getChangeTracker().recordUpdated(_currentRecord);
    }

    @Override
    public Object getItem(int columnIndex) {
        assert _currentRecord != null;

        return _currentRecord.getValue(columnIndex);

    }

    /**
     * Posts changed records to database
     * @throws SQLException 
     */
    @Override
    public void post() throws SQLException {

        getRecordSaver().post(getChangeTracker().getChangedRecords());

    }

    /**
     * Commits all changes to the database
     */
    @Override
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
    @Override
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
    @Override
    public int executeQuery() throws SQLException {

        RecordSelector recordSelector = getRecordSelector();
        _records = recordSelector.executeQuery();

        // Sending this event on Event bus to inform all Observers
        if (_eventBus != null) {
            _eventBus.post(new ContentChangedEvent(this));

        }

        return _records.size();

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
    @Override
    public void addDetailDataBlock(IDataBlock detail, String joinCondition) {

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
    @Override
    public void setMasterRelation(DataBlock dataBlock, String joinCondition) {

        observeOn(dataBlock); // Observe changes von data block
        _masterRelation = new Relation(dataBlock, joinCondition);

    }

    @Override
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
    @Override
    public void queryMasterDetail(Relation relation) throws SQLException {

        assert relation != null;

        List<Parameter> queryParameters = buildParameterList(relation.getBoundColumnNames());
        IDataBlock dataBlock = relation.getDataBlock();

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
    @Override
    public void setQueryParameter(Parameter parameter) throws SQLException {
        RecordSelector recordSelector = getRecordSelector();
        recordSelector.setParameter(parameter);

    }

    @Override
    public void synchronizeSelection(SelectionChangedEvent selectionChange) throws SQLException {

        setRecord(selectionChange.getSelectionIndex());

    }

    @Override
    @Subscribe
    public void handleSelectionChangedEvent(SelectionChangedEvent event) throws SQLException {
        synchronizeSelection(event);
    }

    @Override
    @Subscribe
    public void handleSortOrderChangedEvent(SortOrderChangedEvent event) throws SQLException {
        setOrderBy(event.getSortedColumn());
        executeQuery();
    }

    /**
     * Jump to first selected record 
     * @throws SQLException 
     */
    @Override
    public void firstRecord() throws SQLException {
        setRecord(0);

    }

    @Override
    public void lastRecord() throws SQLException {
        if (_records != null && _records.size() > 0) {
            setRecord(_records.size() - 1);
        }
    }

    @Override
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

    @Override
    public MetadataProvider getMetadataProvider() throws SQLException {
        if (_metadataProvider == null)
            _metadataProvider = new MetadataProvider(getConnection(), getQueryDataSource());
        return _metadataProvider;
    }

    @Override
    public QueryDataSource getQueryDataSource() {
        assert _queryDataSource != null : "Query data source must be specified";
        return _queryDataSource;
    }

    @Override
    public List<Record> getRecords() {
        return _records;

    }

    @Override
    public void observeOn(Object object) {
        getEventBus().register(object);
    }

    @Override
    public boolean isDefferedCoordination() {
        return _defferedCoordination;
    }

    @Override
    public void setDefferedCoordination(boolean defferedCoordination) {
        _defferedCoordination = defferedCoordination;
    }

    private EventBus getEventBus() {
        if (_eventBus == null)
            _eventBus = new EventBus();

        return _eventBus;
    }

    @Override
    public RecordSelector getRecordSelector() throws SQLException {

        if (_recordSelector == null)
            _recordSelector = new RecordSelector(getConnection(), _queryDataSource.getCanonizedDataSource());
        return _recordSelector;
    }

    @Override
    public Connection getConnection() {
        assert _connection != null;
        return _connection;
    }

    @Override
    public RecordSaver getRecordSaver() throws SQLException {

        if (_recordSaver == null)
            _recordSaver = new RecordSaver(getConnection(), getMetadataProvider());

        return _recordSaver;
    }

    @Override
    public ChangeTracker getChangeTracker() {
        if (_changeTracker == null)
            _changeTracker = new ChangeTracker();

        return _changeTracker;
    }

    @Override
    public void setOrderBy(String... sortedColumn) {
        assert _queryDataSource != null;
        _queryDataSource.setOrderByClause(sortedColumn);
    }

    public static DataBlock createDataBlock(IEclipseContext context, String dataSource) throws SQLException {
        DataBlock result = ContextInjectionFactory.make(DataBlock.class, context);
        result.setDataSource(dataSource);

        return result;
    }

    @Override
    public String getName() {
        String result = _queryDataSource.getDMLTarget();
        if (result == null)
            result = _queryDataSource.getDataSource();
        return result;
    }

    /*
     * Try to find a record, if not the create
     */
    @Override
    public void findOrCreate(String praedikat, Object... values) throws SQLException {
        RecordSelector recordSelector = getRecordSelector();
        recordSelector.setOneTimeWhere(praedikat, values);
        List<Record> records = recordSelector.executeQuery();

    }

    @Override
    public String getQueryDataSourceText() {
        return _queryDataSource.getDataSource();
    }

    @Override
    public IDataBlock addDetailDataBlock(String dataSource) throws SQLException {

        DataBlock detail = createDataBlock(_connection, dataSource);

        List<Relation> relations = getRelations();

        Relation relation = new Relation(detail);
        _detailRelations.add(relation);
        detail.setMasterRelation(this);

        return detail;

    }

    @Override
    public IDataBlock addDetailDataBlock(String dataSource, String joinCondition) throws SQLException {

        DataBlock detail = createDataBlock(_connection, dataSource);
        addDetailDataBlock(detail, joinCondition);
        return detail;

    }    
}
