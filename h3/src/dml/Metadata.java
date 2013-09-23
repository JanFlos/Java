/**
 * 
 */
package dml;

import h2.QueryDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import base.ListUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Metadata used to perform DML operations
 * 
 * @author Jan Flos
 */
public class Metadata {

    private final String         _tableName;
    private final List<String>   _columns;
    private List<String>         _returningColumns;
    private final List<String>   _readOnlyColumns;
    private final List<String>   _insertableColumns;
    private final Map<String, Integer> _columnTypes;
    private List<String>         _primaryKeyColumns;
    private final List<String>         _updateableColumns;
    private String                     _queryDataSource;

    /**
     * Constructor method
     */
    public Metadata(
                    String tableName,
                    List<String> tableColumns,
                    List<TableColumn> returningColumns,
                    List<String> readOnlyColumns,
                    List<String> primaryKeyColumns)
    {

        _tableName = tableName;
        _columns = tableColumns;
        _readOnlyColumns = readOnlyColumns;

        // Columns used to insert records
        _insertableColumns = ListUtils.difference(tableColumns, readOnlyColumns);

        // Returning column types
        _columnTypes = Maps.newHashMap();
        if (returningColumns != null) {
            _returningColumns = Lists.newArrayList();
            for (TableColumn col : returningColumns) {
                _returningColumns.add(col.getName());
                _columnTypes.put(col.getName(), col.getType());
            }
        }

        // Primary keys columns
        if (primaryKeyColumns == null && _returningColumns != null)
            _primaryKeyColumns = Lists.newArrayList(_returningColumns);
        else if (primaryKeyColumns != null)
            _primaryKeyColumns = primaryKeyColumns;

        // Construct updateable column List
        _updateableColumns = ListUtils.difference(_insertableColumns, _primaryKeyColumns);

    }

    /**
     * Constructor wrapper
     */
    public Metadata(
                    String tableName,
                    List<String> tableColumns,
                    List<TableColumn> returningColumns,
                    List<String> readOnlyColumns)
    {
        this(tableName, tableColumns, returningColumns, readOnlyColumns, null);
    }

    /**
     * Constructor wrapper
     */
    public Metadata(
                    String tableName,
                    List<String> tableColumns,
                    List<TableColumn> returningColumns)
    {
        this(tableName, tableColumns, returningColumns, null, null);
    }

    /**
     * Constructor wrapper
     */
    public Metadata(String tableName, List<String> tableColumns) {
        this(tableName, tableColumns, null, null, null);
    }

    /**
     * @param queryDataSource
     * @param connection 
     * @throws SQLException 
     */
    public Metadata(Connection connection, QueryDataSource queryDataSource) throws SQLException {

        assert queryDataSource != null;
        assert queryDataSource.getDMLTarget() != null;

        _queryDataSource = queryDataSource.getQueryDataSource();
        List<TableColumn> sourceTableColumns = findTableColumns(connection, _queryDataSource);        // Find all columns for data source query

        _tableName = queryDataSource.getDMLTarget();
        _columns = getColumnNames(sourceTableColumns);
        _columnTypes = getColumnTypes(sourceTableColumns);

        List<TableColumn> targetTableColumns = findTableColumns(connection, _tableName);        // Find all columns for data source query

        _insertableColumns = getColumnNames(targetTableColumns);
        _readOnlyColumns = ListUtils.difference(_columns, _insertableColumns);
        _primaryKeyColumns = findPrimaryKeyColumns(connection, _tableName);                         // Find primary key columns in data dictionary
        _returningColumns = findReturningColumns(_primaryKeyColumns);

        _updateableColumns = ListUtils.difference(_insertableColumns, _primaryKeyColumns);


    }

    /**
     * @param sourceTableColumns
     * @return
     */
    private Map<String, Integer> getColumnTypes(List<TableColumn> tableColumns) {
        Map<String, Integer> result = Maps.newHashMap();
        for (TableColumn column : tableColumns) {
            result.put(column.getName(), column.getType());
        }
        return result;
    }

    /**
     * @param tableColumns
     * @return
     */
    private List<String> getColumnNames(List<TableColumn> tableColumns) {
        List<String> result = Lists.newArrayList();
        for (TableColumn column : tableColumns) {
            result.add(column.getName());
        }
        return result;
    }

    /**
     * @param tableName
     * @return
     * @throws SQLException 
     */
    public List<String> findPrimaryKeyColumns(Connection connection, String tableName) throws SQLException {

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
     * @param tableColumns
     * @param tableName
     * @return
     */
    public List<String> findReadOnlyColumns(List<String> tableColumns, String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param primaryKeyColumns
     * @return
     */
    public List<String> findReturningColumns(List<String> primaryKeyColumns) {
        
        List<String> result = Lists.newArrayList();
        for (String column : primaryKeyColumns) {
            if (column.toLowerCase().equals("id"))
                result.add(column);
        }
        
        return result;
    }

    /**
     * @param queryDataSource
     * @return
     * @throws SQLException 
     */
    private List<TableColumn> findTableColumns(Connection connection, String queryDataSource) throws SQLException {

        List<TableColumn> result = Lists.newArrayList();

        String sqlText = String.format("select * from (%s) where rownum = 0", queryDataSource);
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
            result.add(new TableColumn(columnName, columnType, displaySize));
        }

        command.close();

        return result;
    }

    /**
     * Getters/Setters
     */
    public String getTableName() {
        return _tableName;
    }

    public List<String> getTableColumns() {
        return _columns;
    }

    public List<String> getReadOnlyColumns() {
        return _readOnlyColumns;
    }

    public List<String> getReturningColumns() {
        return _returningColumns;
    }

    public List<String> getInsertableColumns() {
        return _insertableColumns;
    }

    public List<String> getUpdateableColumns() {
        return _updateableColumns;
    }

    public int getColumnType(String returningColumn) {
        return _columnTypes.get(returningColumn);
    }

    public List<String> getPrimaryKeyColumns() {
        return _primaryKeyColumns;
    }

    public int getColumnIndex(String column) {
        return _columns.indexOf(column);
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
        return _columns.size();
    }

    /**
     * @return
     */
    public String getDataSource() {
        return _queryDataSource;
    }

}
