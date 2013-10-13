/**
 * 
 */
package cz.robotron.rf.dml;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Deletes a record in a database table
 * 
 * @author Jan Flos
 * 
 */
public class RecordDeleter {

	private final MetadataProvider			_metadata;
	private final CallableStatement	_command;

	public RecordDeleter(Connection connection, MetadataProvider metadata) throws SQLException {

		_metadata = metadata;

		String commandString = CommandBuilder.deleteCommand(metadata);
		_command = connection.prepareCall(commandString);

	}

	/**
	 * Deletes the record in the Database
	 * 
	 */
	public void post(Object[] record) throws SQLException {

		// Set the parameter values
		int parameterIndex = 0;
		List<String> primaryKeyColumns = _metadata.getPrimaryKeyColumnNames();
		int columnIndex = 0;
		for (String primaryKeyColumn : primaryKeyColumns) {
			columnIndex = _metadata.getColumnIndex(primaryKeyColumn);
			_command.setObject(++parameterIndex, record[columnIndex]);
		}

		// Execute the SQL command on the database
		_command.execute();
	}

}
