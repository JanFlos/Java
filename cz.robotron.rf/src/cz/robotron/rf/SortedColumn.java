package cz.robotron.rf;

import cz.robotron.rf.constants.SortOrderEnum;


public class SortedColumn {

    String _name;
    SortOrderEnum _order;

    public String getName() {
        return _name;
    }

    public SortOrderEnum getOrder() {
        return _order;
    }

    public SortedColumn(String name, SortOrderEnum order) {
        super();
        _name = name;
        _order = order;
    }

}
