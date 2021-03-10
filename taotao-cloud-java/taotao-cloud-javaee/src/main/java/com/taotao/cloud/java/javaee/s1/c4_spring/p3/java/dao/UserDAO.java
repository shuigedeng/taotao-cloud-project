package com.taotao.cloud.java.javaee.s1.c4_spring.p3.java.dao;


import com.taotao.cloud.java.javaee.s1.c4_spring.p3.java.entity.User;
import java.util.List;

public interface UserDAO {
    public List<User> queryUsers();
    public Integer insertUser(User user);
    public Integer updateUser(User user);
    public Integer deleteUser(Integer id);
}
