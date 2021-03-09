package com.taotao.cloud.java.javaee.s1.c11_web.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.mapper.AdminUserMapper;
import com.qianfeng.openapi.web.master.pojo.AdminUser;
import com.qianfeng.openapi.web.master.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;


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
}
