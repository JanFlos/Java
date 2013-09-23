package dml;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Inserts a Record into Database Table
 * 
 * @author Jan Flos
 * 
 */
public class RecordInserter {

	private CallableStatement		_command;
	private Map<Integer, Integer>	_returningColumns	= Maps.newHashMap();
	private int						_columnCount;
	private Metadata				_metadata;

	/**
	 * @param connection Connection to the database
	 * @param metadata TODO
	 * @throws SQLException
	 */
	public RecordInserter(Connection connection, Metadata metadata) throws SQLException {

		_metadata = metadata;

		// Construct prepared command
		String commandString = CommandBuilder.insertCommand(metadata);
		_command = connection.prepareCall(commandString);
		_columnCount = metadata.getInsertableColumns().size();

		// Registriere returning columns
		int returningColumnIndex = 0;
		int columnIndex;
		int outParameterIndex = _columnCount;
		int columnType;
		for (String returningColumn : metadata.getReturningColumns()) {
			columnIndex = _metadata.getColumnIndex(returningColumn);

			_returningColumns.put(++returningColumnIndex, columnIndex);

			columnType = metadata.getColumnType(returningColumn);
			_command.registerOutParameter(++outParameterIndex, columnType);
		}

	}

	/**
	 * Sets the parameter values and sends the SQL command to the database
	 */
	public void post(Object[] record) throws SQLException {

		List<String> insertableColumns = _metadata.getInsertableColumns();
		int columnIndex = 0;
		int i = 0;
		for (String column : insertableColumns) {
			columnIndex = _metadata.getColumnIndex(column);
			_command.setObject(++i, record[columnIndex]);
		}

		// Execute the SQL command on the database
		_command.execute();

		// Return the parameters
		columnIndex = 0;
		int outParameterIndex = insertableColumns.size();
		List<String> returningColumns = _metadata.getReturningColumns();
		for (String returningColumn : returningColumns) {
			columnIndex = _metadata.getColumnIndex(returningColumn);
			record[columnIndex] = _command.getObject(++outParameterIndex);
		}

	}

}
