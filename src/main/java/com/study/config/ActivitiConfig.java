package com.study.config;

import lombok.RequiredArgsConstructor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

//声名为配置类，继承Activiti抽象配置类
@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    private  DataSource dataSource;

    @Resource
    private PlatformTransactionManager activitiTransactionManager;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(){
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseType("oracle");
        configuration.setTransactionManager(activitiTransactionManager);
        configuration.setDatabaseSchemaUpdate("none");
        return  configuration;
    }
}