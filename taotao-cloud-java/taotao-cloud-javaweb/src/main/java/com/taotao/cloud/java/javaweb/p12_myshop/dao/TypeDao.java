package com.taotao.cloud.java.javaweb.p12_myshop.dao;


import com.taotao.cloud.java.javaweb.p12_myshop.entity.Type;
import java.sql.SQLException;
import java.util.List;

public interface TypeDao {

    List<Type> selectAll() throws SQLException;

}
