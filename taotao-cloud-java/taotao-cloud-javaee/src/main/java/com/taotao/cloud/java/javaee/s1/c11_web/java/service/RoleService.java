package com.taotao.cloud.java.javaee.s1.c11_web.java.service;


import com.github.pagehelper.PageInfo;

import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Role;
import java.util.List;

public interface RoleService {
    PageInfo<Role> getRoleList(Role role, Integer page, Integer pageSize);

    List<Role> getRoleList(Role role);

    void addRoleMenu(Integer roleId, Integer[] menuIds);

    List<Integer> getRoleMenuIds(Integer roleId);

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRole(int[] ids);

    Role getRoleId(Integer id);
}
