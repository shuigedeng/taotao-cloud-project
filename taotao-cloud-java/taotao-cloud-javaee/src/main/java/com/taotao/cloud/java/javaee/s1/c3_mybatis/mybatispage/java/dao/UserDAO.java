package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.dao;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {

    List<User> queryUsers();
}
