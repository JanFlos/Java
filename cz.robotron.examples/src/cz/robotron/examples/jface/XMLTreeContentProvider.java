package cz.robotron.examples.jface;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cz.robotron.examples.Utils;

public class XMLTreeContentProvider implements ITreeContentProvider {

    @Override
    // Convert Nodes to array as childern
    public Object[] getChildren(Object arg0) {
        NodeList nodes = ((Node) arg0).getChildNodes();
        return Utils.nodesToArray(nodes);

    }

    @Override
    public Object getParent(Object arg0) {
        return null;
    }

    @Override
    public boolean hasChildren(Object arg0) {
        NodeList childNodes = ((Node) arg0).getChildNodes();
        return !(childNodes == null || childNodes.getLength() == 0);
    }

    @Override
    public Object[] getElements(Object arg0) {

        return (Object[]) arg0;
    }

    @Override
    public void dispose() {}

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}

}