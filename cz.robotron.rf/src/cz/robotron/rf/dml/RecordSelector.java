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
import org.apache.log4j.Logger;
import com.google.common.collect.Lists;

/**
 * @author Jan Flos
 * 
 */
public class RecordSelector {

    private PreparedStatement _command;
    private Integer           _columnCount;
    private final Connection  _connection;
    private String            _dataSource;
    private String            _preparedDataSource;

    static Logger             log = Logger.getLogger(RecordSelector.class);
    private List<Parameter>   _queryParameters;
    private List<Parameter>   _oneTimeQueryParameters;
    private boolean           _resetQueryParameters;

    public List<Parameter> getQueryParameters() {
        return _queryParameters;
    }

    /**
    * @param metadata
    * @throws SQLException
    */
    public RecordSelector(Connection connection, String dataSource) throws SQLException {
        _connection = connection;
        _dataSource = dataSource;

    }

    public RecordSelector(Connection connection) throws SQLException {
        this(connection, null);
    }

    private void prepareCommand(String dataSource) throws SQLException {

        assert _connection != null;
        assert dataSource != null;

        if (_command != null)
            _command.close();

        _command = _connection.prepareStatement(dataSource, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        _columnCount = null;
        _preparedDataSource = dataSource;
        _resetQueryParameters = true;

    }

    /**
     * Reads the data into the cache
     * 
     * @throws SQLException
     */
    public List<Record> executeQuery() throws SQLException {

        String dataSource = getDataSource();

        if ((_preparedDataSource == null) || !_preparedDataSource.equals(dataSource)) {
            prepareCommand(dataSource);
        }

        // Set the parameters of the query
        if (hasResetQueryParameters())
            resetQueryParameters();

        // Let the one Time parameter any more
        if (hasOneTimeParameters()) {
            _oneTimeQueryParameters = null;
        }

        List<Record> result = Lists.newArrayList();
        assert _command != null;

        ResultSet rs = _command.executeQuery();
        ResultSetMetaData metaData = _command.getMetaData();
        _columnCount = metaData.getColumnCount();

        Object[] data = null;
        while (rs.next()) {

            data = new Object[_columnCount];
            for (int i = 0; i < _columnCount; i++) {
                data[i] = rs.getObject(i + 1);
            }
            result.add(new Record(data));

        }

        return result;
    }

    private boolean hasOneTimeParameters() {
        return _oneTimeQueryParameters != null;
    }

    private boolean hasResetQueryParameters() {
        return _resetQueryParameters && (_queryParameters != null || _oneTimeQueryParameters != null);
    }

    private void resetQueryParameters() throws SQLException {

        assert _command != null;

        int index = 0;

        if (_queryParameters != null) {

            Object value;

            for (Parameter parameter : _queryParameters) {
                index++;
                _command.setObject(index, parameter.getValue());
            }
            _resetQueryParameters = false;

        }

        if (_oneTimeQueryParameters != null) {

            for (Parameter parameter : _oneTimeQueryParameters) {
                index++;
                _command.setObject(index, parameter.getValue());
            }

            _resetQueryParameters = false;

        }

    }

    public void setDataSource(String dataSource) {
        if ((_dataSource == null) || (!_dataSource.equals(dataSource)))
            _dataSource = dataSource;
    }

    public int getParameterCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setQueryParameters(List<Parameter> queryParameters) throws SQLException {
        _queryParameters = queryParameters;
        _resetQueryParameters = true;
    }

    public String getDataSource() {

        return (!hasOneTimeParameters() ? _dataSource : String.format("select * from (%s) where %s", _dataSource, getOneTimeWhere()));
    }

    private Object getOneTimeWhere() {
        String result = "";

        if (hasOneTimeParameters()) {
            for (int i = 0; i < _oneTimeQueryParameters.size(); i++) {
                Parameter parameter = _oneTimeQueryParameters.get(i);
                result = result + (i > 0 ? " and " : "") + parameter.getName() + " = ?";
            }
        }

        return result;
    }

    public void setOneTimeParameters(List<Parameter> parameters) {
        _oneTimeQueryParameters = parameters;
        _resetQueryParameters = true;
    }

}
