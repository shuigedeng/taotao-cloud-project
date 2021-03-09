package com.taotao.cloud.java.javaee.s1.c4_spring.p3.java.service;

import com.qf.dao.UserDAO;
import com.qf.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

//@Service  // <bean id="userServiceImpl" class="xx.x.x.UserServiceImpl">
@Service("userService2") //  <bean id="userService" class="xx.x.x.UserServiceImpl">
//@Scope("prototype") // 多例bean
@Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,timeout = -1,readOnly = false,rollbackFor = Exception.class)
public class UserServiceImpl implements UserService{

    //@Autowired // 类型 自动注入
//    @Resource(name="userDAO") // 名称 自动注入
    //@Resource
    // 基于类型 自动注入，并挑选beanid="userDAO"
    @Autowired
    @Qualifier("userDAO")
    private UserDAO userDAO;

    public UserDAO getUserDAO() {
        return userDAO;
    }
//    @Value("10")
//    private Integer id;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> queryUsers() {
        System.out.println("queryUser in service");
        //int a=10/0;
        return userDAO.queryUsers();
    }

    @Override
    public Integer insertUser(User user) {
        return userDAO.insertUser(user);
    }

    @Override
    public Integer updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public Integer deleteUser(Integer id){
        Integer integer = userDAO.deleteUser(id);
        //int a=10/0;
        if(1==1){
            try {
                throw new SQLException("test 事务");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("test 事务！！");
            }
        }
        return integer;
    }
}
