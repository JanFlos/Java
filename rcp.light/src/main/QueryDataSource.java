/**
 * 
 */
package main;

import com.google.common.base.CharMatcher;
import constants.DataSourceTypeEnum;

/**
 * @author Jan Flos
 * 
 */
public class QueryDataSource {

    private final String       _queryDataSource;
    private DataSourceTypeEnum _datasourceType;
    private String             _dmlTarget;
    private String             _whereClause;
    private Object             _orderByClause;

    public QueryDataSource(String dataSource, String dmlTarget) {

        // default is awaitet that datasource is a query
        _datasourceType = DataSourceTypeEnum.FROM_QUERY_CLAUSE;
        _queryDataSource = dataSource;
        _dmlTarget = dmlTarget;

        // if datasource is a simple identifier then this is a table name
        if (dataSource.matches("^[a-zA-Z]\\S*$")) {
            _datasourceType = DataSourceTypeEnum.TABLE;
            _dmlTarget = _queryDataSource;

            // Fill DML Target    
            if (_dmlTarget == null)
                _dmlTarget = _queryDataSource;
        }

        // if parameter defined then make where clause

        if (_datasourceType == DataSourceTypeEnum.FROM_QUERY_CLAUSE)
            assert (_dmlTarget != null) : "DML Target not specified";

    }

    public QueryDataSource(String dataSource) {
        this(dataSource, null);
    }

    public String getDMLTarget() {
        return _dmlTarget;
    }

    public String getDataSource() {
        assert _datasourceType != null;
        String datasource = _datasourceType != DataSourceTypeEnum.FROM_QUERY_CLAUSE ? _queryDataSource : "(" + _queryDataSource + ")";
        datasource = String.format("select * from %s%s%s", datasource, getWhereClause(), getOrderByClause());
        return datasource;
    }

    private String getWhereClause() {
        if (_whereClause != null)
            return String.format(" where %s", _whereClause);
        return "";
    }

    private String getOrderByClause() {
        if (_orderByClause != null)
            return String.format(" order by %s", _orderByClause);
        return "";
    }

    public int getParameterCount() {
        return CharMatcher.is(':').countIn(_whereClause);
    }


    public void setWhereClause(String condition) {
        _whereClause = condition.replaceAll("\\:\\w[\\w\\d]+", "?");
    }

    public void setOrderByClause(String... sortedColumns) {

        String orderByClause = "";

        for (String sortedColumn : sortedColumns) {
            
            assert sortedColumn.matches("\\w[\\w\\d]+(\\s+asc|\\s+desc)?");

            if (orderByClause != "")
                orderByClause = orderByClause + ", ";
            orderByClause = orderByClause + sortedColumn;
        }

        _orderByClause = orderByClause;

    }

}

