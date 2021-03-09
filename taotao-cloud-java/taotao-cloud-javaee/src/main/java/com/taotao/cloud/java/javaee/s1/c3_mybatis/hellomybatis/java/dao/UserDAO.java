package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.dao;

import com.qf.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDAO {

    User queryUserById(Integer id);
    User queryUserByIdAndUsername(Integer id,String username);
    User queryUserByIdAndPassword(@Param("id") Integer id, @Param("password") String password);
    User queryUserByIdAndPassword2(Map map);
    User queryUserByIdAndPassword3(User user);
    List<User> queryUserByUsername(@Param("username") String username);

    Integer deleteUser(@Param("id") Integer id);

    Integer updateUser(User user);

    Integer insertUser(User user);

}
