package cz.robotron.rf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import cz.robotron.rf.dml.Parameter;
import cz.robotron.rf.dml.Record;
import cz.robotron.rf.dml.RecordSelector;

public class RecordFinder {

    private final RecordSelector  _recordSelector;
    private final List<Parameter> _baseQueryParameters;
    private final String          _dataSource;

    public RecordFinder(Connection _connection, RecordSelector recordSelector) throws SQLException {
        assert recordSelector != null;

        _baseQueryParameters = recordSelector.getQueryParameters();
        _recordSelector = new RecordSelector(_connection);
        _dataSource = recordSelector.getDataSource();
    }


    public void setRecordSelector(String praedikat, Object[] values) throws SQLException {

        // Build query
        String dataSource = _dataSource;
        String whereClause = praedikat.matches("\\w[\\w\\d]+") ? String.format("%s = ?", praedikat) : praedikat;
        dataSource = String.format("select * from (%s) where %s", dataSource, whereClause);
        _recordSelector.setDataSource(dataSource);

        // Set Parameter
        int parameterIndex = 0;
        if (_baseQueryParameters != null) {
            _recordSelector.setQueryParameters(_baseQueryParameters);
            parameterIndex = _baseQueryParameters.size();
        }

        // Set the praedikat Parameters
        for (Object parameterValue : values) {
            //_recordSelector.setParameter(new Parameter(parameterIndex++, parameterValue));
        }

    }

    public List<Record> find(String praedikat, Object[] values) throws SQLException {

        setRecordSelector(praedikat, values);
        return _recordSelector.executeQuery();
    }

    public Record findFirstRecord(String praedikat, Object[] values) throws SQLException {

        setRecordSelector(praedikat, values);
        return null;
        //return _recordSelector.executeScalar();
    }

}
