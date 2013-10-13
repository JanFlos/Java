/**
 * 
 */
package cz.robotron.rf;

import java.util.List;
import com.google.common.collect.Lists;
import cz.robotron.rf.constants.RecordStatusEnum;
import cz.robotron.rf.dml.Record;

/**
 * @author Jan Flos
 * 
 */
public class ChangeTracker {

	private final List<Record>	_records	= Lists.newArrayList();

	public void recordAdded(Record record) {
		_records.add(record);
	}

	public void recordDeleted(Record record) {
		_records.remove(record);
	}

	public void recordUpdated(Record record) {

        // Change the status from the QUERY state
        if (record.getStatus() == RecordStatusEnum.QUERY)
            record.setStatus(RecordStatusEnum.CHANGED);

		if (!_records.contains(record))
			_records.add(record);
	}

	public List<Record> getChangedRecords() {
		return _records;

	}

}
