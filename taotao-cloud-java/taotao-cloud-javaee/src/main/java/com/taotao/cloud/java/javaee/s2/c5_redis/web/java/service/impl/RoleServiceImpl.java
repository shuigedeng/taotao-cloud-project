package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper.AdminUserMapper;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper.RoleMapper;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo.Role;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public PageInfo<Role> getRoleList(Role role, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(roleMapper.getRoleList(role));
    }


    @Override
    public void addRoleMenu(Integer roleId, Integer[] menuIds) {
        //多对多的处理，先删中间表，再插入
        roleMapper.deleteRoleMenuByRoleId(roleId);
        for (Integer menuId : menuIds) {
            roleMapper.addRoleMenu(roleId, menuId);
        }
    }

    @Override
    public void addRole(Role role) {
        roleMapper.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }

    /**
     * 根据角色id获得对应的权限，用于授权页面，选中已有权限
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> getRoleMenuIds(Integer roleId) {
        return roleMapper.getRoleMenuIds(roleId);
    }

    @Override
    public List<Role> getRoleList(Role role) {
        return roleMapper.getRoleList(role);
    }

    @Override
    public void deleteRole(int[] ids) {
        Role role = new Role();
        //逻辑删除，把状态改成无效
        role.setStatus(0);
        for (int id : ids) {
            roleMapper.deleteRoleMenuByRoleId(id);
            adminUserMapper.deleteUserRole(id);
            role.setId(id);
            roleMapper.updateRole(role);
        }
    }

    @Override
    public Role getRoleId(Integer id) {
        return roleMapper.getRoleId(id);
    }
}
