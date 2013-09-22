package cz.robotron.jdbc;

public class AbstractDataBlockRecord implements IDataBlockRecord {

	protected RecordStatus fStatus = null;

	Object fData[];

	public AbstractDataBlockRecord(Object[] row) {
		this.fData = row;
	}

	@Override
	public Object getValue(int column) {
		return fData[column - 1];
	}

	public void setValue(int column, Object value) {
		fData[column - 1] = value;
	}

	public RecordStatus getStatus() {
		return fStatus;
	}

	@Override
	public Object[] getValues() {
		return fData;
	}

	public void setValues(Object[] objects) {
		fData = objects;
	}

	@Override
	public IDataBlockRecord save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
