package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.test;

import com.github.pagehelper.PageInfo;
import com.qf.dao.UserDAO;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.impl.UserServiceImpl;
import com.qf.util.MyBatisUtil;
import org.junit.Test;

import java.sql.*;
import java.util.List;

public class MyBatisTest {

    @Test
    public void test1(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        List<User> users = mapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test2(){
        UserServiceImpl userService = new UserServiceImpl();
        Page page = new Page();
        page.setPageNum(1);
        page.setPageSize(4);
        PageInfo<User> pageInfo = userService.queryUsers(page);
        System.out.println(pageInfo);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis_shine?useUnicode=true&characterEncoding=utf-8","root","111111");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user order by id ?");
        preparedStatement.setString(1,"desc");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            System.out.println(resultSet.getInt("id"));
        }
    }
}
