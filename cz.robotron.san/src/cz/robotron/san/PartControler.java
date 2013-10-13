 
package cz.robotron.san;

import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.event.Event;
import cz.robotron.rf.DataBlock;
import cz.robotron.rf.ui.TableViewer;

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

        GridLayout gl_parent = new GridLayout(1, false);
        gl_parent.marginHeight = 0;
        gl_parent.marginWidth = 0;
        gl_parent.horizontalSpacing = 0;
        gl_parent.verticalSpacing = 0;
        parent.setLayout(gl_parent);

        ToolBar toolBar = new ToolBar(parent, SWT.NONE);
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
        tltmNewItem.setImage(SWTResourceManager.getImage(PartControler.class, "/icons/full/dtool16/clear_co.gif"));

        ToolItem tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
        tltmNewItem_1.setImage(SWTResourceManager.getImage(PartControler.class, "/icons/full/obj16/fldr_obj.gif"));
        tltmNewItem_1.setSelection(true);

        _tableViewer = new TableViewer(parent);
        //Composite composite_1 = new Composite(parent, SWT.NONE);

        _tableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        _tableViewer.setDataBlock(_dataBlock);
        //parent.setLayout(new GridLayout());

        //        
        /*
        GridLayout gridLayout = (GridLayout) _tableViewer.getLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        
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
	    /*
        assert _tableViewer != null;
        _tableViewer.setFocus();
        */
	}

    public static String getPluginId() {
        return "bundleclass://cz.robotron.san/cz.robotron.san.PartControler";

    }
}