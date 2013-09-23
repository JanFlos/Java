package cz.robotron.jdbc;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public class InsertedDataBlockRecord extends AbstractDataBlockRecord implements
		IDataBlockRecord {

	SQLDataSource fDMLDataSource;

	@Override
	public IDataBlockRecord save() throws Exception {

		int parameterIndex = 0;
		int savedParameterIndex = 0;

		// Build the insert statement
		CallableStatement callableInsertStatement = fDMLDataSource
				.getCallableInsertStatement();

		// Set the inserted values
		for (int i = 0; i < fData.length; i++) {
			parameterIndex++;
			callableInsertStatement.setObject(parameterIndex, fData[i]);
		}

		savedParameterIndex = parameterIndex;
		// Register OUT parameters
		for (int i = 0; i < fData.length; i++) {
			parameterIndex++;
			callableInsertStatement.registerOutParameter(parameterIndex,
					fDMLDataSource.getColumnSQLType(i + 1));
		}

		// Execute the insert command
		parameterIndex = savedParameterIndex;
		ResultSet result = callableInsertStatement.executeQuery();
		callableInsertStatement.ex
		// Return the parameters
		for (int i = 0; i < fData.length; i++) {
			parameterIndex++;
			fData[i] = callableInsertStatement.getObject(parameterIndex);
		}

		return new QueriedDataBlockRecord(fData);

	}

	public InsertedDataBlockRecord(SQLDataSource dataSource) {
		super(null);
		int columnCount = dataSource.getColumnCount();
		this.setValues(new Object[columnCount]);
		this.fStatus = RecordStatus.INSERT;
		fDMLDataSource = dataSource;
	}

}
