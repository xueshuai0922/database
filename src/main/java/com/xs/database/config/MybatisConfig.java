package com.xs.database.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 薛帅
 * @Date 2020/3/25 20:26
 * @Description
 */
@Configuration
@MapperScan("com.xs.database.mapper")
@EnableTransactionManagement
public class MybatisConfig {
}
