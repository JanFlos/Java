package swt;

/*
 * Virtual Tree example snippet: create a virtual tree (lazy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */ 
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Snippet202 {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout (new FillLayout());
	final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);
	tree.addListener(SWT.SetData, new Listener() {
		@Override
        public void handleEvent(Event event) {
			TreeItem item = (TreeItem)event.item;
			TreeItem parentItem = item.getParentItem();
			String text = null;
			if (parentItem == null) {
				text = "node "+tree.indexOf(item);
			} else {
				text = parentItem.getText()+" - "+parentItem.indexOf(item);
			}
			item.setText(text);
			item.setItemCount(10);
                //item.setd
		}
	});
	tree.setItemCount(20);
	shell.setSize(400, 300);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}