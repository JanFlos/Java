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

        _master = DataBlock.createDataBlock(_eclipseContext, "TTEST");
        _detail = DataBlock.createDataBlock(_eclipseContext, "TTS_DETAIL");
        _master.addDetailBlock(_detail, "tts_id = :id");
    }

    private MPart createDatablockPart(DataBlock dataBlock) {
        MPart part = _modelService.createModelElement(MPart.class);
        part.setLabel(dataBlock.getDMLTarget());
        part.setContributionURI(PartControler.getPluginId());
        IEclipseContext context = part.getContext();
        context.set(DataBlock.class, dataBlock);

        return part;

    }

    @ProcessAdditions
    void createAplicationModel(MApplication application) throws SQLException {



        // Find main window
        MTrimmedWindow window = (MTrimmedWindow) _modelService.find("cz.robotron.mainwindow", application);

        // Add Sash 
        MPartSashContainer sash = _modelService.createModelElement(MPartSashContainer.class);
        window.getChildren().add(sash);

        // Create part for master
        MPart part = createDatablockPart(_master);

        sash.getChildren().add(part);
        _master.executeQuery();
        /*
                MPartStack partStack = _modelService.createModelElement(MPartStack.class);

                MPart part2 = _modelService.createModelElement(MPart.class);
                part2.setLabel("kuna2");
                part2.setContributionURI("bundleclass://cz.robotron.expo/cz.robotron.expo.PartControler");

                MPart part3 = _modelService.createModelElement(MPart.class);
                part3.setLabel("kuna3");
                part3.setContributionURI("bundleclass://cz.robotron.expo/cz.robotron.expo.PartControler");

                sash.getChildren().add(partStack);
                partStack.getChildren().add(part2);
                partStack.getChildren().add(part3);
                part2.setCloseable(true);
                part3.setCloseable(true);

                MPartStack partStack2 = _modelService.createModelElement(MPartStack.class);
                sash.getChildren().add(partStack2);
                part2 = _modelService.createModelElement(MPart.class);
                part2.setLabel("kuna4");
                part2.setContributionURI("bundleclass://cz.robotron.expo/cz.robotron.expo.PartControler");
                partStack2.getChildren().add(part2);
        */
        //part.setVisible(true);

    }

}
