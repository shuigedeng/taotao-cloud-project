package com.taotao.cloud.java.javaweb.p12_myshop.dao;

import com.itqf.entity.User;

import java.sql.SQLException;

/**
 * 负责用户模块数据库访问的接口
 */
public interface UserDao {

    /**
     * 根据用户名查询用户是否存在
     * @param username 查询的条件
     * @return 返回对应的用户数据
     */
    User  selectUserByUname(String username) throws SQLException;

    int   insertUser(User user) throws SQLException;

    User selectUserByCode(String code) throws SQLException;

    int updateStatusByUid(int uid) throws SQLException;

}
