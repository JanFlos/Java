package cz.robotron.rf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import cz.robotron.rf.dml.MetadataProvider;
import cz.robotron.rf.dml.Parameter;
import cz.robotron.rf.dml.Record;
import cz.robotron.rf.dml.RecordSaver;
import cz.robotron.rf.dml.RecordSelector;
import cz.robotron.rf.events.SelectionChangedEvent;
import cz.robotron.rf.events.SortOrderChangedEvent;

public interface IDataBlock {

    public abstract void setDataSource(String dataSource) throws SQLException;

    /**
     * Creates a new record in the block
     * @throws SQLException 
     */
    public abstract void createRecord() throws SQLException;

    /**
     * Changes the current record item value
     */
    public abstract void setItem(String columnName, Object value);

    /**
     * Changes the current record item value
     */
    public abstract void setItem(int columnIndex, Object value);

    public abstract Object getItem(int columnIndex);

    /**
     * Posts changed records to database
     * @throws SQLException 
     */
    public abstract void post() throws SQLException;

    /**
     * Commits all changes to the database
     */
    public abstract void commit();

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
    public abstract void setItems(Object... objects);

    /**
     * @return 
     * @throws SQLException 
     * 
     */
    public abstract int executeQuery() throws SQLException;

    /**
    * @param detail
    * @param joinCondition
    */

    public abstract List<Parameter> buildParameterList(List<String> columnNames) throws SQLException;

    /**
     * Queries all details, if not coordination deffered 
     */
    public abstract void queryMasterDetail(Relation relation) throws SQLException;

    public abstract void synchronizeSelection(SelectionChangedEvent selectionChange) throws SQLException;

    public abstract void handleSelectionChangedEvent(SelectionChangedEvent event) throws SQLException;

    public abstract void handleSortOrderChangedEvent(SortOrderChangedEvent event) throws SQLException;

    /**
     * Jump to first selected record 
     * @throws SQLException 
     */
    public abstract void firstRecord() throws SQLException;

    public abstract void lastRecord() throws SQLException;

    public abstract void nextRecord() throws SQLException;

    public abstract MetadataProvider getMetadataProvider() throws SQLException;

    public abstract List<Record> getRecords();

    public abstract void observeOn(Object object);

    public abstract boolean isDefferedCoordination();

    public abstract void setDefferedCoordination(boolean defferedCoordination);

    public abstract RecordSelector getRecordSelector() throws SQLException;

    public abstract Connection getConnection();

    public abstract RecordSaver getRecordSaver() throws SQLException;

    public abstract ChangeTracker getChangeTracker();

    public abstract void setOrderBy(String... sortedColumn);

    public abstract String getName();

    /*
     * Try to find a record, if not the create
     */
    public abstract void findOrCreate(String praedikat, Object... values) throws SQLException;

    public abstract String getQueryDataSourceText();

    public abstract IDataBlock addDetailDataBlock(String dataSource) throws SQLException;

    public abstract void setQueryParameter(Parameter parameter) throws SQLException;


    public abstract QueryDataSource getQueryDataSource();

    abstract void setMasterRelation(DataBlock dataBlock, String joinCondition);

    void addDetailDataBlock(IDataBlock detail, String joinCondition);

    IDataBlock addDetailDataBlock(String dataSource, String joinCondition) throws SQLException;

}
