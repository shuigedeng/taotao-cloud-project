package com.taotao.cloud.java.javaweb.p2_jdbc.dbcp.jdbc;


import com.taotao.cloud.java.javaweb.p2_jdbc.dbcp.utils.DBUtilDBCP;

import java.sql.Connection;
import java.sql.SQLException;

public class Demo1 {

    public static void main(String[] args) throws SQLException {
        Connection connection = DBUtilDBCP.getConnection();
        System.out.println(connection);
        DBUtilDBCP.close(null,null,connection);
    }
}
