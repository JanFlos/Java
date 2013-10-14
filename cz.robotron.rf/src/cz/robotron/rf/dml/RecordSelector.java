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

    /**
    * @param metadata
    * @throws SQLException
    */
    public RecordSelector(Connection connection, String dataSource) throws SQLException {
        _connection = connection;
        _dataSource = dataSource;

    }

    public void prepareCommand() throws SQLException {

        assert _connection != null;
        assert _dataSource != null;

        _command.close();
        _command = null;

        _command = _connection.prepareStatement(_dataSource, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        _columnCount = null;
        _preparedDataSource = _dataSource;

    }

    /**
     * Reads the data into the cache
     * 
     * @throws SQLException
     */
    public List<Record> executeQuery() throws SQLException {

        if (_preparedDataSource != null && _preparedDataSource != _dataSource)
            prepareCommand();

        List<Record> result = Lists.newArrayList();

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

    public void setParameter(Parameter boundColumnValue) throws SQLException {

        int index = boundColumnValue.getIndex();
        Object value = boundColumnValue.getValue();
        _command.setObject(index + 1, value);

    }

    public void setOneTimeWhere(String praedikat, Object[] values) {
        // TODO Auto-generated method stub

    }

    public void setDataSource(String dataSource) {
        _dataSource = dataSource;
    }

}
