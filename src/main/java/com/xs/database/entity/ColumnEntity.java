package com.xs.database.entity;

import java.io.Serializable;

/**
 * @Author 薛帅
 * @Date 2019/4/14 16:03
 * @Description 字段实体类 要记得序列化
 */
public class ColumnEntity implements Serializable {
    private  String   columnName;
    private  String  columnNameCn;
    private  String  dataType;
    private  String  nullAble;
    private String defaultData;


    @Override
    public String toString() {
        return "ColumnEntity{" +
                "columnName='" + columnName + '\'' +
                ", columnNameCn='" + columnNameCn + '\'' +
                ", dataType='" + dataType + '\'' +
                ", nullAble='" + nullAble + '\'' +
                ", defaultData='" + defaultData + '\'' +
                ", dataLength=" + dataLength +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    private  Integer dataLength;
    private  String  tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameCn() {
        return columnNameCn;
    }

    public void setColumnNameCn(String columnNameCn) {
        this.columnNameCn = columnNameCn;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNullAble() {
        return nullAble;
    }

    public void setNullAble(String nullAble) {
        this.nullAble = nullAble;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }
    public String getDefaultData() {
        return defaultData;
    }

    public void setDefaultData(String defaultData) {
        this.defaultData = defaultData;
    }
}
