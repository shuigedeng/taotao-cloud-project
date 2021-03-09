package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.AdminUser;
import com.qianfeng.openapi.web.master.pojo.Role;
import com.qianfeng.openapi.web.master.bean.TableData;
import com.qianfeng.openapi.web.master.service.AdminUserService;
import com.qianfeng.openapi.web.master.service.RoleService;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/sys/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RoleService roleService;

    @RequestMapping( "/table")
    public TableData table(AdminUser adminUser, Integer page, Integer limit) {
        PageInfo<AdminUser> pageInfo = adminUserService.getUserList(adminUser, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 授权时的角色树
     */
    @RequestMapping( "/role_tree")
    public List<Role> roleList() {
        return roleService.getRoleList(null);
    }

    /**
     * 获取用户已有的角色，回填角色树中的复选框
     * @param userId
     */
    @RequestMapping( "/user_role")
    public List<Integer> userRole(Integer userId) {
        return adminUserService.getUserRoleIds(userId);
    }

    @RequestMapping( "/assign_role")
    public AjaxMessage assignRole(Integer userId, Integer[] roleIds) {
        try {
            adminUserService.addUserRole(userId, roleIds);
            return new AjaxMessage(true, "分配成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "分配失败");
    }

    @RequestMapping( "/add")
    public AjaxMessage add(AdminUser adminUser) {
        try {
            adminUserService.addUser(adminUser);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "添加失败");
    }

    @RequestMapping( "/update")
    public AjaxMessage update(AdminUser adminUser) {
        try {
            adminUserService.updateUser(adminUser);
            return new AjaxMessage(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "修改失败");
    }

    @RequestMapping( "/info")
    public AdminUser info(Integer id) {
        return adminUserService.getUserById(id);
    }

    @RequestMapping( "/del")
    public AjaxMessage delete(int[] ids) {
        try {
            adminUserService.deleteUser(ids);
            return new AjaxMessage(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "删除失败");
    }
}
