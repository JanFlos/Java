/**
 * 
 */
package dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import com.google.common.collect.Lists;

/**
 * @author Jan Flos
 * 
 */
public class RecordSelector {

    private PreparedStatement      _command;
    private int                    _columnCount;
    private final Connection       _connection;
    private final MetadataProvider _metadata;
    private String                 _dataSource;

    static Logger                  log = Logger.getLogger(RecordSelector.class);

    /**
    * @param metadata
    * @throws SQLException
    */
    public RecordSelector(Connection connection, MetadataProvider metadata) throws SQLException {
        _connection = connection;
        _metadata = metadata;

    }

    public void prepareCommand() throws SQLException {

        assert _metadata != null;
        assert _connection != null;

        String dataSource = _metadata.getDataSource();

        if (_command != null) {
            if (!dataSource.equals(_dataSource)) {
                _command.close();
                _command = null;
            }

        }

        //log.info(dataSource);

        if (_command == null) {
            _dataSource = dataSource;
            _command = _connection.prepareStatement(dataSource, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            _columnCount = _metadata.getColumnCount();
        }

    }

    /**
     * Reads the data into the cache
     * 
     * @throws SQLException
     */
    public List<Record> executeQuery() throws SQLException {

        prepareCommand();

        List<Record> result = Lists.newArrayList();

        ResultSet rs = _command.executeQuery();
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
        
        prepareCommand();
        int index = boundColumnValue.getIndex();
        Object value = boundColumnValue.getValue();
        _command.setObject(index + 1, value);

    }

}
