package cz.robotron.examples.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import cz.robotron.examples.Utils;

/**
 * TableViewer: Hide full selection
 * 
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class _004_TableWithFullSelectionAndEdit {

    @Inject
    Shell               _shell;
    private TableViewer _tableViewer;

    @PostConstruct
    public void createComposite(Composite parent) {
        try {
            _tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
            _tableViewer.setLabelProvider(new LabelProvider());
            _tableViewer.setContentProvider(new MyContentProvider());
            _tableViewer.setCellModifier(new ICellModifier() {

                @Override
                public boolean canModify(Object element, String property) {
                    return true;
                }

                @Override
                public Object getValue(Object element, String property) {
                    return ((MyModel) element).counter + "";
                }

                @Override
                public void modify(Object element, String property, Object value) {
                    TableItem item = (TableItem) element;
                    ((MyModel) item.getData()).counter = Integer.parseInt(value.toString());
                    _tableViewer.update(item.getData(), null);
                }

            });
            _tableViewer.setColumnProperties(new String[] { "column1", "column2" });
            TextCellEditor textCellEditor = new TextCellEditor(_tableViewer.getTable());
            _tableViewer.setCellEditors(new CellEditor[] { textCellEditor, new TextCellEditor(_tableViewer.getTable()) });

            TableColumn column = new TableColumn(_tableViewer.getTable(), SWT.NONE);
            column.setWidth(100);
            column.setText("Column 1");

            column = new TableColumn(_tableViewer.getTable(), SWT.NONE);
            column.setWidth(100);
            column.setText("Column 2");

            MyModel[] model = createModel();
            _tableViewer.setInput(model);
            //_tableViewer.getTable().setLinesVisible(true);
            _tableViewer.getTable().setHeaderVisible(true);

            _tableViewer.getTable().addListener(SWT.EraseItem, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    event.detail &= ~SWT.SELECTED;
                }
            });

        } catch (Exception e) {
            Utils.showException("Cannot create ViewerSupport for a Table", e);
        }
    }

    private class MyContentProvider implements IStructuredContentProvider {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        @Override
        public Object[] getElements(Object inputElement) {
            return (MyModel[]) inputElement;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        @Override
        public void dispose() {

        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

    }

    @Focus
    public void setFocus() {
        _tableViewer.getControl().setFocus();
    }

    public class MyModel {
        public int counter;

        public MyModel(int counter) {
            this.counter = counter;
        }

        @Override
        public String toString() {
            return "Item " + this.counter;
        }
    }

    private MyModel[] createModel() {
        MyModel[] elements = new MyModel[10];

        for (int i = 0; i < 10; i++) {
            elements[i] = new MyModel(i);
        }

        return elements;
    }

}
