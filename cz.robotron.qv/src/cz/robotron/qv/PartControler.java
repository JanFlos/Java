 
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

	public PartControler() {
		//TODO Your code here
	}
	
	@PostConstruct
    public void postConstruct(Composite parent, @Optional DataBlock dataBlock) throws SQLException {
        //assert _dataBlock != null;

        parent.setLayout(new GridLayout(1, false));

        _tableViewer = new TableViewer(parent);
        GridLayout gridLayout = (GridLayout) _tableViewer.getLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        _tableViewer.setDataBlock(_dataBlock);
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
	    /*
        assert _tableViewer != null;
        _tableViewer.setFocus();
        */
	}

    public static String getPluginId() {
        return "bundleclass://cz.robotron.qv/cz.robotron.qv.PartControler";

    }
	
	
}