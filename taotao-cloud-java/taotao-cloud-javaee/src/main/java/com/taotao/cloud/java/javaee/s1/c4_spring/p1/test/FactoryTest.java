package com.taotao.cloud.java.javaee.s1.c4_spring.p1.test;


import com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.dao.UserDAO;
import com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.factory.MyFactory;
import com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.service.UserService;
import java.io.IOException;

public class FactoryTest {

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        // 创建工厂对象
        MyFactory myFactory = new MyFactory("/bean.properties");
        // 从工厂中获取对象
        UserDAO userDAO = (UserDAO)myFactory.getBean("userDAO");
        UserService userService = (UserService)myFactory.getBean("userService");
        userDAO.deleteUser(1);
        userService.deleteUser(1);
    }
}
