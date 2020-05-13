package com.xs.database.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 薛帅
 * @Date 2019/4/27 9:28
 * @Description 多数据源，切换并被spring所使用（AbstractRoutingDataSource 在getConnection中起作用）
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    static ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
    //默认增加 默认的数据源
//    static {
//        map.put("defaultDataSource",IocUtil.getBean("defaultDataSource"));
//    }
    public DynamicDataSource() {
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return "dynamic";
    }

    public static void  setDataSource(DataSource ds){
        DynamicDataSource dataSource = (DynamicDataSource)IocUtil.getBean(
                "dynamicDataSource");
         Map m = new ConcurrentHashMap<>();
         m.put("dynamic",ds);
        map.putAll(m);
        try {
            dataSource.setTargetDataSources(map);
            dataSource.afterPropertiesSet();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
