package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.service;


import com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.entity.User;
import java.util.List;

public interface UserService {

    public List<User> queryUsers();
    public Integer updateUser(User user);
    public Integer saveUser(User user);
    public Integer deleteUser(Integer id);
}
