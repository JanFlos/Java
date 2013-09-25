package h2;

public class DataBlockColumn {

    private final String _name;
    private final int    _type;
    private final int    _displaySize;
	
    public DataBlockColumn(String name, int type, int displaySize) {
        _name = name.toLowerCase();
        _type = type;
        _displaySize = displaySize;
	}

    /**
     * @param string
     * @param numeric
     */
    public DataBlockColumn(String name, int type) {
        this(name, type, 100);
    }

    public String getName() {
        return _name;
	}

	public int getType() {
        return _type;
	}

    public int getDisplaySize() {
        return _displaySize;
    }

	
	
}
