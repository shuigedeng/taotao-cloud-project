package com.taotao.cloud.java.javaweb.p12_myshop.service.impl;

import com.itqf.dao.TypeDao;
import com.itqf.dao.impl.TypeDaoImpl;
import com.itqf.entity.Type;
import com.itqf.service.TypeService;

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
