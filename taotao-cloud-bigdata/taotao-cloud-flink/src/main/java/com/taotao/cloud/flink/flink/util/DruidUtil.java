/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.flink.flink.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

public class DruidUtil {
    // 连接池对象
    private static DataSource dataSource = null;

    static {
        // 1.实例化配置对象
        Properties properties = new Properties();
        // 2.获取配置文件的输入流
        InputStream inputStream =
                DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties");
        // 3.加载
        try {
            properties.load(inputStream);
            DruidUtil.dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接池对象
     * @return
     */
    public static DataSource getDataSource() {
        return DruidUtil.dataSource;
    }

    /**
     * 获取连接对象
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * 关闭结果集
     * @param res
     */
    public static void closeRes(ResultSet res) {
        try {
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭预处理对象
     * @param stmt
     */
    public static void closeStmt(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接资源
     * @param connection
     */
    public static void closeCon(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
