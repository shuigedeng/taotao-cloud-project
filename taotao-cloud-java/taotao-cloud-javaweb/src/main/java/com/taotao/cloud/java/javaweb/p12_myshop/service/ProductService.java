package com.taotao.cloud.java.javaweb.p12_myshop.service;

import com.itqf.entity.PageBean;
import com.itqf.entity.Product;

import java.sql.SQLException;

public interface ProductService {
    PageBean<Product> findPage(String tid, int page, int pageSize) throws SQLException;

    Product findProductByPid(String pid) throws SQLException;
}
