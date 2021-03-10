package com.taotao.cloud.java.javaee.s1.c11_web.java.service;


import com.github.pagehelper.PageInfo;

import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.AdminUser;
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
}
