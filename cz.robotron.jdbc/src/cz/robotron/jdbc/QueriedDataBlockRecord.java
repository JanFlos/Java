package cz.robotron.jdbc;

class QueriedDataBlockRecord extends AbstractDataBlockRecord implements
		IDataBlockRecord {

	public QueriedDataBlockRecord(Object[] row) {
		super(row);
		this.fStatus = RecordStatus.QUERY;
	}

	/*
	 * public void updateDatabaseRecord() { // TODO handle only updated columns
	 * // TODO save last saved SQL statement
	 * 
	 * }
	 * 
	 * public void insertDatabaseRecord() throws Exception {
	 * 
	 * // Build the insert statement CallableStatement callableInsertStatement =
	 * getCallableInsertStatement();
	 * 
	 * int parameterIndex = 0;
	 * 
	 * // Set the inserted values for (int i = 0; i < fData.length; i++) {
	 * parameterIndex++; callableInsertStatement.setObject(parameterIndex,
	 * fData[i]); }
	 * 
	 * // Register OUT parameters for (int i = 0; i < fData.length; i++) {
	 * parameterIndex++;
	 * callableInsertStatement.registerOutParameter(parameterIndex,
	 * getDMLDataTarget().getColumnSQLType(i + 1)); }
	 * 
	 * // execute the insert statement callableInsertStatement.execute();
	 * 
	 * }
	 * 
	 * public RecordStatus getStatus() { return fStatus; }
	 * 
	 * public void setStatus(RecordStatus status) { fStatus = status; }
	 */

}
