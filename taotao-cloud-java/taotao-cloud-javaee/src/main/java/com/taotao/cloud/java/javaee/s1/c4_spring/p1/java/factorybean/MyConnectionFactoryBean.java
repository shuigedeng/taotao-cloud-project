package com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.factorybean;

import org.springframework.beans.factory.FactoryBean;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnectionFactoryBean implements FactoryBean<Connection> {
    @Override
    public Connection getObject() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis_shine", "root", "111111");
    }

    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
