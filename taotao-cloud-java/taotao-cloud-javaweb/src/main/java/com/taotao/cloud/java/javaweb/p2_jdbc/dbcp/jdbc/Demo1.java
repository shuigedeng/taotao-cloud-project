package com.taotao.cloud.java.javaweb.p2_jdbc.dbcp.jdbc;


import com.taotao.cloud.java.javaweb.p2_jdbc.dbcp.utils.DBUtilDBCP;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 测试dbcp工具类
 */
public class Demo1 {

    public static void main(String[] args) throws SQLException {
        Connection connection = DBUtilDBCP.getConnection();
        System.out.println(connection);
        DBUtilDBCP.close(null,null,connection);
    }
}
