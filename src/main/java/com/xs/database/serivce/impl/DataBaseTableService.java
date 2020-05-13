package com.xs.database.serivce.impl;

import com.xs.database.config.CustomException;
import com.xs.database.config.ResultCode;
import com.xs.database.controller.DataBaseTableController;
import com.xs.database.entity.ColumnEntity;
import com.xs.database.entity.ConnectionEntity;
import com.xs.database.entity.DBTableEntity;
import com.xs.database.serivce.IDataBaseTableService;
import com.xs.database.util.ConnectionUtil;
import com.xs.database.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 薛帅
 * @Date 2019/3/25 20:24
 * @Description
 */
@Service("DataBaseTableService")
public class DataBaseTableService implements IDataBaseTableService {

    private Logger logger = LoggerFactory.getLogger(DataBaseTableController.class);
    @Cacheable(cacheNames="table", key="#root.args[0].ip+#root.args[0].tableName+#root" +
            ".args[0].encode")
    public List<DBTableEntity> getTableList(ConnectionEntity conEntity)throws Exception   {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<DBTableEntity> list = null;
        long stime = 0;
        ConnectionUtil conUtil = ConnectionUtil.getConUtil();
        final  Connection con = conUtil.getCon(conEntity);
        final  String dataType = conEntity.getDataType();
        String tableNamePara = conEntity.getTableName();
        String dbName = conEntity.getdataName();
        String encode = conEntity.getEncode();
        String sb = SqlUtil.queryTableSql(dataType,tableNamePara,dbName);
        logger.info("查询表sql"+sb+"");
        try {
            statement = con.prepareStatement(sb + "",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            if("mysql".equals(dataType)){
                statement.setFetchSize(Integer.MIN_VALUE);
            }else{
                statement.setFetchSize(1000);
            }
            long startTime = System.currentTimeMillis();
            resultSet = statement.executeQuery();
            long endTime = System.currentTimeMillis();
            logger.info("结束执行sql,用时：" + (endTime - startTime));
            list = new ArrayList<>();
            stime = System.currentTimeMillis();
            double i = 0;
            while (resultSet.next()) {
                DBTableEntity dbInfo = new DBTableEntity();
                dbInfo.setTableName(resultSet.getString("TABLE_NAME"));
                String comments = resultSet.getString("COMMENTS");
                System.out.println(resultSet.getString("TABLE_NAME"));
                if(comments ==null){
                    dbInfo.setTableNameCn("");
                }else /*if("UTF-8".equals(encode))*/{
                    dbInfo.setTableNameCn(new String(comments.getBytes(
                            "ISO-8859-1"),"GBK"));
                }
//                else {
//                    dbInfo.setTableNameCn(new String(comments.getBytes(
//                            encode),"UTF-8"));
//                }
                list.add(dbInfo);
            }
            logger.info("总共" + list.size() + "张表");
           /* final int threadCount = 2;
            class task implements Callable<String>{
                private  int i;
                private  List<DBTableEntity> list;
                public task() {
                }

                public task(int i,  List<DBTableEntity> list) {
                    this.i = i;
                    this.list = list;
                }

                @Override
                public String call() throws Exception {
                    logger.info("进入call方法");
                    for (int j = list.size()/threadCount*i; j <list.size()/threadCount*(i+1) ; j++) {
                        List<Map> lm=getCol(list.get(j).getTableName(), con,dataType);
                        list.get(j).setcolumnEntity(lm);
                    }
                    logger.info("结束call方法");
                    return "ok";
                }
            }
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            for (int j = 0; j <threadCount ; j++) {
                logger.info("开始更新字段");
                String s = executorService.submit(new task(j, list)).get();
                if(!"ok".equals(s)){
                    logger.info("多线程执行更新表字段失败");
                    throw new CustomException(ResultCode.TASK_FAIL);
                }
            }*/

            //多线程处理  TODO
//                list  ---%size  --fenzu
            for (DBTableEntity dbInfo : list) {
                List<ColumnEntity> lm = getCol(dbInfo.getTableName(), con,dataType);
                dbInfo.setcolumnEntity(lm);
            }
        } catch (CustomException e) {
            e.printStackTrace();
           throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("转码失败");
            throw  new CustomException(ResultCode.ENCODE_FAIL);
        } finally {
            ConnectionUtil.closeRs(resultSet);
            ConnectionUtil.closePre(statement);
            ConnectionUtil.closeCon(con);
        }
        long etime = System.currentTimeMillis();
        logger.info("结束添加列表,相差时间： " + (double)(etime - stime) / 60000 + "min");
        logger.info("创建表个数： "+list.size());
        return list;
    }

    @Override
    public boolean testCon(ConnectionEntity conEntity) {
        return false;
    }

    /* 根据数据库名称获取数据表字段信息
     *
             * @param tableName
     * @param con
     * @return
             */
    public List<ColumnEntity> getCol(String tableName, Connection con,String dataType) throws Exception {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ColumnEntity> list = new ArrayList<>();
        String s = SqlUtil.queryColSql(tableName,dataType);
        try {
            statement = con.prepareStatement(s + "",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ColumnEntity columnEntity = new ColumnEntity();
                columnEntity.setColumnName(resultSet.getString("COLUMN_NAME"));
                String columnNameCn = resultSet.getString("column_name_cn");
                if(columnNameCn ==null){
                    columnEntity.setColumnNameCn("");
                }else{
                    columnEntity.setColumnNameCn(new String(resultSet.getString("column_name_cn").getBytes(
                            "ISO-8859-1"),"GBK"));
                }
                columnEntity.setDataLength(resultSet.getInt("DATA_LENGTH"));
                columnEntity.setDataType(resultSet.getString("DATA_TYPE"));
                columnEntity.setNullAble(resultSet.getString("NULLABLE"));
                list.add(columnEntity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("执行查询字段sql错误");
            throw  new  CustomException(ResultCode.SQL_FAIL);


        } finally {
            ConnectionUtil.closeRs(resultSet);
            ConnectionUtil.closePre(statement);
        }

        return list;
    }
}
