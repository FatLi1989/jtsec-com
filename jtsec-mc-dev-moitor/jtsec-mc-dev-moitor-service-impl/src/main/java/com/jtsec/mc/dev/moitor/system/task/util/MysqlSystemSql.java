package com.jtsec.mc.dev.moitor.system.task.util;

public class MysqlSystemSql {
	
	public static final String COUNT  = "count(*)";
	
	public static final String SELECT = "`tb`.`TABLE_SCHEMA` AS `database_name`,"+ 
										"`tb`.`TABLE_NAME` AS `table_name`,"+ 
										"`col`.`COLUMN_NAME` AS `column_name`,"+
										" substring_index(`col`.`COLUMN_TYPE`, '(', 1) AS `column_type`,"+ 	
										" substring_index(substring_index(`col`.`COLUMN_TYPE`,'(' ,- (1)),')',1) AS `data_length`,"+ 
										"`col`.`COLUMN_KEY` AS `column_key`,"+
										"`col`.`COLUMN_COMMENT` AS `column_comment`";
	
	public static final String FROM = "`information_schema`.`tables` `tb`"+ 
									  "LEFT JOIN `"+
									  "information_schema`.`columns` `col` ON `tb`.`TABLE_NAME` = `col`.`TABLE_NAME`"+ 
									  "AND `tb`.`TABLE_SCHEMA` = `col`.`TABLE_SCHEMA`";
	
	
	public static final String ALTERTABLENAME = "alter table ";
	
	
	
}
