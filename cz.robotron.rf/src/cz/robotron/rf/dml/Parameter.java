package cz.robotron.rf.dml;

public class Parameter {

    //String _columnName;
    Object _value;
    int    _index;
    String _name;

    public Parameter(String name, int index, Object value) {
        super();
        _name = name;
        _value = value;
        _index = index;
    }

    public String getName() {
        return _name;
    }

    public Object getValue() {
        return _value;
    }

    public int getIndex() {
        return _index;
    }

}
