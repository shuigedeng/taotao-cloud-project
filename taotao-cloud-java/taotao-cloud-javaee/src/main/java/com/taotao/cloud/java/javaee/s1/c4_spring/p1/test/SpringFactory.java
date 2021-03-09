package com.taotao.cloud.java.javaee.s1.c4_spring.p1.test;

import com.qf.entity.Student;
import com.qf.entity.User;
import com.qf.factorybean.MyConnectionFactoryBean;
import com.qf.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpringFactory {

    @Test
    public void testSpringFactory(){
        // 启动工厂
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        // 获取对象
       // UserDAO userDAO = (UserDAO)context.getBean("userDAO");

        //UserService userService = (UserService)context.getBean("userService");

        //userDAO.deleteUser(1);
        //userService.deleteUser(2);
        context.close();
    }


    @Test
    public void testSet(){
        // 启动工厂
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        User user = (User)context.getBean("user");
        System.out.println("=================");
    }


    @Test
    public void testCons(){
        // 启动工厂
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        Student student = (Student)context.getBean("student");
        System.out.println("=============");
    }

    @Test
    public void testSingleton(){
        // 启动工厂
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        User user = (User)context.getBean("user");
        User user2 = (User)context.getBean("user");
        User user3 = (User)context.getBean("user");
        System.out.println(user==user2);
        System.out.println(user==user3);
    }

    @Test
    public void testFactoryBean() throws SQLException {
        // 启动工厂
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        Connection conn = (Connection) context.getBean("conn");
        MyConnectionFactoryBean factoryBean = (MyConnectionFactoryBean) context.getBean("&conn");
        System.out.println(factoryBean);
        System.out.println(conn);
        PreparedStatement preparedStatement = conn.prepareStatement("select * from t_user");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        System.out.println(resultSet.getInt("id"));
    }

    @Test
    public void testLife(){
        // 启动工厂
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context2.xml");

        context.getBean("addr");
        context.getBean("addr");

        System.out.println("==============");
        // 关闭工厂
        context.close();
    }
}
