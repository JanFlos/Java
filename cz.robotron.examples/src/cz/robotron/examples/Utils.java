package cz.robotron.examples;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.internal.services.Activator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
    public static void showException(String message, final Exception ex) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        ex.printStackTrace();

        final String trace = sw.toString(); // stack trace as a string

        // Temp holder of child statuses
        List<Status> childStatuses = new ArrayList<Status>();

        String error = null;
        // Split output by OS-independend new-line
        for (String line : trace.split(System.getProperty("line.separator"))) {
            if (error == null)
                error = line;
            else
                childStatuses.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, line));
        }

        MultiStatus multiStatus = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR,
                                                  childStatuses.toArray(new Status[] {}), // convert to array of statuses
                                                  error, ex);

        ErrorDialog.openError(null, "Unexpected Error", message, multiStatus);
    }

    /*
        public void showException(final Exception ex) {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), new Exception(sw.toString()));
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error", ex.getMessage(), status);
                }
            });
        }
    */

    public static Node[] nodesToArray(NodeList nodes) {
        int length = nodes.getLength();
        Node[] result = new Node[length];
        for (int i = 0; i < nodes.getLength(); i++) {
            result[i] = nodes.item(i);
        }

        return result;
    }

    public static InputStream getResourceAsStream(Object object, String fileName) {
        InputStream s = object.getClass().getResourceAsStream(fileName);
        return s;
    }
}
