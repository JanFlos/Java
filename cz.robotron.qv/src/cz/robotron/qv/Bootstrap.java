package cz.robotron.qv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Inject;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import cz.robotron.rcplight.DataBlock;

@SuppressWarnings("restriction")
public class Bootstrap {

    @Inject
    @Optional
    EModelService     _modelService;

    @Inject
    @Optional
    IEclipseContext   _eclipseContext;

    private DataBlock _master;
    private DataBlock _detail;

    @PostContextCreate
    void postContextCreate()
        throws ClassNotFoundException,
        SQLException
    {
        // Create oracle connection
        Class.forName("oracle.jdbc.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//vm/ecr4pres", "ec_calc", "calc");

        // Set the context
        _eclipseContext.set(Connection.class, connection);

        _master = DataBlock.createDataBlock(_eclipseContext, "v$session");
        _detail =
            DataBlock.createDataBlock(_eclipseContext,
                                      "select sid, names.name, stats.statistic#, stats.value from v$sesstat stats, v$statname names where names.Statistic# = stats.Statistic# order by stats.statistic#");
        _master.addDetailBlock(_detail, "sid = :sid");
    }

    private MPart createDatablockPart(DataBlock dataBlock) {
        MPart part = _modelService.createModelElement(MPart.class);
        part.setContributionURI(PartControler.getPluginId());
        IEclipseContext localContext = _eclipseContext.createChild();
        localContext.set(DataBlock.class, dataBlock);
        part.setContext(localContext);
        part.setLabel(dataBlock.getName());
        return part;

    }

    @ProcessAdditions
    void createAplicationModel(MApplication application) throws SQLException {

        // Find main window
        MTrimmedWindow window = (MTrimmedWindow) _modelService.find("cz.robotron.mainwindow", application);

        // Add Sash 
        MPartSashContainer sash = _modelService.createModelElement(MPartSashContainer.class);
        window.getChildren().add(sash);
        MPartStack masterStack = _modelService.createModelElement(MPartStack.class);
        MPartStack detailStack = _modelService.createModelElement(MPartStack.class);
        //        masterStack.set
        
        sash.getChildren().add(masterStack);
        sash.getChildren().add(detailStack);

        // Create part for master
        MPart masterPart = createDatablockPart(_master);
        MPart detailPart = createDatablockPart(_detail);

        masterStack.getChildren().add(masterPart);
        detailStack.getChildren().add(detailPart);
        

        _master.executeQuery();

    }

}
