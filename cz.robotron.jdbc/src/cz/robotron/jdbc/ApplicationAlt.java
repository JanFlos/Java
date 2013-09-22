package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ApplicationAlt implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {

		Class.forName("oracle.jdbc.OracleDriver");
		// String url = "jdbc:oracle:thin:@localhost:1521:ecr4pres";
		String url = "jdbc:oracle:thin:@rdslinux25.robotron.de:1521:ecr5e11";

		// creating connection to Oracle database using JDBC
		Connection conn = DriverManager.getConnection(url, "ec_calc", "calc");

		String sql = "select * from berechnungen where rownum <= 10";

		// creating PreparedStatement object to execute query
		PreparedStatement preStatement = conn.prepareStatement(sql);

		ResultSet result = preStatement.executeQuery();

		// Metadata holen
		ResultSetMetaData resultSetMetaData = result.getMetaData();
		printMetadata(resultSetMetaData);

		/*
		 * Durchloopen eines resultsets while (result.next()) {
		 * System.out.println("Aktuální datum Oracle : " +
		 * result.getString("current_day")); } System.out.println("done");
		 */
		return IApplication.EXIT_OK;
	}

	private void printMetadata(ResultSetMetaData resultSetMetaData)
			throws SQLException {
		int count = resultSetMetaData.getColumnCount();

		for (int i = 1; i <= count; i++) {
			// System.out.println(resultSetMetaData.getColumnLabel(i));
			System.out.printf("%-15s %-15s %s \n",
					resultSetMetaData.getColumnLabel(i),
					resultSetMetaData.getColumnTypeName(i),
					resultSetMetaData.getColumnClassName(i));
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
