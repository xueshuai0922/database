package com.xs.database.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author 薛帅
 * @Date 2020/3/25 20:24
 * @Description
 */

public class DBTableEntity implements Serializable {
    private String tableName;
    private String tableNameCn;
    private List<ColumnEntity> columnEntity;
   

    public List<ColumnEntity> getcolumnEntity() {
        return columnEntity;
    }

    public void setcolumnEntity(List<ColumnEntity> columnEntity) {
        this.columnEntity = columnEntity;
    }


    public DBTableEntity(){

    }
    

    public DBTableEntity(String tableName, String tableNameCn, String columnName, String columnNameCn, String dataType, int dataLength, String nullAbleStatus) {
        this.tableName = tableName;
        this.tableNameCn = tableNameCn;
    }



    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNameCn() {
        return tableNameCn;
    }

    public void setTableNameCn(String tableNameCn) {
        this.tableNameCn = tableNameCn;
    }
    @Override
    public String toString() {
        return "DBTableEntity{" +
                "tableName='" + tableName + '\'' +
                ", tableNameCn='" + tableNameCn + '\'' +
                ", columnEntity=" + columnEntity +
                '}';
    }
    

}
