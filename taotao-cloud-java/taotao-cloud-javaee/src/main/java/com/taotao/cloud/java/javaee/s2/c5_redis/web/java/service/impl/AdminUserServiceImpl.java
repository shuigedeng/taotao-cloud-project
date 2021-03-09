package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.mapper.AdminUserMapper;
import com.qianfeng.openapi.web.master.pojo.AdminUser;
import com.qianfeng.openapi.web.master.service.AdminUserService;
import com.qianfeng.openapi.web.master.util.AdminConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private JedisPool pool;

    @Override
    public PageInfo<AdminUser> getUserList(AdminUser user, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(adminUserMapper.getUserList(user));
    }

    @Override
    public void addUserRole(Integer userId, Integer[] roleIds) {
        adminUserMapper.deleteUserRole(userId);
        for (Integer roleId : roleIds) {
            adminUserMapper.addUserRole(roleId, userId);
        }
    }

    @Override
    public List<Integer> getUserRoleIds(Integer userId) {
        return adminUserMapper.getUserRoleIds(userId);
    }

    @Override
    public void addUser(AdminUser user) {
        adminUserMapper.addUser(user);
    }

    @Override
    public void updateUser(AdminUser user) {
        adminUserMapper.updateUser(user);
    }

    @Override
    public AdminUser getUserById(Integer id) {
        return adminUserMapper.getUserById(id);
    }

    @Override
    public void deleteUser(int[] ids) {
        for (int id : ids) {
            AdminUser user = adminUserMapper.getUserById(id);
            if (user != null) {
                user.setStatus(0);
                adminUserMapper.updateUser(user);
            }
        }
    }

    @Override
    public AdminUser doLogin(String email, String password) {
        AdminUser user = adminUserMapper.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    @Override
    public String doLoginByRedis(String email, String password) throws JsonProcessingException {
        //1. 判断用户名和密码是否正确
        AdminUser user = adminUserMapper.getUserByEmail(email);
        //2. 如果user==null ， 登录失败，返回null
        if(user == null){
            return null;
        }
        //3. user ！= null 登录成功，声明一个UUID，作为存储到Redis中的key，返回key到Controller
        //3.1 声明key和value
        String key = UUID.randomUUID().toString();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(user);
        //3.2 将key和value存储到Redis中
        Jedis jedis = pool.getResource();
        jedis.setex(AdminConstants.SESSION_USER + key,600,value);
        jedis.close();
        //4. 返回结果
        return key;
    }
}
