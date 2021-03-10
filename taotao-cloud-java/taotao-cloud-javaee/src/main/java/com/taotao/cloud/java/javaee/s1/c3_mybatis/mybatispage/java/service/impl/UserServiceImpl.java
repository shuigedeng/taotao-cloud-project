package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.dao.UserDAO;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.entity.Page;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.entity.User;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.service.UserService;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.util.MyBatisUtil;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public PageInfo<User> queryUsers(Page page) {
        UserDAO mapper = MyBatisUtil.getMapper(UserDAO.class);
        // 分页设置
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<User> users = mapper.queryUsers();
        // 封装PageInfo
        return new PageInfo<User>(users);
    }
}
