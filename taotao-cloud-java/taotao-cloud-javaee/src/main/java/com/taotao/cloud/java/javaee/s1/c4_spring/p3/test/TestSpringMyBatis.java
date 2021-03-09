package com.taotao.cloud.java.javaee.s1.c4_spring.p3.test;

import com.qf.dao.UserDAO;
import com.qf.entity.User;
import com.qf.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

// 测试启动，启动spring工厂，并且当前测试类也会被工厂生产
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestSpringMyBatis {

    @Autowired
    @Qualifier("userService2")
    private UserService userService;


    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Test
    public void testSpringJunit(){

//        List<User> users = userService.queryUsers();
//        for (User user : users) {
//            System.out.println(user);
//        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDAO mapper = sqlSession.getMapper(UserDAO.class);
        List<User> users = mapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }



    @Test
    public void test(){
       ApplicationContext context =  new ClassPathXmlApplicationContext("/applicationContext.xml");
       SqlSessionFactory factory = (SqlSessionFactory) context.getBean("sqlSessionFactory");
        SqlSession sqlSession = factory.openSession();
        UserDAO mapper = sqlSession.getMapper(UserDAO.class);
        List<User> users = mapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testDAO(){
        ApplicationContext context =  new ClassPathXmlApplicationContext("/applicationContext.xml");
        UserDAO userDAO = (UserDAO)context.getBean("userDAO");
        List<User> users = userDAO.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testService(){
        ApplicationContext context =  new ClassPathXmlApplicationContext("/applicationContext.xml");
        UserService userService = (UserService) context.getBean("userService");
        List<User> users = userService.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testTx() throws SQLException {
        ApplicationContext context =  new ClassPathXmlApplicationContext("/applicationContext.xml");
        UserService userService = (UserService) context.getBean("userService2");
//        userService.deleteUser(10012);
        User user = new User(10021, "张三3", "789", true, new Date());
//        userService.insertUser(user);
        userService.updateUser(user);
    }

    @Test
    public void testAOP() throws SQLException {
        ApplicationContext context =  new ClassPathXmlApplicationContext("/applicationContext.xml");
        UserService userService = (UserService) context.getBean("userService2");
        userService.queryUsers();
    }
}
