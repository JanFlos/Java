package cz.robotron.rf.dml;

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

	private final CallableStatement		_command;
	private final Map<Integer, Integer>	_returningColumns	= Maps.newHashMap();
	private final int						_columnCount;
	private final MetadataProvider				_metadata;

	/**
	 * @param connection Connection to the database
	 * @param metadata TODO
	 * @throws SQLException
	 */
	public RecordInserter(Connection connection, MetadataProvider metadata) throws SQLException {

		_metadata = metadata;

		// Construct prepared command
		String commandString = CommandBuilder.insertCommand(metadata);
		_command = connection.prepareCall(commandString);
        _columnCount = metadata.getColumnCount();

		// Registriere returning columns
		int returningColumnIndex = 0;
		int columnIndex;
		int outParameterIndex = _columnCount;
		int columnType;
		for (String returningColumn : metadata.getReturningColumnNames()) {
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

        List<String> insertableColumns = _metadata.getInsertableColumnNames();
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
		List<String> returningColumns = _metadata.getReturningColumnNames();
		for (String returningColumn : returningColumns) {
			columnIndex = _metadata.getColumnIndex(returningColumn);
			record[columnIndex] = _command.getObject(++outParameterIndex);
		}

	}

}
