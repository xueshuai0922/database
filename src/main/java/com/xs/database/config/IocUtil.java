package com.xs.database.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author 薛帅
 * @Date 2019/4/27 14:55
 * @Description ioc上下文的工具类：实现ApplicationContextAware 获取在容器中的bean
 */

@Component
public class IocUtil implements ApplicationContextAware {



    private static ApplicationContext  applicationContext;

    public static ApplicationContext getBaseApplicationContext() {
        return applicationContext;
    }



    public static  Object getBean(String name){
        return applicationContext.getBean(name);

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(this.applicationContext==null){
            this.applicationContext= applicationContext;
        }
    }
}
