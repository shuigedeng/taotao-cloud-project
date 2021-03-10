package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.test;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.dao.UserDAO;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.entity.User;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.util.MyBatisUtil;
import org.junit.Test;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class MyBatisTest {
    @Test
    public void test1(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        /*List<User> users = mapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("============");
        User user = mapper.queryUserById(10011);
        System.out.println(user);*/
        //mapper.deleteUser(10011);
        User new_user = new User(null, "new_user", "1111", true, new Date());
        //mapper.insertUser(new_user);
        System.out.println("新用户id:"+new_user.getId());
        //mapper.updateUser(new User(10015,"张三2","11111",true,new Date()));
        MyBatisUtil.commit();
    }


    /**
     * 1.占位符：规避sql注入风险
     * 2.要和列相关位置才可以使用
     * 原则：填充数据，要和列相关
     * select * from t_user where id=?
     * insert into t_user values(?,?,?)
     * update t_user set username=?,password=?
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Test
    public void test2() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis_shine?useUnicode=true&characterEncoding=utf-8","root","111111");

        //要填充的数据
        String username = "shine_66' or '1'='1";
        String rule = "desc";
        String sql = "select * from t_user where username=?";
        String sql2 = "select * from t_user order by id ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql2);
        // 在占位符上 填充desc
        preparedStatement.setString(1,rule);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("username"));
        }
    }

    /**
     * 1. sql注入风险
     * 2. 随意拼接
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Test
    public void test3() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis_shine?useUnicode=true&characterEncoding=utf-8","root","111111");

        //要填充的数据
        String username = "shine_xxx' or '1'='1";
        String rule = "desc";
        //sq拼接填充数据  select * from t_user where username='shine_66'
        //sq拼接填充数据  select * from t_user where username='shine_xxx' or '1'='1'
        // 当拼接sql片段，有sql注入风险，外界参数改变原有sql的语义
        String sql = "select * from t_user where username='"+username+"'";
        String sql2 = "select * from t_user order by id "+rule;

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql2);
        while(resultSet.next()){
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("username"));

        }
    }

    @Test
    public void test4(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);

        User user1 = mapper.queryUserById(10012);
        System.out.println("=========");
        User user2 = mapper.queryUserById2(10012);

//        User user2 = new User();
//        user2.setId(10013);
//        user2.setUsername("李四");
//        List<User> users = mapper.queryUserByUsernameorId(user2);

    }

    @Test
    public void test5(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        List<User> shine_666 = mapper.queryUserByUsername("shine_xxxx' or '1'='1");
        for (User user : shine_666) {
            System.out.println(user);
        }
    }
    
    @Test
    public void test6(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        Integer sig=0; // 0 desc 1  asc
        if(sig==0){
            mapper.queryUsers("desc");
        }else {
            mapper.queryUsers("asc");
        }
    }
}
