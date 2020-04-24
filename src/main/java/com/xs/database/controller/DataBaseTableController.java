package com.xs.database.controller;

import com.xs.database.config.CommonResult;
import com.xs.database.config.CustomException;
import com.xs.database.entity.ConnectionEntity;
import com.xs.database.serivce.IDataBaseTableService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 薛帅
 * @Date 2020/3/25 20:23
 * @Description
 */
@Controller

public class DataBaseTableController {
    @Autowired
    @Qualifier("MybatisTableService")
    IDataBaseTableService service;

    @Autowired
    DataSourceProperties sourceProperties;

    private static final String MY_SQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private Logger logger = LoggerFactory.getLogger(DataBaseTableController.class);



    //todo  nginx静态分离
    @PostMapping("/getTableList")
    @ResponseBody
    public CommonResult<List> getTableList(@RequestBody ConnectionEntity conEntity)  {
        try {
             return CommonResult.success(service.getTableList(conEntity)) ;
        } catch (CustomException e) {
            logger.error(e.getErrorMsg());
            return CommonResult.failed(e.getErrorMsg());
        } catch (Exception e) {
            System.out.println("打印错误信息");
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
    @PostMapping("/testConnection")
    @ResponseBody
    public CommonResult testConnection(@RequestBody ConnectionEntity conEntity){
        return  CommonResult.success(service.testCon(conEntity));
    }
    @RequestMapping("/")
    public String get()  {
        return "index2";
    }
}