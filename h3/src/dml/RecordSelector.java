/**
 * 
 */
package dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * @author Jan Flos
 * 
 */
public class RecordSelector {

    private final PreparedStatement _command;
    private final int               _columnCount;

    /**
    * @param metadata
    * @throws SQLException
    */
    public RecordSelector(Connection connection, MetadataProvider metadata) throws SQLException {
        _command = connection.prepareStatement(metadata.getDataSource(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        _columnCount = metadata.getColumnCount();

    }


    /**
     * Reads the data into the cache
     * 
     * @throws SQLException
     */
    public List<Record> executeQuery() throws SQLException {

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
        int index = boundColumnValue.getIndex();
        Object value = boundColumnValue.getValue();
        _command.setObject(index + 1, value);

    }

}
