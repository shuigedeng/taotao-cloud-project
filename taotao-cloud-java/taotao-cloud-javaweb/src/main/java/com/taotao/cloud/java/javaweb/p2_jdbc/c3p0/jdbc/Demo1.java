package com.taotao.cloud.java.javaweb.p2_jdbc.c3p0.jdbc;

import com.taotao.cloud.java.javaweb.p2_jdbc.c3p0.utils.DBUtilC3p0;

import java.sql.Connection;
import java.sql.SQLException;

public class Demo1 {
    public static void main(String[] args) throws SQLException {
        Connection connection = DBUtilC3p0.getConnection();
        System.out.println(connection);
        DBUtilC3p0.close(null,null,connection);
    }
}
