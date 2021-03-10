package com.taotao.cloud.java.javaee.s1.c10_ssm.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.mapper.UserMapper;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.pojo.User;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> getUserList(int page, int limit) {
        //开启分页
        PageHelper.startPage(page,limit);
        List<User> userList = userMapper.getAllUsers();
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        return pageInfo;
    }

    @Override
    public void delteUserByIds(int[] ids) {
        userMapper.delteUserByIds(ids);
    }
}
