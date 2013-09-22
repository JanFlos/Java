package dml;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Updates a record in a database table
 * 
 * @author Jan Flos
 * 
 */
public class RecordUpdater {

	private CallableStatement	_command;
	private Metadata			_metadata;

	public RecordUpdater(Connection connection, Metadata metadata) throws SQLException {

		_metadata = metadata;

		String commandString = CommandBuilder.updateCommand(metadata);
		_command = connection.prepareCall(commandString);

	}

	/*
	 * (non-Javadoc)
	 * @see h2.IDMLRecordSaver#post(java.lang.Object[])
	 */
	public void post(Object[] record) throws SQLException {

		int parameterIndex = 0;

		for (String updatedColumn : _metadata.getUpdateableColumns()) {
			int columnIndex = _metadata.getColumnIndex(updatedColumn);
			_command.setObject(++parameterIndex, record[columnIndex]);
		}

		for (String primaryKeyColumn : _metadata.getReturningColumns()) {
			int columnIndex = _metadata.getColumnIndex(primaryKeyColumn);
			_command.setObject(++parameterIndex, record[columnIndex]);

		}
		// Execute the SQL command on the database
		_command.execute();

	}
}
