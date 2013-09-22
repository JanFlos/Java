/**
 * 
 */
package dml;

import constants.RecordStatusEnum;

/**
 * @author Jan Flos
 * 
 */
public class Record {
	public static Record newRecord(Object[] data) {
        return new Record(data);
    }

    Object[]			_data;
	RecordStatusEnum	_status;

	private Record(Object[] data) {
		this(data, RecordStatusEnum.QUERY);
	}

	/**
	 * @param _data
	 */
	public Record(Object[] data, RecordStatusEnum status) {
		_data = data;
		_status = status;
	}

	public Object[] getData() {
		return _data;
	}

	public void setData(Object[] data) {
		_data = data;
	}

	public RecordStatusEnum getStatus() {
		return _status;
	}

	public void setStatus(RecordStatusEnum status) {
		_status = status;
	}

    /**
     * @param columnIndex
     * @param value
     */
    public void setColumn(int columnIndex, Object value) {
        _data[columnIndex] = value;

    }

    /**
     * @param data
     * @return
     */
    public static Record newQueriedRecord(Object[] data) {
        Record record = new Record(data);
        record.setStatus(RecordStatusEnum.NEW);
        return record;
    }

    /**
     * @param data
     * @return
     */

}