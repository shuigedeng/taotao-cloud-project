package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.test;

import com.qf.dao.UserDAO;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestSSM {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Test
    public void test1(){
        List<User> users = userDAO.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test2(){
        Page page = new Page(1,3);
        userService.queryUsers(page);
        /*for (User user : users) {
            System.out.println(user);
        }*/
    }
}
