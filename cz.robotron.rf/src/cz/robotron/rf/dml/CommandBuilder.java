package cz.robotron.rf.dml;

import java.util.List;
import cz.robotron.rf.CollectionUtils;

public class CommandBuilder {

	public static String insertCommand(MetadataProvider metadata) {

		String returningClause = "";
		List<String> returningColumns = metadata.getReturningColumnNames();
		if (returningColumns != null) {

            String lCols = CollectionUtils.join(returningColumns, ", ");
            String lDummy = CollectionUtils.join("?", ", ", returningColumns.size());

			returningClause = String.format(" returning %s into %s", lCols, lDummy);
		}

        List<String> readWriteColumns = metadata.getInsertableColumnNames();
        String readWriteColumnList = CollectionUtils.join(readWriteColumns, ", ");
        String parameterList = CollectionUtils.join("?", ", ", readWriteColumns.size());
		String tableName = metadata.getDmlTargetTableName();

		return String.format("{call insert into %s (%s) values (%s)%s}", tableName, readWriteColumnList, parameterList, returningClause);

	}

	public static String deleteCommand(MetadataProvider metadata) {

		String tableName = metadata.getDmlTargetTableName();
        String primaryKeyList = CollectionUtils.join(metadata.getPrimaryKeyColumnNames(), " and ", " = ?");

		return String.format("{call delete from %s where %s}", tableName, primaryKeyList);

	}

	public static String lockCommand(String tableName, List<String> primaryKey) {

        return String.format("select * from %s where %s for update", tableName, CollectionUtils.join(primaryKey, " and ", " = ?"));

	}

	public static String updateCommand(MetadataProvider metadata) {

		String tableName = metadata.getDmlTargetTableName();
        String updatedColumns = CollectionUtils.join(metadata.getUpdateableColumnNames(), ", ", " = ?");
        String primaryKeyList = CollectionUtils.join(metadata.getPrimaryKeyColumnNames(), " and ", " = ?");

		return String.format("{call update %s set %s where %s}", tableName, updatedColumns, primaryKeyList);
	}
}
