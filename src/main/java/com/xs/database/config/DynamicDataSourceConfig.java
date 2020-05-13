package com.xs.database.config;

import com.xs.database.handler.BatisEncodeHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.DefaultResourceLoader;

import javax.sql.DataSource;


/**
 * @Author 薛帅
 * @Date 2019/4/27 14:53
 * @Description  DynamicDataSource配置类注册到spring容器中去
 */
@Configuration
@DependsOn("dataBaseTableController")
@MapperScan(basePackages="com.xs.database.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
public class DynamicDataSourceConfig{

    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.password}")
    private  String password;
    @Autowired
    BatisEncodeHandler batisEncodeHandler;


    /**/
    @Bean
    public  DynamicDataSource dynamicDataSource(){
        DynamicDataSource dataSource = new DynamicDataSource();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPassword(password);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(userName);
        hikariConfig.setDriverClassName(driver);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        dataSource.map.put("defaultDatasource",
                hikariDataSource);
        //继承abstractRoutingDataSource类初始化bean会验证targetDataSource 不为空
        dataSource.setTargetDataSources(dataSource.map);
        return dataSource;
    }
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration =
                new org.apache.ibatis.session.Configuration();

        LocalCacheScope localCacheScope =  LocalCacheScope.STATEMENT;
        configuration.setLocalCacheScope(localCacheScope);
        configuration.setCallSettersOnNulls(true);
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        //1.mybatis自动导入sqlsessionfactory时候，会注册一个handler，后面resultMap
        // 解析的时候，会验证是否存在typeHandler
        //如果存在，则不创建，不存在，会解析xml中的所有typeHandler（这样会导致创建多个typeHandler对象）
//        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
//        typeHandlerRegistry.register(batisEncodeHandler.getClass());
        factory.setTypeHandlers(batisEncodeHandler);
        factory.setConfiguration(configuration);
        factory.setMapperLocations(new DefaultResourceLoader(this.getClass().getClassLoader()).getResource("static/xml/DataBaseTableMapper.xml"));
        return  factory.getObject();
    }





    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
