package com.xs.database.util;

import org.springframework.util.StringUtils;

/**
 * @Author 薛帅
 * @Date 2019/3/29 13:14
 * @Description 根据数据库类型不同，查询不同表sql
 */
public class SqlUtil {
    /**
     * 查询数据库表
     * @param dataType
     * @return
     */
    public static String queryTableSql(String dataType,String tableNamePara,
                                       String dbName) {
        StringBuilder sb = new StringBuilder();
        switch (dataType) {
            case "oracle": {
                sb.append(
                        "SELECT S.TABLE_NAME, T.COMMENTS\n" +
                                "  FROM USER_TABLES S, USER_TAB_COMMENTS T\n" +
                                " WHERE S.TABLE_NAME = T.TABLE_NAME(+) \n" +
                                "  and t.table_type = 'TABLE'  \n"

                );
                if(!StringUtils.isEmpty(tableNamePara)){
                    sb.append("and s.table_name like '%"+tableNamePara+"%'");
                }
                break;
            }
            case "mysql": {
                sb .append( "select " +
                        "table_name TABLE_NAME, " +
                        "table_comment COMMENTS " +
                        "from " +
                        "information_schema.tables " +
                        "where table_schema='" + dbName   + "'"
//                      and table_type='base table' \n"
                );

                if(!StringUtils.isEmpty(tableNamePara)){
                    sb.append("and table_name like '%"+tableNamePara+"%'");
                }
                break;
            }

        }


        return sb + "";
    }

    /**
     * 查询字段sql
     * @param tableName
     * @param dataType
     * @return
     */
    public static String queryColSql(String tableName,String dataType) {
        StringBuilder s = new StringBuilder();
        switch (dataType) {
            case "oracle": {
                s.append("select  s.COLUMN_NAME,\n" +
                        "         c.comments as column_name_cn,\n" +
                        "         s.DATA_TYPE,\n" +
                        "         s.DATA_DEFAULT,--long\n" +
                        "         s.NULLABLE,\n" +
                        "         s.DATA_LENGTH --number\n" +
                        "    from user_tab_columns S\n" +
                        "      left join user_col_comments c\n" +
                        "      on c.table_name = s.TABLE_NAME\n" +
                        "      and c.column_name = s.COLUMN_NAME\n"
                        + " where s.table_name = '" + tableName + "'");
                break;
            }
            case "mysql":{
                s.append(
                        "select column_name COLUMN_NAME," +
                                "column_default DATA_DEFAULT," +
                                "is_nullable NULLABLE," +
                                "data_type DATA_TYPE," +
                                "character_maximum_length DATA_LENGTH," +
                                "column_comment column_name_cn  " +
                                "from information_schema.columns " +
                                "where table_name = '"+tableName+"' " +
                                "and table_schema = (select database()) order by " +
                                "ordinal_position");
                break;
            }
        }
        return s + "";
    }
}
