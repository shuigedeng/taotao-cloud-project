package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.dao;


import com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.entity.User;
import java.util.List;

public interface UserDAO {
    List<User> queryUsers();
}
