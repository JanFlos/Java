package dml;


public class Parameter {

    String _columnName;
    Object _value;
    int    _index;

    public Parameter(String boundColumnName, int index, Object value) {
        super();
        _columnName = boundColumnName;
        _value = value;
        _index = index;
    }

    public String getColumnName() {
        return _columnName;
    }

    public Object getValue() {
        return _value;
    }

    public int getIndex() {
        return _index;
    }

}
