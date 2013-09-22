/**
 * 
 */
package dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Flos
 * 
 */
public class RecordReader {

	private PreparedStatement	_command;

	/**
	 * @param queryText
	 * @throws SQLException
	 */
	public RecordReader(Connection connection, String queryText) throws SQLException {
		_command = connection.prepareStatement(queryText, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	}

	/**
	 * Reads the data into the cache
	 * 
	 * @throws SQLException
	 */
	public void executeQuery() throws SQLException {
		ResultSet rs = _command.executeQuery();
	}

}
