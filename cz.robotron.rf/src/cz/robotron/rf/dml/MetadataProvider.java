/**
 * 
 */
package cz.robotron.rf.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cz.robotron.rf.CollectionUtils;
import cz.robotron.rf.DataBlockColumn;
import cz.robotron.rf.QueryDataSource;

/**
 * Metadata used to perform DML operations
 * 
 * @author Jan Flos
 */
public class MetadataProvider {

    private final String               _dmlTargetTableName;
    private final List<String>         _allColumnNames;
    private List<String>               _returningColumnNames  = null;
    private List<String>               _readOnlyColumnNames;
    private List<String>               _insertableColumnNames = null;
    private final Map<String, Integer> _columnTypes;
    private List<String>               _primaryKeyColumnsNames;
    private List<String>               _updateableColumnsNames = null;
    private String                     _queryDataSource;
    private List<DataBlockColumn>      _allColumns;

    /**
     * Constructor method
     */
    public MetadataProvider(
                            String tableName,
                            List<String> tableColumns,
                            List<DataBlockColumn> returningColumns,
                            List<String> readOnlyColumns,
                            List<String> primaryKeyColumns)
    {

        _dmlTargetTableName = tableName;
        _allColumnNames = tableColumns;
        _readOnlyColumnNames = readOnlyColumns;

        // Columns used to insert records
        _insertableColumnNames = CollectionUtils.difference(tableColumns, readOnlyColumns);

        // Returning column types
        _columnTypes = Maps.newHashMap();
        if (returningColumns != null) {
            _returningColumnNames = Lists.newArrayList();
            for (DataBlockColumn col : returningColumns) {
                _returningColumnNames.add(col.getName());
                _columnTypes.put(col.getName(), col.getType());
            }
        }

        // Primary keys columns
        if (primaryKeyColumns == null && _returningColumnNames != null)
            _primaryKeyColumnsNames = Lists.newArrayList(_returningColumnNames);
        else if (primaryKeyColumns != null)
            _primaryKeyColumnsNames = primaryKeyColumns;

        // Construct updateable column List
        _updateableColumnsNames = CollectionUtils.difference(_insertableColumnNames, _primaryKeyColumnsNames);

    }

    /**
     * Constructor wrapper
     */
    public MetadataProvider(
                            String tableName,
                            List<String> tableColumns,
                            List<DataBlockColumn> returningColumns,
                            List<String> readOnlyColumns)
    {
        this(tableName, tableColumns, returningColumns, readOnlyColumns, null);
    }

    /**
     * Constructor wrapper
     */
    public MetadataProvider(
                            String tableName,
                            List<String> tableColumns,
                            List<DataBlockColumn> returningColumns)
    {
        this(tableName, tableColumns, returningColumns, null, null);
    }

    /**
     * Constructor wrapper
     */
    public MetadataProvider(String tableName, List<String> tableColumns) {
        this(tableName, tableColumns, null, null, null);
    }

    /**
     * @param queryDataSource
     * @param connection 
     * @throws SQLException 
     */
    public MetadataProvider(Connection connection, QueryDataSource queryDataSource) throws SQLException {

        assert connection != null;
        assert queryDataSource != null;
        assert queryDataSource.getDMLTarget() != null;

        _queryDataSource = queryDataSource.getCanonizedDataSource();
        _allColumns = findTableColumns(connection, _queryDataSource);

        _allColumnNames = getColumnNames(_allColumns);
        _columnTypes = getColumnTypes(_allColumns);

        _dmlTargetTableName = queryDataSource.getDMLTarget();

        if (_dmlTargetTableName != null) {

            List<DataBlockColumn> targetTableColumns = findTableColumns(connection, _dmlTargetTableName);        // Find all columns for data source query
            _insertableColumnNames = getColumnNames(targetTableColumns);
            _readOnlyColumnNames = CollectionUtils.difference(_allColumnNames, _insertableColumnNames);
            _primaryKeyColumnsNames = findPrimaryKeyColumnNames(connection, _dmlTargetTableName);                         // Find primary key columns in data dictionary
            _returningColumnNames = findReturningColumnNames(_primaryKeyColumnsNames);
            _updateableColumnsNames = CollectionUtils.difference(_insertableColumnNames, _primaryKeyColumnsNames);

        } else {
            _readOnlyColumnNames = Lists.newArrayList(_allColumnNames);
        }

    }

    /**
     * @param sourceTableColumns
     * @return
     */
    private Map<String, Integer> getColumnTypes(List<DataBlockColumn> tableColumns) {
        Map<String, Integer> result = Maps.newHashMap();
        for (DataBlockColumn column : tableColumns) {
            result.put(column.getName(), column.getType());
        }
        return result;
    }

    /**
     * @param tableColumns
     * @return
     */
    private List<String> getColumnNames(List<DataBlockColumn> tableColumns) {
        List<String> result = Lists.newArrayList();
        for (DataBlockColumn column : tableColumns) {
            result.add(column.getName());
        }
        return result;
    }

    /**
     * @param tableName
     * @return
     * @throws SQLException 
     */
    public List<String> findPrimaryKeyColumnNames(Connection connection, String tableName) throws SQLException {

        List<String> result = Lists.newArrayList();

        PreparedStatement command = connection.prepareStatement(getPrimaryKeyQueryString(), ResultSet.FETCH_FORWARD);
        command.setString(1, tableName);
        ResultSet resultSet = command.executeQuery();

        String columnName = null;
        while (resultSet.next())
        {
            columnName = resultSet.getString("column_name");
            result.add(columnName);
        }

        resultSet.close();
        return result;
    }

    /**
     * @param primaryKeyColumns
     * @return
     */
    public List<String> findReturningColumnNames(List<String> primaryKeyColumns) {

        List<String> result = Lists.newArrayList();
        for (String column : primaryKeyColumns) {
            if (column.toLowerCase().equals("id"))
                result.add(column);
        }

        return result;
    }

    /**
     * @param queryDataSource.
     * @return
     * @throws SQLException 
     */
    private List<DataBlockColumn> findTableColumns(Connection connection, String queryDataSource) throws SQLException {

        assert connection != null;
        assert queryDataSource != null;

        List<DataBlockColumn> result = Lists.newArrayList();

        String sqlText = queryDataSource.replaceAll("\\?", "null");
        sqlText = String.format("select * from (%s) where rownum = 0", sqlText);
        
        PreparedStatement command = connection.prepareStatement(sqlText);

        ResultSet resultSet = command.executeQuery();

        // Get the metadata
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        String columnName;
        int columnType;
        int displaySize;
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            columnName = resultSetMetaData.getColumnName(i);
            columnType = resultSetMetaData.getColumnType(i);
            displaySize = resultSetMetaData.getColumnDisplaySize(i);
            result.add(new DataBlockColumn(columnName, columnType, displaySize));
        }

        command.close();

        return result;
    }

    /**
     * Getters/Setters
     */
    public String getDmlTargetTableName() {
        return _dmlTargetTableName;
    }

    public List<String> getTableColumnNames() {
        return _allColumnNames;
    }

    public List<String> getReadOnlyColumnNames() {
        return _readOnlyColumnNames;
    }

    public List<String> getReturningColumnNames() {
        return _returningColumnNames;
    }

    public List<String> getInsertableColumnNames() {
        return _insertableColumnNames;
    }

    public List<String> getUpdateableColumnNames() {
        return _updateableColumnsNames;
    }

    public int getColumnType(String returningColumn) {
        return _columnTypes.get(returningColumn);
    }

    public List<String> getPrimaryKeyColumnNames() {
        return _primaryKeyColumnsNames;
    }

    public int getColumnIndex(String column) {
        return _allColumnNames.indexOf(column);
    }

    private String getPrimaryKeyQueryString() {

        return "SELECT lower(c.column_name) column_name" +
            "   FROM user_constraints cons, user_cons_columns c" +
            "   WHERE c.table_name = ?" +
            "     AND cons.constraint_type = 'P'" +
            "     AND cons.constraint_name = c.constraint_name" +
            "     AND cons.owner = c.owner" +
            "   ORDER BY c.position";

    }

    /**
     * @return
     */
    public int getColumnCount() {
        return _allColumnNames.size();
    }

    /**
     * @return
     */
    private String getDataSource() {
        return _queryDataSource;
    }

    public List<DataBlockColumn> getColumns() {
        return _allColumns;
    }

    public String getColumnName(int i) {
        return _allColumnNames.get(i);
    }

}
