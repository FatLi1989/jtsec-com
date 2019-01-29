package com.jtsec.mc.dev.moitor.system.task.util;

public class SqlServerSystemSql {
	
	public static final String SELECT = "c.table_schema AS database_name,\n" +
										"\tc.table_name AS table_name,\n" +
										"\tc.column_name AS column_name,\n" +
										"\tc.data_type AS column_type,\n" +
										"\tc.CHARACTER_MAXIMUM_LENGTH AS data_length,\n" +
										"\tc.is_nullable AS column_key,\n" +
										"\tt.column_description AS column_comment";

	public static final String FROM =   "\tINFORMATION_SCHEMA.COLUMNS c\n" +
										"LEFT JOIN (\n" +
										"\tSELECT\n" +
										"\t\tA.name AS table_name,\n" +
										"\t\tB.name AS column_name,\n" +
										"\t\t\t(cast(isnull(c.[value],'') as nvarchar(100)))   as column_description \n" +
										"\tFROM\n" +
										"\t\tsys.tables A\n" +
										"\tINNER JOIN sys.columns B ON B.object_id = A.object_id\n" +
										"\tLEFT JOIN sys.extended_properties C ON C.major_id = B.object_id\n" +
										"\tAND C.minor_id = B.column_id\n" +
										") t ON t.column_name = c.column_name and t.table_name = c.table_name";
	
	public static final String COUNT =  "count(*)";

	
	public static final String ALTERTABLENAME = "alter table ";
	
}
