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
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;
import cz.robotron.examples.Utils;
import cz.robotron.examples.jface.XMLTreeContentProvider;
import cz.robotron.examples.xml.TestXML;

public class _002_FilteredTreeAndXML {

    private TreeViewer viewer;

    @Inject
    Shell              _shell;

    @PostConstruct
    public void createComposite(Composite parent) {

        GridLayout gl_parent = new GridLayout();
        parent.setLayout(gl_parent);
        FilteredTree tree = new FilteredTree(parent, SWT.CHECK | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter(), true);

        viewer = tree.getViewer();
        viewer.setContentProvider(new XMLTreeContentProvider());
        viewer.setLabelProvider(new XMLTreeLabelProvider());

        try {

            Node[] _testNodes = TestXML.getTestNodes();

            viewer.setInput(_testNodes);
            //viewer.expandAll();

        } catch (Exception e) {
            Utils.showException("Error while loading test nodes", e);
        }

    }

    @Focus
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}



class XMLTreeLabelProvider extends LabelProvider {
    @Override
    public String getText(Object element) {
        Node node = (Node) element;
        String nodeValue = node.getNodeValue();
        String nodeName = node.getNodeName();
        return (nodeName != "#text" ? nodeName : "") + (nodeValue != null ? " " + nodeValue : "");
    }
}
