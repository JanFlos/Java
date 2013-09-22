package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// TODO Vylepšit error handling
// TODO Zakomponovat možnost volat databázové procedůry
// TODO Zakomponovat triggery podobně jako ve formsích
// TODO Implement Iterable
public class DataBlock {

	public DataBlock() {
		fRecords = new ArrayList<IDataBlockRecord>();
		IDataBlockRecord record = new NewDataBlockRecord();
		fRecords.add(record);
		setCurrentRecord(record);
	}

	private Connection fConnection;
	private IDataBlockRecord fCurrentRecord;
	private SQLDataSource fQueryDataSource;
	private SQLDataSource fDMLDataTarget;
	private DataBlockStatus fStatus;

	private List<IDataBlockRecord> fRecords;
	private List<IDataBlockRecord> fUnsavedRecords;

	private int fCurrentRecordIndex = 0;

	/*
	 * Executes the query on the datasource
	 */
	public void executeQuery() throws Exception {

		assert (fConnection != null) : "connection is not defined use setConnection() method";

		assertBlockIsSaved(); // Datablock have to be saved

		// Prepare Query and fetch data into internall sctructure
		ResultSet resultSet = fQueryDataSource.executeQuery();

		fRecords = new ArrayList<IDataBlockRecord>();

		int columnCount = fQueryDataSource.getColumnCount();
		while (resultSet.next()) {

			Object row[] = new Object[columnCount];

			for (int i = 0; i < columnCount; i++) {
				row[i] = resultSet.getObject(i + 1);
			}

			IDataBlockRecord record = new QueriedDataBlockRecord(row);
			fRecords.add(record);
		}
		resultSet.close();

	}

	public int getColumnCount() {
		return getQueryDataSource().getColumnCount();
	}

	private void assertBlockIsSaved() throws UnsavedBlockException {
		if (getStatus() == DataBlockStatus.CHANGED)
			throw new UnsavedBlockException();
	}

	/*
	 * CRUD Operations
	 */
	public void createRecord() throws Exception {
		IDataBlockRecord record = new NewDataBlockRecord();

		addToRecordList(record);
		addToUnsavedRecords(record);
		setCurrentRecord(record);
	}

	private void addToRecordList(IDataBlockRecord record) {
		getRecords().add(getCurrentRecordIndex(), record);
	}

	private void addToUnsavedRecords(IDataBlockRecord record) {

		List<IDataBlockRecord> unsavedRecords = getUnsavedRecords();

		// initialize unsaved records
		if (unsavedRecords == null)
			fUnsavedRecords = new ArrayList<IDataBlockRecord>();

		if (fUnsavedRecords.indexOf(record) == -1)
			fUnsavedRecords.add(record);
	}

	private void setCurrentRecordIndex(int i) {
		fCurrentRecordIndex = i;
		fCurrentRecord = fRecords.get(i);
	}

	private List<IDataBlockRecord> getRecords() {

		return fRecords;

	}

	public void deleteRecord() {
		IDataBlockRecord record = getCurrentRecord();

		fRecords.remove(record);

		getUnsavedRecords().add(new DeletedDataBlockRecord(record));

		// TODO: handle set the current record
		// TODO: delete from fUpdated, fInserted
		// TODO: when in fInserted then do not delete from DB
	}

	/*
	 * Sets the value of the current record For Queried record makes a new
	 * instance of inserted or updatedRecord to extend change tracking
	 * possibilities (Memory saving)
	 */
	public void setValue(int column, Object value) {

		IDataBlockRecord record = getCurrentRecord();
		RecordStatus recordStatus = record.getStatus();

		if (Sets.isIn(recordStatus, RecordStatus.NEW, RecordStatus.QUERY)) {

			// For new records create inserted record and add to tracked records
			if (recordStatus == RecordStatus.NEW) {

				record = new InsertedDataBlockRecord(fDMLDataTarget);

			} else if (recordStatus == RecordStatus.QUERY) {

				record = new UpdatedDataBlockRecord(record.getValues());

			}

			getRecords().set(getCurrentRecordIndex(), record); // Replace the
																// current
																// record
			addToUnsavedRecords(record); // Add to unsaved record list
			setCurrentRecord(record); // Set the current record pointer
		}

		record.setValue(column, value);

	}

	public Object getValue(int column) {
		return getCurrentRecord().getValue(column);
	}

	/*
	 * Save the changes to the target datasource
	 */
	public void post() throws Exception {

		int recordIndex = 0;
		List<IDataBlockRecord> unsavedRecords = getUnsavedRecords();
		List<IDataBlockRecord> allRecords = getRecords();

		// Insert, Update, Delete unsaved Records into database
		for (IDataBlockRecord record : unsavedRecords) {

			// Save current record to database
			IDataBlockRecord savedRecord = record.save();

			// Replace the record object
			recordIndex = allRecords.indexOf(record);
			allRecords.set(recordIndex, savedRecord);

		}

		unsavedRecords = null; // Remove all elements

		//

	}

	/*
	 * Save the changes to the target datasource and commit the transaction
	 */
	public void commit() throws Exception {
		post();
		fConnection.commit();
	}

	/*
	 * Forms method : go_record()
	 */
	public void goRecord(int pRecordNumber) {

		assert (pRecordNumber <= getRecordCount()) : String.format(
				"Invalid record number %s. Record count is %s2", pRecordNumber,
				getRecordCount());

		setCurrentRecordIndex(pRecordNumber);
	}

	private void setCurrentRecord(IDataBlockRecord record) {
		fCurrentRecord = record;
		fCurrentRecordIndex = fRecords.indexOf(record);
	}

	/*
	 * Selection movement
	 */
	public void firstRecord() {
		goRecord(0);
	}

	public void lastRecord() {
		// TODO Fetch all records in the array
		goRecord(getRecordCount());
	}

	private int getRecordCount() {
		return fRecords.size();
	}

	public Boolean nextRecord() {
		if (getCurrentRecordIndex() < getRecordCount()) {
			goRecord(getCurrentRecordIndex() + 1);
			return true;
		}

		return false;

	}

	public Boolean previousRecord() {
		if (getCurrentRecordIndex() > 1) {
			goRecord(getCurrentRecordIndex() - 1);
			return true;
		}
		return false;
	}

	/*
	 * Datasource setup
	 */
	public void setQueryDataSource(String pName) throws Exception {
		fQueryDataSource = new SQLDataSource(fConnection, pName);

		if (fDMLDataTarget == null) {
			fDMLDataTarget = fQueryDataSource;
		}
	}

	public void setDMLDataTarget(String pTable) throws Exception {
		fDMLDataTarget = new SQLDataSource(fConnection, pTable);
	}

	public DataBlockStatus getStatus() {
		return fStatus;
	}

	public void setStatus(DataBlockStatus status) {
		this.fStatus = status;
	}

	public void setConnection(Connection pConnection) {
		fConnection = pConnection;

	}

	public List<IDataBlockRecord> getUnsavedRecords() {
		return fUnsavedRecords;
	}

	public IDataBlockRecord getCurrentRecord() {
		return fCurrentRecord;
	}

	public SQLDataSource getDMLDataTarget() {
		return fDMLDataTarget;
	}

	public int getCurrentRecordIndex() {
		return fCurrentRecordIndex;
	}

	public SQLDataSource getQueryDataSource() {
		return fQueryDataSource;
	}
}
