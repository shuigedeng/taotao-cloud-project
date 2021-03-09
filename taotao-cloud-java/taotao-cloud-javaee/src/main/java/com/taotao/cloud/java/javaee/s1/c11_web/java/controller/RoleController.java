package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.Menu;
import com.qianfeng.openapi.web.master.pojo.Role;
import com.qianfeng.openapi.web.master.bean.TableData;
import com.qianfeng.openapi.web.master.service.MenuService;
import com.qianfeng.openapi.web.master.service.RoleService;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @RequestMapping( "/table")
    public TableData table(Role role, Integer page, Integer limit) {
        PageInfo<Role> pageInfo = roleService.getRoleList(role, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 授权页面的菜单树
     */
    @RequestMapping( "/menu_tree")
    public List<Menu> menuTree() {
        return menuService.getFullMenuTree();
    }

    /**
     * 授权
     *
     * @param roleId  角色id
     * @param menuIds 菜单
     */
    @RequestMapping( "/assign_menu")
    public AjaxMessage assign(Integer roleId, Integer[] menuIds) {
        try {
            roleService.addRoleMenu(roleId, menuIds);
            return new AjaxMessage(true, "分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, "分配失败");
        }
    }

    /**
     * 获取角色已有的菜单，用于回填选中页面上的菜单
     * @param roleId
     */
    @RequestMapping( "/role_menu")
    public List<Integer> roleMenu(Integer roleId) {
        return roleService.getRoleMenuIds(roleId);
    }

    /**
     * 打开修改页面
     * @param id
     */
    @RequestMapping( "/info")
    public Role info(Integer id) {
        return roleService.getRoleId(id);
    }

    @RequestMapping( "/update")
    public AjaxMessage edit(Role role) {
        try {
            roleService.updateRole(role);
            return new AjaxMessage(true, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, "编辑失败");
        }
    }

    @RequestMapping( "/add")
    public AjaxMessage add(Role role) {
        try {
            roleService.addRole(role);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, "添加失败");
        }
    }

    @RequestMapping( "/delete")
    public AjaxMessage delete(int[] ids) {
        try {
            roleService.deleteRole(ids);
            return new AjaxMessage(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, "删除失败");
        }
    }
}
