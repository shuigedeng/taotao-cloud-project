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

package com.taotao.cloud.realtime.datalake.mall.utils;

import com.google.common.base.CaseFormat;
import com.taotao.cloud.realtime.mall.bean.TableProcess;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * Date: 2021/2/1
 * Desc: 从MySQL数据中查询数据的工具类
 * 完成ORM，对象关系映射
 * O：Object对象       Java中对象
 * R：Relation关系     关系型数据库
 * M:Mapping映射      将Java中的对象和关系型数据库的表中的记录建立起映射关系
 * 数据库                 Java
 * 表t_student           类Student
 * 字段id，name           属性id，name
 * 记录 100，zs           对象100，zs
 * ResultSet(一条条记录)             List(一个个Java对象)
 */
public class MySQLUtil {
    /**
     * @param sql               执行的查询语句
     * @param clz               返回的数据类型
     * @param underScoreToCamel 是否将下划线转换为驼峰命名法
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(String sql, Class<T> clz, boolean underScoreToCamel) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 创建连接
            conn =
                    DriverManager.getConnection(
                            "jdbc:mysql://hadoop202:3306/gmall0820_realtime?characterEncoding=utf-8&useSSL=false",
                            "root",
                            "123456");
            // 创建数据库操作对象
            ps = conn.prepareStatement(sql);
            // 执行SQL语句
            // 100      zs      20
            // 200		ls 		30
            rs = ps.executeQuery();
            // 处理结果集
            // 查询结果的元数据信息
            // id		student_name	age
            ResultSetMetaData metaData = rs.getMetaData();
            List<T> resultList = new ArrayList<T>();
            // 判断结果集中是否存在数据，如果有，那么进行一次循环
            while (rs.next()) {
                // 创建一个对象，用于封装查询出来一条结果集中的数据
                T obj = clz.newInstance();
                // 对查询的所有列进行遍历，获取每一列的名称
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    String propertyName = columnName;
                    if (underScoreToCamel) {
                        // 如果指定将下划线转换为驼峰命名法的值为 true，通过guava工具类，将表中的列转换为类属性的驼峰命名法的形式
                        propertyName =
                                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
                    }
                    // 调用apache的commons-bean中工具类，给obj属性赋值
                    BeanUtils.setProperty(obj, propertyName, rs.getObject(i));
                }
                // 将当前结果中的一行数据封装的obj对象放到list集合中
                resultList.add(obj);
            }

            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从MySQL查询数据失败");
        } finally {
            // 释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        List<TableProcess> list =
                queryList("select * from table_process", TableProcess.class, true);
        for (TableProcess tableProcess : list) {
            System.out.println(tableProcess);
        }
    }
}
