package com.taotao.cloud.java.javaweb.p2_jdbc.c3p0.jdbc;

import com.taotao.cloud.java.javaweb.p2_jdbc.c3p0.utils.DBUtilC3p0;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 测试工具类
 */
public class Demo1 {
    public static void main(String[] args) throws SQLException {
        Connection connection = DBUtilC3p0.getConnection();
        System.out.println(connection);
        DBUtilC3p0.close(null,null,connection);
    }
}
