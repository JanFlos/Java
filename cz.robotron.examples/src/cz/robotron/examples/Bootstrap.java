package cz.robotron.examples;

import java.sql.SQLException;
import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

@SuppressWarnings("restriction")
public class Bootstrap {

    @Inject
    @Optional
    EModelService _modelService;

    @Inject
    @Optional
    MApplication  _application;

    @PostContextCreate
    void postContextCreate()
    {

    }

    @ProcessAdditions
    void createAplicationModel(MApplication application) throws SQLException {
        setUpExamplePart("_005_TreeWithViewerSupport", application);
    }

    private void setUpExamplePart(String exampleClass, MApplication application) {
        MPart part = (MPart) _modelService.find("cz.robotron.examples.mainpart", application);
        //cz.robotron.examples/cz.robotron.examples.parts._002_FilteredTreeAndXML
        //String contributionURI = part.getContributionURI();
        part.setContributionURI("bundleclass://cz.robotron.examples/cz.robotron.examples.parts." + exampleClass);

    }

}
