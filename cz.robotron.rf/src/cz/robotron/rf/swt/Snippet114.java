package cz.robotron.rf.swt;

/*
 * Tree example snippet: detect a selection or check event in a tree (SWT.CHECK)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.google.gson.Gson;

public class Snippet114 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Tree tree = new Tree(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        for (int i = 0; i < 12; i++) {
            TreeItem item = new TreeItem(tree, SWT.NONE);
            item.setText("Item " + i);
            TreeItem item2 = new TreeItem(item, SWT.NONE);
            item2.setText("Subitem");
        }

        Gson gson = new Gson();

        String json = gson.toJson(tree);
        System.out.println(json);

        Rectangle clientArea = shell.getClientArea();
        tree.setBounds(clientArea.x, clientArea.y, 100, 100);
        tree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
                System.out.println(event.item + " " + string);
            }
        });
        shell.setSize(200, 200);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
