package com.taotao.cloud.sys.biz.tools.database.service;


import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ExMysqlDataSource extends MysqlDataSource {
    @Override
    public Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("password",password);
        properties.setProperty("user",user);
        final Properties mysqlCommonProperties = mysqlCommonProperties();
        properties.putAll(mysqlCommonProperties);
        Connection connection = super.getConnection(properties);
        return connection;
    }

    public static Properties mysqlCommonProperties(){
        Properties properties = new Properties();
        properties.setProperty("remarks", "true");
        properties.setProperty("useInformationSchema", "true");
        properties.setProperty("Unicode","true");
        properties.setProperty("characterEncoding","UTF-8");
        properties.setProperty("serverTimezone","Asia/Shanghai");
        properties.setProperty("useAffectedRows","true");
        properties.setProperty("useSSL","false");
        properties.setProperty("allowPublicKeyRetrieval","true");
        return properties;
    }

    @Override
    public String getURL() {
        final String url = super.getURL();
        final Properties properties = mysqlCommonProperties();
        StringBuffer buffer = new StringBuffer();
        final Iterator<String> iterator = properties.stringPropertyNames().iterator();
        int i = 0;
        while (iterator.hasNext()){
            final String key = iterator.next();
            final String value = properties.getProperty(key);
            if (i == 0){
                buffer.append("?").append(key).append("=").append(value);
            }else{
                buffer.append("&").append(key).append("=").append(value);
            }
            i ++;
        }
        return url +  buffer.toString();
    }
}
