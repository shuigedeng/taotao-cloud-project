package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.AdminUser;

import java.util.List;

public interface AdminUserService {

    PageInfo<AdminUser> getUserList(AdminUser user, Integer page, Integer pageSize);

    void addUserRole(Integer userId, Integer[] roleIds);

    List<Integer> getUserRoleIds(Integer userId);

    void addUser(AdminUser user);

    void updateUser(AdminUser user);

    AdminUser getUserById(Integer id);

    void deleteUser(int[] ids);

    AdminUser doLogin(String email, String password);

    /**
     * 执行登录，并将用户信息存储在Service
     * @param email
     * @param password
     * @return
     */
    String doLoginByRedis(String email, String password) throws JsonProcessingException;
}
