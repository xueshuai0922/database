package com.xs.database.config;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @Author 薛帅
 * @Date 2019/4/14 13:02
 * @Description 日志记录和有自定义注解的方法 后续功能扩展使用
 */

//@Component
    //todo 1.拦截器，监听器 示例        2.动态数据源map操作threadLocal
    //todo 3.spring注入bean循环依赖问题  4.手写快排
    //todo 5.@transactional 无效问题   6.数据库表结构工具上线，小程序整个流程走通 https://www.cnblogs.com/hei12138/p/mybatis-spring.html
@Aspect

public class AopConfig {
        //增加一个日志记录器slf4j
    private final Logger logger = LoggerFactory.getLogger(AopConfig.class);

//    @before
    //切点
    //切点之前
    //切点之后
public static void main(String[] args) {
    String s ="";
    for (int i = 0; i < 3; i++) {
       s+=i+",";
    }
    System.out.println(s.substring(0,s.length()-1));
}


}
