package com.taotao.cloud.java.javaweb.p12_myshop.dao;


import com.taotao.cloud.java.javaweb.p12_myshop.entity.Cart;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface CartDao {
    Cart hasCart(int uid, String pid) throws SQLException, InvocationTargetException, IllegalAccessException;

    void updateCart(Cart cart) throws SQLException;

    void insertCart(Cart cart) throws SQLException;

    List<Cart> selectCartsByUid(int uid) throws InvocationTargetException, IllegalAccessException, SQLException;

    void deleteCartByCid(String cid) throws SQLException;

    void updateByCid(BigDecimal count, BigDecimal cnumbig, String cid) throws SQLException;

    void deleteCartByUid(String uid) throws SQLException;
}
