/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cz.robotron.examples.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.swt.internal.copy.FilteredTree;
import org.eclipse.e4.ui.workbench.swt.internal.copy.PatternFilter;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.robotron.examples.Utils;
import cz.robotron.examples.gson.MyNode;
import cz.robotron.examples.gson.TestGson;

public class _001_FilteredTreeAndGson {

    private TreeViewer viewer;
    private Text       _text;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private MyNode[]   _testNodes;

    @Inject
    Shell              _shell;

    @PostConstruct
    public void createComposite(Composite parent) {
        GridLayout gl_parent = new GridLayout();
        parent.setLayout(gl_parent);

        SashForm sashForm = new SashForm(parent, SWT.NONE);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        FilteredTree tree = new FilteredTree(sashForm, SWT.CHECK | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter(), true);

        _text = new Text(sashForm, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        viewer = tree.getViewer();
        sashForm.setWeights(new int[] { 1, 1 });
        viewer.setContentProvider(new TreeContentProvider());
        viewer.setLabelProvider(new TreeLabelProvider());

        try {
            _testNodes = TestGson.getTestNodes();

            viewer.setInput(_testNodes);

            viewer.getTree().addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
                    System.out.println(event.item + " " + string);
                    MyNode myNode = (MyNode) event.item.getData();
                    myNode.checked = !myNode.checked;
                    _text.setText(gson.toJson(_testNodes));

                }
            });

            viewer.expandAll();

        } catch (Exception e) {
            Utils.showException("Error while loading test nodes", e);
            //String msg = e.toString();
            //Utils.errorDialogWithStackTrace(msg, e);
        }

    }



    @Focus
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}

class TreeContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(Object arg0) {
        return ((MyNode) arg0).childNodes;

    }

    @Override
    public Object getParent(Object arg0) {
        return null;
    }

    @Override
    public boolean hasChildren(Object arg0) {
        if (arg0 != null) {
            MyNode myNode = (MyNode) arg0;

            if (myNode.childNodes != null && myNode.childNodes.length > 0)
                return true;
        }
        return false;
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

class TreeLabelProvider extends LabelProvider {
    @Override
    public String getText(Object element) {
        MyNode node = (MyNode) element;
        return node.name;
    }
}
