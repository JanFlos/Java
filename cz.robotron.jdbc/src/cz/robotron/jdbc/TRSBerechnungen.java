package cz.robotron.jdbc;

import java.sql.SQLException;

public class TRSBerechnungen extends AbstractTypedResultSet {

	private final String ID = "id";
	private final String NAME = "name";

	public TRSBerechnungen() {
		setQuery("select ber_id, name from berechnungen where rownum <= 1000");
		Object i = null;

	}

	/*
	 * Getters
	 */
	Long getId() throws SQLException {
		return fResultSet.getLong(ID);
	}

	String getName() throws SQLException {
		return fResultSet.getString(NAME);
	}

	/*
	 * Setters
	 */

	public void setId(Long pId) throws SQLException {
		fResultSet.updateLong(ID, pId);
	}

	public void setName(String pName) throws SQLException {
		fResultSet.updateString(NAME, pName);
	}

	public void finalze() throws SQLException {
		fResultSet.close();
	}
}
