package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.service;

import com.qf.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    @Override
    public List<User> queryUsers() {
//        System.out.println("事务控制");
//        System.out.println("日志打印");
        System.out.println("queryUser");
        return new ArrayList<>();
    }

    @Override
    public Integer updateUser(User user) {
//        System.out.println("事务控制");
//        System.out.println("日志打印");
        System.out.println("update User");
        /*if(1==1) {
            throw new NullPointerException("test 空指针");
        }*/
        return 1;
    }

    @Override
    public Integer saveUser(User user) {
//        System.out.println("事务控制");
//        System.out.println("日志打印");
        System.out.println("Save User");
        return 1;
    }

    @Override
    public Integer deleteUser(Integer id) {
//        System.out.println("事务控制");
//        System.out.println("日志打印");
        System.out.println("delete User");
        return 1;
    }
}
