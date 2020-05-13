package com.xs.database.util;

import com.xs.database.config.CustomException;
import com.xs.database.config.ResultCode;
import com.xs.database.entity.ConnectionEntity;
import oracle.jdbc.driver.OracleDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @Author 薛帅
 * @Date 2019/3/28 16:30
 * @Description 数据库连接类 懒汉线程 安全 单例  /饿汉就是类加载的时候就创建实例
 */
public class ConnectionUtil {
    //饿汉模式
   // private static ConnectionUtil connectionUtil = new ConnectionUtil();

    //懒汉模式 用到的时候进行创建实例
    private static volatile ConnectionUtil connectionUtil;


    private static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

    private ConnectionUtil (){

    }

    public  static ConnectionUtil getConUtil(){
        return  getInstance();
    }

    private static ConnectionUtil getInstance(){
        if(connectionUtil ==null){
            synchronized (ConnectionUtil.class){
                if(connectionUtil==null){
                    connectionUtil = new ConnectionUtil();
                }
            }
        }
        return connectionUtil;
    }



    /**
     * 获取数据库连接
     * @param con
     * @return
     */
    public  Connection getCon (ConnectionEntity con) throws Exception{
        String userName = con.getUserName();
        int port = con.getPort();
        String password = con.getPassword();
        String ip = con.getIp();
        String dbName = con.getdataName();
        String dataType = con.getDataType();
        String url = "";
        switch (dataType){
            case "oracle":{
                OracleDriver driver = new OracleDriver();
                try {
                    DriverManager.registerDriver(driver);
                    url = "jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
                } catch (SQLException e) {
                    logger.error("oracle加载驱动错误：" + e);
                    throw  e;
                }
                break;
            }
            case "mysql":{
                Class.forName("com.mysql.cj.jdbc.Driver");
                url="jdbc:mysql://"+ip+":"+port+"/"+dbName;
                break;
            }
        }

       Connection connection =null;

        try {
            connection = DriverManager.getConnection(url,
                    userName,password);

        } catch (Exception e) {
            logger.error("创建连接错误：" + e);
            throw  new CustomException(ResultCode.RS_FAIL);

        }

        return  connection;
    }
    /**
     * 关闭connection
     */
    public static void  closeCon(Connection con) throws  Exception{
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("关闭数据连接异常："+e);
                throw  new CustomException(ResultCode.CN_FAIL);

            }
        }

    }
    /**
     * 关闭resultSet
     */
    public  static void closeRs(ResultSet resultSet) throws SQLException {
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error("关闭结果集连接异常："+e);
                throw  new CustomException(ResultCode.RS_FAIL);
            }
        }

    }
    /**
     * 关闭预编译
     */
    public static  void  closePre(PreparedStatement pre) throws SQLException {
        if(pre!=null){
            try {
                pre.close();
            } catch (SQLException e) {
                logger.error("关闭预编译连接异常："+e);
                throw new CustomException(ResultCode.PRE_FAIL);
            }
        }

    }


}
