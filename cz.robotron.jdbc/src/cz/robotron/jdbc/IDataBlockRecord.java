package cz.robotron.jdbc;

public interface IDataBlockRecord {

	public RecordStatus getStatus();

	public Object getValue(int column);

	public void setValue(int column, Object value);

	public IDataBlockRecord save() throws Exception;

	public Object[] getValues();

	public void setValues(Object[] objects);

}
