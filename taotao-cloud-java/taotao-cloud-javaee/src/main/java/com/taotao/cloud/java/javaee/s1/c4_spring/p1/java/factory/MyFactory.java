package com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// 工厂
// 1.加载配置文件
// 2.生产配置中记录的对应对象
public class MyFactory {

    private Properties properties = new Properties();

    public MyFactory(){}
    public MyFactory(String config) throws IOException {
        InputStream resourceAsStream = MyFactory.class.getResourceAsStream(config);
        // properties 读取配置文件
        properties.load(resourceAsStream);
    }

    public Object getBean(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //1. 通过name，获取对应类路径
        String classPath = properties.getProperty(name);
        // 2. 反射 构建对象
        Class claz = Class.forName(classPath);
        return claz.newInstance();
    }
}
