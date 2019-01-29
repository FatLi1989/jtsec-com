package com.jtsec.mc.dev.moitor.system.task.util;

public class OracleSystemSql {
	
	public static final String SELECT = "T.database_name AS database_name,"+
										"T.table_name AS table_name,"+ 
										"T.column_name AS column_name,"+ 
										"T.column_type AS column_type,"+ 
										"T.data_length AS data_length,"+ 
										"b.constraint_type AS column_key,"+
										"T.column_comment AS column_comment";
	
	public static final String FROM = /*"("+ 
									  	"SELECT"+ 
										  	"UB.tablespace_name AS database_name,"+
										  	"UTC.table_name AS table_name,"+ 
										  	"UTC.column_name AS column_name,"+
										    "UTC.data_type AS column_type,"+ 
										    "utc.data_length AS data_length,"+ 
										    "ucc.comments AS column_comment"+
									    " FROM"+ 
										    "user_tables ub"+ 
										    "LEFT JOIN user_tab_columns utc  ON ub.table_name = UTC.table_name"+ 
										    "LEFT JOIN user_col_comments ucc ON utc.column_name = ucc.column_name"+ 
										    " AND  utc.table_name = ucc.table_name) T"+
										" LEFT JOIN"+
										    "("+ 
								    	        "SELECT"+ 
												    "UCC.table_name AS table_name,"+ 
												    "ucc.column_name AS column_name,"+ 
												    "wm_concat (UC.constraint_type) AS constraint_type"+ 
											    "FROM"+ 
												 	"user_cons_columns ucc"+ 
												 	"LEFT JOIN user_constraints uc ON UCC.constraint_name = UC.constraint_nam"+ 
												 "GROUP BY"+ 
												 	"UCC.table_name,ucc.column_name) b"+																			 
										 "ON  T .table_name  = b.TABLE_NAME"+ 
										 " AND T .column_name = b.column_name";*/
										"(\r\n" + 
										"		SELECT\r\n" + 
										"			UB.tablespace_name AS database_name,\r\n" + 
										"			UTC.table_name AS table_name,\r\n" + 
										"			UTC.column_name AS column_name,\r\n" + 
										"			UTC.data_type AS column_type,\r\n" + 
										"			utc.data_length AS data_length,\r\n" + 
										"			ucc.comments AS column_comment\r\n" + 
										"		FROM\r\n" + 
										"			user_tables ub\r\n" + 
										"		LEFT JOIN user_tab_columns utc ON ub.table_name = UTC.table_name\r\n" + 
										"		LEFT JOIN user_col_comments ucc ON utc.column_name = ucc.column_name\r\n" + 
										"		AND utc.table_name = ucc.table_name\r\n" + 
										"	) T\r\n" + 
										"LEFT JOIN (\r\n" + 
										"	SELECT\r\n" + 
										"		UCC.table_name AS table_name,\r\n" + 
										"		ucc.column_name AS column_name,\r\n" + 
										"		wm_concat (UC.constraint_type) AS constraint_type\r\n" + 
										"	FROM\r\n" + 
										"		user_cons_columns ucc\r\n" + 
										"	LEFT JOIN user_constraints uc ON UCC.constraint_name = UC.constraint_name\r\n" + 
										"	GROUP BY\r\n" + 
										"		UCC.table_name,\r\n" + 
										"		ucc.column_name\r\n" + 
										") b ON T .table_name = b.TABLE_NAME\r\n" + 
										"AND T .column_name = b.column_name\r\n" ;
	
	public static final String COUNT = "count(*)";
	
	public static final String ALTERTABLENAME = "alter table ";
}
