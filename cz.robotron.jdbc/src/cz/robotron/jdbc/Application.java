package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Application implements IApplication {

	private DependencyInjectionSetupModule setupModule;
	private Connection fConnection;

	@Override
	public Object start(IApplicationContext context) throws Exception {

		Class.forName("oracle.jdbc.OracleDriver");
		fConnection = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:ecr4pres", "ec_calc", "calc");

		DataBlock dataBlock = new DataBlock();

		dataBlock.setConnection(fConnection);

		// Query The block
		/*
		 * dataBlock.setQueryDataSource("select * from test_table");
		 * dataBlock.executeQuery(); while (dataBlock.nextRecord()) {
		 * System.out.println(dataBlock.getValue(2)); }
		 * 
		 * // Set the current record dataBlock.goRecord(3);
		 */

		// Create record
		dataBlock.setQueryDataSource("select * from test_table");
		// dataBlock.createRecord();
		dataBlock.setValue(1, "33");
		dataBlock.commit();

		// Update record
		dataBlock.setValue(1, "24");
		dataBlock.commit();

		fConnection.close();
		/*
		 * Display display = new Display();
		 * 
		 * Shell shell = new Shell(display); shell.setLayout(new FillLayout());
		 * 
		 * final TableViewer v = new TableViewer(shell, SWT.BORDER |
		 * SWT.FULL_SELECTION); v.setLabelProvider(new
		 * TypedResultsetLabelProvider()); v.setContentProvider(new
		 * TypedResultSetContentProvider());
		 * 
		 * TableColumn column = new TableColumn(v.getTable(), SWT.NONE);
		 * column.setWidth(200); column.setText("Id"); column = new
		 * TableColumn(v.getTable(), SWT.NONE); column.setWidth(200);
		 * column.setText("Name");
		 * 
		 * setupModule = new DependencyInjectionSetupModule(); Injector injector
		 * = Guice.createInjector(setupModule);
		 * 
		 * TRSBerechnungen tds = injector.getInstance(TRSBerechnungen.class);
		 * tds.execute(); System.out.println(tds.getName());
		 * 
		 * v.setInput(tds); v.getTable().setLinesVisible(true);
		 * v.getTable().setHeaderVisible(true);
		 * 
		 * shell.open();
		 * 
		 * while (!shell.isDisposed()) { if (!display.readAndDispatch())
		 * display.sleep(); }
		 * 
		 * display.dispose();
		 * 
		 * setupModule.cleanup();
		 */
		return IApplication.EXIT_OK;

	}

	@Override
	public void stop() {
	}

}
