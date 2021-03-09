package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;

import com.qianfeng.openapi.web.master.pojo.Menu;
import com.qianfeng.openapi.web.master.bean.TableData;
import com.qianfeng.openapi.web.master.service.MenuService;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping( "/list")
    public TableData<Menu> list() {
        List<Menu> menus = menuService.getMenuList();
        return new TableData<>(menus.size(), menus);
    }

    /**
     * 菜单树，在添加和修改页面选择父菜单用
     *
     * @return
     */
    @RequestMapping( "/tree")
    public List<Menu> tree() {
        return menuService.getMenuTree();
    }

    @RequestMapping( "/delete")
    public AjaxMessage delete(Integer[] ids) {
        try {
            menuService.deleteMenus(ids);
            return new AjaxMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(true, "删除失败");
        }
    }

    @RequestMapping( "/info")
    public Menu goEdit(Integer id) {
        return menuService.getMenuById(id);
    }

    @RequestMapping( "/add")
    public AjaxMessage add(Menu menu) {
        try {
            menuService.addMenu(menu);
            return new AjaxMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(true, "添加失败");
        }
    }

    @RequestMapping( "/update")
    public AjaxMessage update(Menu menu) {
        try {
            menuService.updateMenu(menu);
            return new AjaxMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(true, "修改失败");
        }
    }
}
