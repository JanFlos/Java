/** 
 * Copyright (c) Robotron Datenbanksoftware GmbH and/or its affiliates. All rights reserved.
 */
package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.google.common.eventbus.EventBus;

/**
 * @author Jan Flos
 *
 */
public class TestUtils {

    static ApplicationContext _appContext;
    private static Shell _shell;
    private static Display    _display;
    

    private static ApplicationContext createApplicationContext() throws ClassNotFoundException, SQLException {
        if (_appContext == null) {
            _appContext = new ApplicationContext();

            Class.forName("oracle.jdbc.OracleDriver");

            /* 
            System.setProperty("oracle.net.tns_admin", "c:\\Oracle\\Dev10g\\NETWORK\\ADMIN");
            _connection = DriverManager.getConnection("jdbc:oracle:thin:@ece", "ec_calc", "calc");
            */
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//vm/ecr4pres", "ec_calc", "calc");

            _appContext.setConnection(connection);
            _appContext.setEventBus(new EventBus());
        }
        return _appContext;
    }

    private static void createUiContext() {
        _display = new Display();
        _shell = new Shell(_display);

    }

    public static void startUI() {

        _shell.pack();
        _shell.open();

        while (!_shell.isDisposed()) {
            if (!_display.readAndDispatch())
                _display.sleep();
        }
        _display.dispose();

    }

    public static Shell getShell() {
        if (_shell == null)
            createUiContext();
        return _shell;
    }

    public static Display getDisplay() {
        if (_display == null)
            createUiContext();
        return _display;
    }

    public static ApplicationContext getAppContext() throws ClassNotFoundException, SQLException {
        if (_appContext == null)
            createApplicationContext();
        return _appContext;
    }

}

