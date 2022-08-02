package com.taotao.cloud.sys.biz.modules.database.service.connect;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.sys.biz.modules.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.modules.database.service.connect.dtos.DatabaseConnect;
import com.taotao.cloud.sys.biz.modules.database.service.meta.dtos.Namespace;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.ConnectionPoolDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnDatasourceAdapter {
    @Autowired
    private ConnectService connectService;

    /**
     * 连接数据源存储(默认命名空间) connName => DruidDataSource
     */
    private Map<String, DruidDataSource> connDefaultDataSourceMap = new ConcurrentHashMap<>();

    /**
     * 连接数据源存储(指定名称空间) connName => Namespace => DruidDataSource
     */
    private Map<String,Map<Namespace,DruidDataSource>> namespaceDatasourceMap = new ConcurrentHashMap<>();

    /**
     * 获取数据源
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public DruidDataSource poolDataSource(String connName) throws IOException, SQLException {
        DruidDataSource dataSource = connDefaultDataSourceMap.get(connName);
        if (dataSource != null){
            return dataSource;
        }
        dataSource = createDataSource(connName);
        connDefaultDataSourceMap.put(connName,dataSource);
        return dataSource;
    }

    /**
     * 带默认 catlog 的连接数据源
     * @param connName
     * @param namespace
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public DruidDataSource poolDataSource(String connName, Namespace namespace) throws IOException, SQLException{
//        final DruidDataSource druidDataSource = poolDataSource(connName);
//        druidDataSource.setDefaultCatalog(namespace.getCatalog());
//        return druidDataSource;

        final Map<Namespace, DruidDataSource> namespaceDruidDataSourceMap = namespaceDatasourceMap.computeIfAbsent(connName, k -> new ConcurrentHashMap<>());
        if (namespaceDruidDataSourceMap.containsKey(namespace)){
            return namespaceDruidDataSourceMap.get(namespace);
        }

        // 复制数据源
        final DruidDataSource druidDataSource = poolDataSource(connName);
        final DruidDataSource targetDataSource = new DruidDataSource();
        BeanUtils.copyProperties(druidDataSource,targetDataSource);
        targetDataSource.setDefaultCatalog(namespace.getCatalog());

        namespaceDruidDataSourceMap.put(namespace,targetDataSource);
        return targetDataSource;
    }

    /**
     * 获取一个连接
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public Connection connection(String connName) throws IOException, SQLException {
        final ConnectionPoolDataSource connectionPoolDataSource = poolDataSource(connName);
        return connectionPoolDataSource.getPooledConnection().getConnection();
    }

    /**
     * 获取数据库类型
     * @param connName
     * @return
     * @throws IOException
     */
    public String dbType(String connName) throws IOException {
        final DatabaseConnect databaseConnect = databaseConnect(connName);
        final String dbType = JdbcUtils.getDbType(databaseConnect.getUrl(), null);
        return dbType;
    }

    /**
     * 创建一个数据源
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private DruidDataSource createDataSource(String connName) throws IOException, SQLException {
        final DatabaseConnect databaseConnect = databaseConnect(connName);
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(databaseConnect.getUsername());
        druidDataSource.setPassword(databaseConnect.getPassword());
        druidDataSource.setUrl(databaseConnect.getUrl());
        if (StringUtils.isNotBlank(databaseConnect.getDriverClassName())){
            druidDataSource.setDriverClassName(databaseConnect.getDriverClassName());
        }

        final String url = databaseConnect.getUrl();
        // 连接属性(默认值)
        Properties properties = connectProperties(url);
        // 覆盖连接属性
        properties.putAll(databaseConnect.getProperties());
        druidDataSource.setConnectProperties(properties);
        // 连接失败时, 只尝试 5 次
        druidDataSource.setConnectionErrorRetryAttempts(5);
        druidDataSource.setBreakAfterAcquireFailure(true);
        return druidDataSource;
    }

    /**
     * 获取连接附加属性
     * @param url
     * @return
     */
    public static Properties connectProperties(String url) {
        Properties properties = new Properties();

        final String dbType = JdbcUtils.getDbType(url, null);
        if (JdbcConstants.MYSQL.equals(dbType)){
            properties.setProperty("serverTimezone","GMT+8");
            properties.setProperty("useSSL","false");
            properties.setProperty("useUnicode","true");
            properties.setProperty("characterEncoding","UTF-8");
            properties.setProperty("allowPublicKeyRetrieval","true");
        }
        return properties;
    }

    public static String keyValueProperties(String url){
        final Properties properties = connectProperties(url);
        final Set<String> keys = properties.stringPropertyNames();
        final Iterator<String> iterator = keys.iterator();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while (iterator.hasNext()){
                final String key = iterator.next();
                final String value = properties.getProperty(key);
                stringBuffer.append("&").append(key).append("=").append(URLEncoder.encode(value,StandardCharsets.UTF_8.toString()));
            }
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        if (keys.size() > 0){
            return stringBuffer.substring(1);
        }
        return null;
    }

    /**
     * 获取数据库连接信息
     * @param connName
     * @return
     * @throws IOException
     */
    public DatabaseConnect databaseConnect(String connName) throws IOException {
        final String databaseNew = connectService.loadContent("database", connName);
        final DatabaseConnect databaseConnect = JSONObject.parseObject(databaseNew, DatabaseConnect.class);
        return databaseConnect;
    }

}
