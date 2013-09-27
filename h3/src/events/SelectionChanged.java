package events;


public class SelectionChanged {
    int _selectionIndex;

    public int getSelectionIndex() {
        return _selectionIndex;
    }

    public SelectionChanged(int index) {
        super();
        _selectionIndex = index;
    }
}
