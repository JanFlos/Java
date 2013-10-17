/**
 * 
 */
package cz.robotron.rf.dml;

import cz.robotron.rf.constants.RecordStatusEnum;

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

    public Record(Object[] data) {
		this(data, RecordStatusEnum.QUERY);
	}

	/**
	 * @param _data
	 */
	public Record(Object[] data, RecordStatusEnum status) {
		_data = data;
		_status = status;
	}


    public String[] getStringData() {
        int count = _data.length;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = _data[i] != null ? _data[i].toString() : "";

        }
        return result;
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
    public void setValue(int columnIndex, Object value) {
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

    public Object getValue(int columnIndex) {
        
        assert _data != null && _data.length - 1 >= columnIndex;
        
        return _data[columnIndex];

    }

    /**
     * @param data
     * @return
     */

}
