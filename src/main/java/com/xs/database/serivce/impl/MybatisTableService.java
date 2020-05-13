package com.xs.database.serivce.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.xs.database.entity.DBTableEntity;
import com.xs.database.config.CustomException;
import com.xs.database.config.DynamicDataSource;
import com.xs.database.config.ResultCode;
import com.xs.database.entity.ConnectionEntity;
import com.xs.database.handler.BatisEncodeHandler;
import com.xs.database.mapper.DataBaseTableMapper;
import com.xs.database.serivce.IDataBaseTableService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.driver.OracleDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Driver;
import java.util.List;

/**
 * @Author 薛帅
 * @Date 2019/4/10 13:30
 * @Description
 */
@Service("MybatisTableService")
public class MybatisTableService implements IDataBaseTableService {
    @Autowired
    DataBaseTableMapper mapper;

    @Autowired
    BatisEncodeHandler batisEncodeHandler;

    private Logger logger = LoggerFactory.getLogger(MybatisTableService.class);

    @Override
    public List<DBTableEntity> getTableList(ConnectionEntity conEntity) throws Exception {
        try {
            reSetDB(conEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResultCode.FAILED.getCode(), "数据库配置错误，请检查数据库信息！");
        }
        //中文乱码处理
        batisEncodeHandler.setCode(conEntity.getEncode());
        String dbType = conEntity.getDataType();

        String tableName = StringUtils.isEmpty(conEntity.getTableName())?"":
                "%" + conEntity.getTableName().toUpperCase() +
                        "%";
        List<DBTableEntity> tableList = null;
        if ("mysql".equals(dbType)) {
            tableList = mapper.getTableListByMysql(tableName);
        } else if ("oracle".equals(dbType)) {
            tableList = mapper.getTableListByOracle(tableName);
        }
        return tableList;
    }

    @Override
    public boolean testCon(ConnectionEntity conEntity) {
        try {
            reSetDB(conEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResultCode.FAILED.getCode(), "数据库配置错误，请检查数据库信息！");
        }
        return mapper.testCon()>0?true:false;
    }


    private void reSetDB(ConnectionEntity conEntity) throws Exception {
        String driver = null;
        String url = "";
        /*重新获取DataSource bean,然后设置数据库信息*/
        if ("mysql".equals(conEntity.getDataType())) {
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + conEntity.getIp() + ":"
                    + conEntity.getPort() + "/" + conEntity.getdataName();
        } else if ("oracle".equals(conEntity.getDataType())) {
            driver = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@" + conEntity.getIp() + ":"
                    + conEntity.getPort() + ":" + conEntity.getdataName();

        }
        try{
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPassword(conEntity.getPassword());
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(conEntity.getUserName());
            hikariConfig.setDriverClassName(driver);
            HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
            DynamicDataSource.setDataSource(hikariDataSource);
        }catch (Exception e){
            throw e;
        }

    }
}

