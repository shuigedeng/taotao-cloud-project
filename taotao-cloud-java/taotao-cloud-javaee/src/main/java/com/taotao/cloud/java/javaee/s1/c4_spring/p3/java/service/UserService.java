package com.taotao.cloud.java.javaee.s1.c4_spring.p3.java.service;

import com.qf.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public List<User> queryUsers();
    public Integer insertUser(User user);
    public Integer updateUser(User user);
    public Integer deleteUser(Integer id) throws SQLException;
}
