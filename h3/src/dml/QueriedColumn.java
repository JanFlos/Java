package dml;


public class QueriedColumn {

    int    _columnIndex;
    Object _value;


    public QueriedColumn(int columnIndex, Object value) {
        super();
        _columnIndex = columnIndex;
        _value = value;
    }

    public int getColumnIndex() {
        return _columnIndex;
    }

    public Object getValue() {
        return _value;
    }

}
