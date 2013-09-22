package cz.robotron.jdbc;

import java.sql.CallableStatement;
import java.util.HashSet;

public class UpdatedDataBlockRecord extends AbstractDataBlockRecord implements
		IDataBlockRecord {

	SQLDataSource fDMLDataSource;

	Object[] fOriginalData;
	HashSet<Integer> fChangedColumns;

	public UpdatedDataBlockRecord(Object[] data) {
		super(data);
		fOriginalData = data;
		fChangedColumns = new HashSet<Integer>();

	}

	@Override
	public void setValue(int column, Object value) {
		// TODO Auto-generated method stub
		super.setValue(column, value);
		fChangedColumns.add(column);
	}

	@Override
	public void setValues(Object[] objects) {
		// TODO Auto-generated method stub
		super.setValues(objects);
		for (int i = 0; i < objects.length; i++) {
			fChangedColumns.add(i);
		}
	}

	@Override
	public void save() throws Exception {
		// Build the update statement only for the changed columns
		CallableStatement callableInsertStatement = fDMLDataTarget
				.getCallableInsertStatement();

		// Set the variables on the statement

		// Execute command

		// Read the values back

	}

}
