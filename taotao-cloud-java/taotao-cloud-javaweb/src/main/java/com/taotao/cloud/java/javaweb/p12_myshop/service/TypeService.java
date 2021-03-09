package com.taotao.cloud.java.javaweb.p12_myshop.service;

import com.itqf.entity.Type;

import java.sql.SQLException;
import java.util.List;

public interface TypeService {

    List<Type> findAll() throws SQLException;

}
