package cz.robotron.examples.jface;

import org.eclipse.e4.ui.workbench.swt.internal.copy.PatternFilter;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class MyPatternFilter extends PatternFilter {

    @Override
    protected boolean isLeafMatch(final Viewer viewer, final Object element) {
        TreeViewer treeViewer = (TreeViewer) viewer;
        int numberOfColumns = treeViewer.getTree().getColumnCount();

        ObservableMapLabelProvider labelProvider = (ObservableMapLabelProvider) treeViewer.getLabelProvider();

        boolean isMatch = false;
        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            String labelText = labelProvider.getColumnText(element, columnIndex);
            isMatch |= wordMatches(labelText);
        }
        return isMatch;
    }
}
