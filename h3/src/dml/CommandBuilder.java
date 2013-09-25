package dml;

import java.util.List;
import base.ListUtils;

public class CommandBuilder {

	public static String insertCommand(MetadataProvider metadata) {

		String returningClause = "";
		List<String> returningColumns = metadata.getReturningColumnNames();
		if (returningColumns != null) {

            String lCols = ListUtils.join(returningColumns, ", ");
            String lDummy = ListUtils.join("?", ", ", returningColumns.size());

			returningClause = String.format(" returning %s into %s", lCols, lDummy);
		}

        List<String> readWriteColumns = metadata.getInsertableColumnNames();
        String readWriteColumnList = ListUtils.join(readWriteColumns, ", ");
        String parameterList = ListUtils.join("?", ", ", readWriteColumns.size());
		String tableName = metadata.getTableName();

		return String.format("{call insert into %s (%s) values (%s)%s}", tableName, readWriteColumnList, parameterList, returningClause);

	}

	public static String deleteCommand(MetadataProvider metadata) {

		String tableName = metadata.getTableName();
        String primaryKeyList = ListUtils.join(metadata.getPrimaryKeyColumnNames(), " and ", " = ?");

		return String.format("{call delete from %s where %s}", tableName, primaryKeyList);

	}

	public static String lockCommand(String tableName, List<String> primaryKey) {

        return String.format("select * from %s where %s for update", tableName, ListUtils.join(primaryKey, " and ", " = ?"));

	}

	public static String updateCommand(MetadataProvider metadata) {

		String tableName = metadata.getTableName();
        String updatedColumns = ListUtils.join(metadata.getUpdateableColumnNames(), ", ", " = ?");
        String primaryKeyList = ListUtils.join(metadata.getPrimaryKeyColumnNames(), " and ", " = ?");

		return String.format("{call update %s set %s where %s}", tableName, updatedColumns, primaryKeyList);
	}
}
