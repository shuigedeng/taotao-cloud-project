package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.java.dao.UserDAO;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.java.entity.User;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis_02.java.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyBatisTest {
    UserDAO mapper;
    @Before
    public void init(){
         mapper = MyBatisUtil.getMapper(UserDAO.class);
    }

    @Test
    public void test1(){
        //UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        //User user = mapper.queryUserById(10006);
        List<User> users = mapper.queryUsers();
        //System.out.println(user);
        System.out.println("================");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void test2(){
        User user = new User();
        user.setId(10014);
        //user.setUsername("shine_0");
        User user1 = mapper.queryUser(user);
        System.out.println(user1);
    }

    @Test
    public void test3(){
        User user  = new User();
        //user.setUsername("shine_0");
        user.setGender(true);
        List<User> users = mapper.queryUser2(user);
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void test4(){
        User user = new User(10006,"shine_0002","9876543210",true,null);
        mapper.updateUser(user);
        MyBatisUtil.commit();
    }

    @Test
    public void test5(){
        List<Integer> ids = Arrays.asList(10006, 10007, 10008, 10009);
        mapper.deleteManyUser(ids);
        MyBatisUtil.commit();
    }

    @Test
    public void test6(){
        List<User> users = Arrays.asList(new User(null, "张三", "123", true, new Date()),
                new User(null, "李四", "456", false, new Date()));
        mapper.insertManyUser(users);
        MyBatisUtil.commit();
    }

    @Test
    public void test7(){
        SqlSession sqlSession = MyBatisUtil.openSession();
        UserDAO mapper = sqlSession.getMapper(UserDAO.class);
        List<User> users = mapper.queryUsers();
        System.out.println("===========");
        List<User> users1 = mapper.queryUsers();

        System.out.println("-------------------------------");
        SqlSession session = MyBatisUtil.getSession();
        UserDAO mapper1 = session.getMapper(UserDAO.class);
        mapper1.queryUsers();
    }

    @Test
    public void test8(){
        // 通过相同的SqlSessionFactory获取多个SqlSession
        SqlSession session1 = MyBatisUtil.getSession();
        SqlSession session2 = MyBatisUtil.getSession();
        SqlSession session3 = MyBatisUtil.getSession();
        UserDAO mapper1 = session1.getMapper(UserDAO.class);
        UserDAO mapper2 = session2.getMapper(UserDAO.class);
        UserDAO mapper3 = session3.getMapper(UserDAO.class);
        mapper1.queryUsers();
        session1.close(); //二级缓存生效

        //修改   ，修改相关的缓存，会被移除
        SqlSession session4 = MyBatisUtil.getSession();
        UserDAO mapper4 = session4.getMapper(UserDAO.class);
        mapper4.deleteUser(10010);
        session4.commit();
        session4.close();

        System.out.println("===================");
        mapper2.queryUsers();
        session2.close();
        System.out.println("===================");
        mapper3.queryUsers();
        session3.close();
    }
    @Test
    public void testPage(){
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        //在查询前，设置分页  查询第一页，每页2条数据
        // PageHelper 对其之后的第一个查询，进行分页功能追加
        PageHelper.startPage(2,2);
        List<User> users = mapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
        // 将查询结果 封装到 PageInfo对象中
        PageInfo<User> pageInfo = new PageInfo(users);
        System.out.println("================");
    }
}
