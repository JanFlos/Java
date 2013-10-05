package swt;
 
/*
 * Tree example snippet: create a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Snippet15 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree (shell, SWT.BORDER);
	for (int i=0; i<4; i++) {
		TreeItem iItem = new TreeItem (tree, 0);
		iItem.setText ("TreeItem (0) -" + i);
		for (int j=0; j<4; j++) {
			TreeItem jItem = new TreeItem (iItem, 0);
			jItem.setText ("TreeItem (1) -" + j);
			for (int k=0; k<4; k++) {
				TreeItem kItem = new TreeItem (jItem, 0);
				kItem.setText ("TreeItem (2) -" + k);
				for (int l=0; l<4; l++) {
					TreeItem lItem = new TreeItem (kItem, 0);
					lItem.setText ("TreeItem (3) -" + l);
				}
			}
		}
	}
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 