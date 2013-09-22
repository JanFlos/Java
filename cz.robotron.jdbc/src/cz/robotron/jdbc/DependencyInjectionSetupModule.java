package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DependencyInjectionSetupModule extends AbstractModule {

	private Connection fConnection;

	@Override
	protected void configure() {

		/*
		 * bind(String.class).annotatedWith(Names.named(Constants.JDBC_URL))
		 * .toInstance(Constants.CONNECTION_STRING);
		 */

	}

	@Provides
	Connection getConnection() throws ClassNotFoundException, SQLException {

		if (fConnection == null) {

			Class.forName("oracle.jdbc.OracleDriver");
			// String url =
			// "jdbc:oracle:thin:@rdslinux25.robotron.de:1521:ecr5e11";
			String url = "jdbc:oracle:thin:@localhost:1521:ecr4pres";

			fConnection = DriverManager.getConnection(url, "ec_calc", "calc");
		}

		return fConnection;
	}

	void cleanup() throws SQLException {
		if (fConnection != null) {
			fConnection.close();
		}

	}

}
