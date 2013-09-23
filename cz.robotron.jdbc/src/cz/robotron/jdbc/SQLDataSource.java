package cz.robotron.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDataSource {

	private String fSQlText;
	private SQLDataSourceMetadata fMetaData;
	private Connection fConnection;

	private CallableStatement fCallableInsertStatement;
	private String fTargetTableName;

	public SQLDataSource(Connection connection, String sqlText)
			throws Exception {

		this.fSQlText = sqlText;
		this.fMetaData = null;
		this.fTargetTableName = DatablockHelper.getTableNameFromSql(sqlText);
		this.fConnection = connection;

		String sqlCommand = getSelectSQLCommand();
		String selectSQLCommandWithNoRows = String.format(
				"select * from (%s) where rownum < 0", sqlCommand);

		PreparedStatement fQueryStatement = fConnection
				.prepareStatement(selectSQLCommandWithNoRows);

		ResultSet resultSet = fQueryStatement.executeQuery();
		setMetaData(new SQLDataSourceMetadata(resultSet.getMetaData()));

		resultSet.close();
	}

	public String getSelectSQLCommand() {
		return fSQlText;
	}

	private SQLDataSourceMetadata getMetaData() {

		return fMetaData;
	}

	public int getColumnSQLType(int i) throws Exception {
		return getMetaData().getColumnSQLType(i);
	}

	public int getColumnCount() {
		if (fMetaData == null)
			return 0;
		return fMetaData.getColumnCount();
	}

	private void setMetaData(SQLDataSourceMetadata metaData)
			throws SQLException {
		fMetaData = metaData;
	}

	public ResultSet executeQuery() throws SQLException {

		PreparedStatement fQueryStatement = fConnection
				.prepareStatement(getSelectSQLCommand());

		ResultSet resultSet = fQueryStatement.executeQuery();

		// Set the DML Metadata
		if (fMetaData == null)
			setMetaData(new SQLDataSourceMetadata(resultSet.getMetaData()));

		return resultSet;
	}

	public CallableStatement getCallableInsertStatement() throws Exception {

		if (fCallableInsertStatement == null) {
			String insertSQLCommand = getInsertSQLCommand();
			fCallableInsertStatement = fConnection
					.prepareCall(insertSQLCommand);
		}

		return fCallableInsertStatement;
	}

	private Object getTargetTableName() {
		return this.fTargetTableName;
	}

	public IDataBlockRecord save(IDataBlockRecord record) {
		
		assert record.getStatus() == RecordStatus.INSERT || record.getStatus() == RecordStatus.CHANGED : "Wrong record status"
		
		// Build the DML statement for insert or update on the database
		DMLStatement dmlStatement = new DMLStatement(fMetaData, fConnection);
		
		// Set the variables on callable statement
		dmlStatement.setVariables(record); 
		
		// Execute command and write changed values back to the command
		return dmlStatement.execute();

	}

	private CallableStatement getCallableDMLStatement(IDataBlockRecord record) {
		// TODO Auto-generated method stub
		return null;
	}
}
