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

    private final String             _queryDataSource;
    private DataSourceTypeEnum _datasourceType;
    private String             _dmlTarget;

    public QueryDataSource(String dataSource, String dmlTarget) {

        _datasourceType = DataSourceTypeEnum.FROM_QUERY_CLAUSE;
        _queryDataSource = dataSource;
        _dmlTarget = dmlTarget;
        if (dataSource.matches("^[a-zA-Z]\\S*$")) {
            _datasourceType = DataSourceTypeEnum.TABLE;
            _dmlTarget = _queryDataSource;
        }
        
        if (_datasourceType == DataSourceTypeEnum.TABLE && _dmlTarget == null)
            _dmlTarget = _queryDataSource;

    }

    public QueryDataSource(String dataSource) {
        this(dataSource, null);
    }

    public String getDMLTarget() {
        return _dmlTarget;
    }

    public String getQueryDataSource() {
        if (_datasourceType == DataSourceTypeEnum.FROM_QUERY_CLAUSE)
            return _queryDataSource;
        return String.format("select * from %s", _queryDataSource);
    }

}
