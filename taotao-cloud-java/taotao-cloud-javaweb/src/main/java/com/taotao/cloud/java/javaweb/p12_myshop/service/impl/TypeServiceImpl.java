package com.taotao.cloud.java.javaweb.p12_myshop.service.impl;


import com.taotao.cloud.java.javaweb.p12_myshop.dao.TypeDao;
import com.taotao.cloud.java.javaweb.p12_myshop.dao.impl.TypeDaoImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.Type;
import com.taotao.cloud.java.javaweb.p12_myshop.service.TypeService;
import java.sql.SQLException;
import java.util.List;

public class TypeServiceImpl  implements TypeService {

    @Override
    public List<Type> findAll() throws SQLException {
        TypeDao typeDao = new TypeDaoImpl();

        List<Type> types = typeDao.selectAll();

        return types;
    }
}
