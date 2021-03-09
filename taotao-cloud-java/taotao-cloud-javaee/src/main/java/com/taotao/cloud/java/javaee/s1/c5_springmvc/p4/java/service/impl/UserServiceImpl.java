package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.dao.UserDAO;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // userServiceImpl
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<User> queryUsers(Page page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<User> users = userDAO.queryUsers();
        return new PageInfo<User>(users);
    }
}
