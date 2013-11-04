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
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import cz.robotron.examples.Utils;
import cz.robotron.examples.xtend.Person;
import cz.robotron.examples.xtend.TestData;

public class _003_TableWithViewerSupport {

    @Inject
    Shell               _shell;
    private TableViewer _tableViewer;

    @PostConstruct
    public void createComposite(Composite parent) {
        try {

            GridLayout gl_parent = new GridLayout();
            parent.setLayout(gl_parent);

            WritableList input = new WritableList(TestData.getPersonData(), Person.class);

            _tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
            Table table = _tableViewer.getTable();
            table.setHeaderVisible(true);

            // Set content Provider
            ObservableListContentProvider contentProvider = new ObservableListContentProvider();
            _tableViewer.setContentProvider(contentProvider);

            table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

            _tableViewer.setInput(input);
            String[] propertyNames = new String[] { "lastName", "firstName", "address.street", "address.city" };

            createTableColumns(_tableViewer, propertyNames);
            ViewerSupport.bind(_tableViewer,
                               input,
                               BeanProperties.values(propertyNames));

        } catch (Exception e) {
            Utils.showException("Cannot create ViewerSupport for a Table", e);
        }

    }

    private void createTableColumns(TableViewer tableViewer, String[] propertyNames) {
        for (String name : propertyNames) {
            TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
            TableColumn tableColumn = viewerColumn.getColumn();
            tableColumn.setText(name);
            tableColumn.setWidth(100);
        }

    }

    @Focus
    public void setFocus() {
        _tableViewer.getControl().setFocus();
    }
}
