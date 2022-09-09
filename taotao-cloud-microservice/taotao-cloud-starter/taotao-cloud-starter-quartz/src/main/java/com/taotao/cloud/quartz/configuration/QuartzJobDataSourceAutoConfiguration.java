package com.taotao.cloud.quartz.configuration;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

/**
 * 为Quartz单独配置数据源
 * 有时候我们会希望将quartz的相关表保存在单独的一个数据库中，从而与业务相关的表分开。
 */
@Configuration
public class QuartzJobDataSourceAutoConfiguration {
    /**
     * 为quartz的任务和触发器单独配置一个数据源  @QuartzDataSource注解用于声明quartz使用这个数据源
     */
    @Bean
    @QuartzDataSource
    public DataSource quartzDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123456");
        hikariDataSource.setJdbcUrl("jdbc:mysql://192.168.253.202:3306/db_quartz?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
        return hikariDataSource;
    }

    /**
     * 为quartz的数据源配置事务管理器  @QuartzTransactionManager注解用于声明quartz使用这个事务管理器
     * @param quartzDataSource
     */
    @Bean
    @QuartzTransactionManager
    public TransactionManager quartzTransactionManager(@Qualifier("quartzDataSource") DataSource quartzDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(quartzDataSource);
        return transactionManager;
    }
}
