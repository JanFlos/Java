/**
 * 
 */
package h2;

import constants.DataSourceTypeEnum;

/**
 * @author Jan Flos
 * 
 */
public class QueryDataSource {

    private final String       _queryDataSource;
    private DataSourceTypeEnum _datasourceType;
    private String             _dmlTarget;
    private String             _parameterClause;

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

    public String getQueryDataSource() {
        assert _datasourceType != null;

        if (_datasourceType == DataSourceTypeEnum.FROM_QUERY_CLAUSE) {

            if (_parameterClause == null) {
                return _queryDataSource;
            }

            return String.format("select * from (%s) where %s", _queryDataSource, getParameterClause());

        }

        if (_parameterClause == null) {
            return String.format("select * from %s", _queryDataSource);
        }

        return String.format("select * from %s where %s", _queryDataSource, getParameterClause());

    }

    private String getParameterClause() {
        return _parameterClause;
    }


    public void setWhereCondition(String condition) {
        _parameterClause = condition.replaceAll("\\:\\w[\\w\\d]+", "?");
    }


}

