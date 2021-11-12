package com.taotao.cloud.java.javaweb.p2_jdbc.druid.jdbc;


import com.taotao.cloud.java.javaweb.p2_jdbc.druid.utils.DBUtilDruid;

import java.sql.Connection;
import java.sql.SQLException;


public class Demo1 {

    public static void main(String[] args) throws SQLException {
        Connection connection = DBUtilDruid.getConnection();
        System.out.println(connection);
        DBUtilDruid.close(null,null,connection);
    }
}
