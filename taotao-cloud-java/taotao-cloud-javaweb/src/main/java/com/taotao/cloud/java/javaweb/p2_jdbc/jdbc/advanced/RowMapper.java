package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.advanced;

import java.sql.ResultSet;

/**
 * 约束封装对象的ORM
 */
public interface RowMapper<T> {
    public T getRow(ResultSet resultSet);
}
