package com.taotao.cloud.java.javaee.s1.c10_ssm.java.mapper;


import com.taotao.cloud.java.javaee.s1.c10_ssm.java.pojo.User;
import java.util.List;

public interface UserMapper {
    List<User> getAllUsers();

    void delteUserByIds(int[] ids);
}
