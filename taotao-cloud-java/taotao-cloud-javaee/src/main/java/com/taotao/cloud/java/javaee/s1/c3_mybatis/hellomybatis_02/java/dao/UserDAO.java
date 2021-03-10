package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.java.dao;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.java.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {

    List<User> queryUsers();
    /*User queryUserById(@Param("id") Integer id);
    User queryUserByUsername(@Param("username") String username);*/
    User queryUser(User user);
    List<User>  queryUser2(User user);
    Integer deleteUser(@Param("id") Integer id);
    Integer updateUser(User user);
    Integer insertUser(User user);

    Integer deleteManyUser(List<Integer> ids);

    Integer insertManyUser(List<User> users);
}
