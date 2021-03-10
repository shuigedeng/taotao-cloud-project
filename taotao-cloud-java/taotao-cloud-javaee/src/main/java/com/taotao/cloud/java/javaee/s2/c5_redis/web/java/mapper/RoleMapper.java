package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper;

import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> getRoleList(Role role);

    void addRoleMenu(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    void deleteRoleMenuByRoleId(Integer roleId);

    void deleteRoleMenuByMenuId(Integer menuId);

    List<Integer> getRoleMenuIds(Integer roleId);

    void addRole(Role role);

    void updateRole(Role role);

    Role getRoleId(Integer id);
}
