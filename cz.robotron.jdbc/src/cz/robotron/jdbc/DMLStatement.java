/*************************************************************************
 * Copyright (c) 2013 Robotron Database solutions (http://www.robotron.cz)
 * All rights reserved. 
 *************************************************************************/
package cz.robotron.jdbc;

import java.sql.Connection;

/*
 * This class is responsible for generating callable CRUID statements
 * for DML Operation based on datablock records
 */
public class DMLStatement {

	private SQLDataSourceMetadata fMetaData;
	private Connection fConnection;

	public DMLStatement(SQLDataSourceMetadata metaData, Connection connection) {
		this.fMetaData = metaData;
		this.fConnection = connection;

	}

	public void setVariables(IDataBlockRecord record) {
		// TODO Set Variables of the record

		registerOutputVariables(record); // Register output variables

	}

	private void registerOutputVariables(IDataBlockRecord record) {
		// TODO Auto-generated method stub

	}

	public IDataBlockRecord execute() {
		return null;
		// TODO Auto-generated method stub

	}

	public String getInsertSQLCommand() throws Exception {
		return String
				.format("{call INSERT INTO %1$s (%2$s) VALUES (%3$s) RETURNING %2$s INTO %3$s }",
						fMetaData.getTableName(),
						fMetaData.getCommaSeparatedColumnList(),
						fMetaData.getCommaSeparatedPlaceholders());
	}

}
