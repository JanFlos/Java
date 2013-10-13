package cz.robotron.rf.events;

import cz.robotron.rf.constants.SortOrderEnum;


public class SortOrderChangedEvent {

    private final String _columnName;
    private final SortOrderEnum _sortDirection;

    public SortOrderChangedEvent(String columnName, SortOrderEnum sortOrder) {
        _columnName = columnName;
        _sortDirection = sortOrder;
    }

    public String getColumnName() {
        return _columnName;
    }

    public SortOrderEnum getSortDirection() {
        return _sortDirection;
    }

    public String getSortedColumn() {
        return (_sortDirection == SortOrderEnum.ASC ? _columnName : _columnName + " desc");
    }

}
