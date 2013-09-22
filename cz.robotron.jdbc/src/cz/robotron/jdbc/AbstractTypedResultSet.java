package cz.robotron.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.inject.Inject;

public class AbstractTypedResultSet {

	protected ResultSet fResultSet;
	protected String fSql;

	private Connection fConnection;
	private PreparedStatement fStatement;
	private int fClumnCount;

	@Inject
	public void setConnection(Connection fConnection) {
		this.fConnection = fConnection;
	}

	/*
	 * Setting the query
	 */
	protected void setQuery(String pSql) {
		fSql = pSql;
	}

	public void execute() throws ClassNotFoundException, SQLException {
		fStatement = fConnection.prepareStatement(fSql);
		fResultSet = fStatement.executeQuery();
		fClumnCount = fResultSet.getMetaData().getColumnCount();
		boolean next = next();
	}

	public boolean next() {
		try {
			return fResultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean previous() throws SQLException {
		return fResultSet.previous();
	}

	public Object[] toArray() {

		ArrayList<String[]> result = new ArrayList<String[]>();

		while (next()) {
			String[] row = new String[fClumnCount];
			for (int i = 0; i < fClumnCount; i++) {
				row[i] = getColumnValue(i + 1);
			}
			result.add(row);
		}
		return result.toArray();
	}

	private String getColumnValue(int i) {
		try {
			return fResultSet.getString(i);
		} catch (SQLException e) {
			return "error";
		}
	}
}
