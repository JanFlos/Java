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
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.property.list.DelegatingListProperty;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.swt.internal.copy.FilteredTree;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import cz.robotron.examples.Utils;
import cz.robotron.examples.jface.MyPatternFilter;
import cz.robotron.examples.xtend.PersonGroup;
import cz.robotron.examples.xtend.PersonList;
import cz.robotron.examples.xtend.TestData;

public class _005_TreeWithViewerSupport {

    @Inject
    Shell              _shell;
    private TreeViewer _treeViewer;

    @PostConstruct
    public void createComposite(Composite parent) {
        try {

            GridLayout gl_parent = new GridLayout();
            parent.setLayout(gl_parent);

            FilteredTree filteredTree = new FilteredTree(parent, SWT.BORDER | SWT.FULL_SELECTION, new MyPatternFilter(), true);
            _treeViewer = filteredTree.getViewer();
            Tree tree = _treeViewer.getTree();
            tree.setHeaderVisible(true);
            tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

            String[] propertyNames = new String[] { "name", "lastName", "firstName", "address.street", "address.city" };
            createTreeColumns(_treeViewer, propertyNames);

            final Object input = TestData.getRoot();

            ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(new IObservableFactory() {

                @Override
                public IObservable createObservable(Object target) {
                    return (IObservable) target;
                }
            }, null);

            _treeViewer.setContentProvider(contentProvider);

            IListProperty childrenProp = new DelegatingListProperty() {
                IListProperty root                = BeanProperties.list(PersonGroup.class, "lists");
                IListProperty personGroupChildren = BeanProperties.list(PersonList.class, "persons");

                @Override
                protected IListProperty doGetDelegate(Object source) {
                    if (source instanceof PersonList)
                        return personGroupChildren;

                    else if (source instanceof PersonGroup)
                        return root;

                    return null;

                }
            };

            IValueProperty[] valueProperties = BeanProperties.values(propertyNames);
            
            ViewerSupport.bind(_treeViewer, input, childrenProp, valueProperties);

        } catch (Exception e) {
            Utils.showException("Cannot create ViewerSupport for a Table", e);
        }

    }

    private void createTreeColumns(TreeViewer treeViewer, String[] propertyNames) {
        for (String name : propertyNames) {
            TreeViewerColumn viewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
            TreeColumn tableColumn = viewerColumn.getColumn();
            tableColumn.setText(name);
            tableColumn.setWidth(100);

        }

    }

    @Focus
    public void setFocus() {
        _treeViewer.getControl().setFocus();
    }
}
