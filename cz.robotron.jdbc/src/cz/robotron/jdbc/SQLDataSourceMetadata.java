package cz.robotron.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SQLDataSourceMetadata {

	private int fColumnCount;
	private String[] fColumns;
	private int[] fColumnSQLTypes;
	private String fCommaSeparatedPlaceholders;
	private String fCommaSeparatedColumns;

	// Constructs Metadata by Queried Resultset
	public SQLDataSourceMetadata(ResultSetMetaData pMetaData)
			throws SQLException {

		fCommaSeparatedPlaceholders = null;
		fCommaSeparatedColumns = null;

		fColumnCount = pMetaData.getColumnCount();

		fColumns = new String[fColumnCount];
		fColumnSQLTypes = new int[fColumnCount];

		for (int i = 0; i < fColumnCount; i++) {
			fColumns[i] = pMetaData.getColumnName(i + 1);
			fColumnSQLTypes[i] = pMetaData.getColumnType(i + 1);
		}

	}

	public String[] getColumns() {
		return fColumns;
	}

	public int getColumnSQLType(int i) {
		assert i > 0 && i <= fColumnCount : String.format(
				"Columns Index %s doen't exists", i);

		return fColumnSQLTypes[i - 1];
	}

	public Object getCommaSeparatedColumnList() {

		if (fCommaSeparatedColumns == null) {

			if (fColumnCount > 0) {
				fCommaSeparatedColumns = fColumns[0];

				for (int i = 1; i < fColumns.length; i++) {
					fCommaSeparatedColumns += ", " + fColumns[i];
				}
				return fCommaSeparatedColumns;
			}

		}

		return fCommaSeparatedPlaceholders;
	}

	public Object getCommaSeparatedPlaceholders() {

		if (fCommaSeparatedPlaceholders == null) {

			if (fColumnCount > 0) {
				fCommaSeparatedPlaceholders = "?";
				for (int i = 1; i < fColumns.length; i++) {
					fCommaSeparatedPlaceholders += ", ?";
				}
				return fCommaSeparatedPlaceholders;
			}

		}

		return fCommaSeparatedPlaceholders;
	}

	public int getColumnCount() {
		return fColumnCount;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
