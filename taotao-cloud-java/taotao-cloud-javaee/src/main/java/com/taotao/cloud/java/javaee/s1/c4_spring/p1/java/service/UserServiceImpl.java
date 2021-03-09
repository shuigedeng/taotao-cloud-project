package com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.service;

import com.qf.dao.MyUserDAO;
import com.qf.dao.UserDAO;
import com.qf.dao.UserDAOImpl;

// Servlet  Service
// 稳定 健壮
public class UserServiceImpl implements UserService{

    // 满足依赖关系  强耦合
//    private UserDAO userDAO = new MyUserDAO();
    private UserDAO userDAO2;
    @Override
    public void deleteUser(Integer id) {
        System.out.println("delete User in Service");
        userDAO2.deleteUser(id);
    }

    public UserDAO getUserDAO2() {
        return userDAO2;
    }

    public void setUserDAO2(UserDAO userDAO) {
        this.userDAO2 = userDAO;
    }
}
