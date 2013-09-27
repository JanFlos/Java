package events;

import java.util.List;
import com.google.common.collect.Lists;


public class SelectionChanged {
    List<Integer> _selection;

    public Integer getSelectionIndex() {
        if (_selection != null && _selection.size() > 0) {
            return _selection.get(0);
        }
        return null;
    }

    public SelectionChanged(List<Integer> list) {
        super();
        _selection = list;
    }

    public SelectionChanged(int i) {
        super();
        _selection = Lists.newArrayList();
        _selection.add(i);
    }
}
