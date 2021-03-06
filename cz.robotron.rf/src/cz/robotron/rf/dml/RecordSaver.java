/**
 * 
 */
package cz.robotron.rf.dml;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import cz.robotron.rf.constants.RecordStatusEnum;

/**
 * @author Jan Flos
 * 
 */
public class RecordSaver {

	private RecordDeleter	_deleter;
	private RecordInserter	_inserter;
	private final MetadataProvider			_metadata;
	private RecordUpdater	_updater;
    private final Connection _connection;

    public RecordSaver(Connection connection, MetadataProvider metadata) {
		_metadata = metadata;
        _connection = connection;
	}

    public void post(List<Record> records) throws SQLException {

		Object[] data;
		for (Record record : records) {

			data = record.getData();

			switch (record.getStatus()) {

			case NEW:
				if (_inserter == null)
                        _inserter = new RecordInserter(_connection, _metadata);

				_inserter.post(data);
                    record.setStatus(RecordStatusEnum.QUERY);
                    break;

			case CHANGED:
				if (_updater == null)
                        _updater = new RecordUpdater(_connection, _metadata);

				_updater.post(data);
                    record.setStatus(RecordStatusEnum.QUERY);
                    break;

			case DELETED:
				if (_deleter == null)
                        _deleter = new RecordDeleter(_connection, _metadata);

				_deleter.post(data);
                    break;

			default:
				break;
			}

		}

        records.clear(); // clear the list
	}
}
