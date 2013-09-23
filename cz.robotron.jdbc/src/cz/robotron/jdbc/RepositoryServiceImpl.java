package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryServiceImpl {

	public void kuna() throws ClassNotFoundException, SQLException {

		Class.forName("oracle.jdbc.OracleDriver");
		String url = "jdbc:oracle:thin:@rdslinux25.robotron.de:1521:ecr5e11";
		Connection conn = DriverManager.getConnection(url, "ec_calc", "calc");

	}
}
