package com.xs.database.entity;

/**
 * @Author 薛帅
 * @Date 2019/3/25 21:31
 * @Description
 */
public class ConnectionEntity {
    private String ip;
    private int port;
    private String userName;
    private String password;
    private String dataName;
    private String dataType;
    private String tableName;
    private String encode;

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getdataName() {
        return dataName;
    }

    public void setdataName(String dataName) {
        this.dataName = dataName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
