 
package cz.robotron.qv;

import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import cz.robotron.rcplight.DataBlock;
import cz.robotron.rcplight.ui.TableViewer;

public class PartControler {

    @Inject
    @Optional
    DataBlock      _dataBlock;

    private TableViewer _tableViewer;

    @Inject
	public PartControler() {
		//TODO Your code here
	}
	
	@PostConstruct
    public void postConstruct(Composite parent) throws SQLException {

        assert _dataBlock != null;

        parent.setLayout(new GridLayout(1, false));

        _tableViewer = new TableViewer(parent);
        _tableViewer.setDataBlock(_dataBlock);
        /*
        _table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        _table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        _table.setHeaderVisible(true);
        _table.setLinesVisible(true);
        */
		//TODO Your code here
	}
	
	
    @Inject
    @Optional
    public void partActivation(@UIEventTopic(UIEvents.UILifeCycle.BRINGTOTOP) Event event, MPart part) {
        // Do something
        System.out.println(part.getLabel() + " " + part.isOnTop());

    }
	
	@Focus
	public void onFocus() {
        assert _tableViewer != null;
        _tableViewer.setFocus();
	}

    public static String getPluginId() {
        return "bundleclass://cz.robotron.qv/" + PartControler.class.toString();

    }
	
	
}