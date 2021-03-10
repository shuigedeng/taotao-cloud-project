package com.taotao.cloud.java.javaee.s1.c11_web.java.mapper;

import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUserMapper {
    List<AdminUser> getUserList(AdminUser user);

    AdminUser getUserByEmail(String email);

    void addUserRole(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

    void deleteUserRole(Integer roleId);

    List<Integer> getUserRoleIds(Integer userId);

    List<String> getUserPerms(Integer userId);

    void addUser(AdminUser user);

    void updateUser(AdminUser user);

    AdminUser getUserById(Integer id);
}
