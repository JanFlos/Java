package cz.robotron.jdbc;

public class NewDataBlockRecord extends AbstractDataBlockRecord implements
		IDataBlockRecord {

	public NewDataBlockRecord() {
		super(null);
		this.fStatus = RecordStatus.NEW;
	}

}
