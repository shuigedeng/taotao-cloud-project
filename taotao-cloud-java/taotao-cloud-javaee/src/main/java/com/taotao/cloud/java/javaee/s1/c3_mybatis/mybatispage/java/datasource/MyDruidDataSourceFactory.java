package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

/**
 * 连接池 工厂
 */
public class MyDruidDataSourceFactory extends PooledDataSourceFactory {

    public MyDruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();//替换数据源
    }
}
