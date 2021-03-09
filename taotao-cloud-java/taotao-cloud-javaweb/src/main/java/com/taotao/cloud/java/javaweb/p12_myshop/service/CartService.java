package com.taotao.cloud.java.javaweb.p12_myshop.service;

import com.itqf.entity.Cart;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface CartService {
    void createCart(int uid, String pid) throws SQLException, InvocationTargetException, IllegalAccessException;

    List<Cart> findAll(int uid) throws IllegalAccessException, SQLException, InvocationTargetException;

    void deleteCartByCid(String cid) throws SQLException;

    void updateCartByCid(String cid, String price, String cnum) throws SQLException;

    void clearCart(String uid) throws SQLException;
}
