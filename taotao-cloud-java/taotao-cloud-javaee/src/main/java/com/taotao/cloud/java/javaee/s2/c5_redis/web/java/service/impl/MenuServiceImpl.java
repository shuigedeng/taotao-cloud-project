package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.openapi.web.master.mapper.MenuMapper;
import com.qianfeng.openapi.web.master.mapper.RoleMapper;
import com.qianfeng.openapi.web.master.pojo.AdminUser;
import com.qianfeng.openapi.web.master.pojo.Menu;
import com.qianfeng.openapi.web.master.service.MenuService;
import com.qianfeng.openapi.web.master.util.AdminConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private JedisPool pool;

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menus = menuMapper.getAllMenu();
        return makeMenuTree(menus);
    }

    /**
     * 封装不带按钮的菜单树，左边导航和选择父菜单时使用
     */
    private List<Menu> makeMenuTree(List<Menu> menus) {
        List<Menu> firstMenu = new ArrayList<>();
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
            if (menu.getParentId() == null) {//一级菜单
                firstMenu.add(menu);
            }
        }
        for (Menu menu : menus) {
            if (menu.getType() != AdminConstants.MENU_TYPE_BUTTON) {//不是按钮
                if (menu.getParentId() != null && menuMap.containsKey(menu.getParentId())) {
                    menuMap.get(menu.getParentId()).getChildren().add(menu);
                }
            }
        }
        return firstMenu;
    }

    /**
     * 所有菜单组成的菜单树，管理菜单列表页使用
     * @return
     */
    @Override
    public List<Menu> getFullMenuTree() {
        List<Menu> menus = menuMapper.getAllMenu();
        List<Menu> firstMenu = new ArrayList<>();
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
            if (menu.getParentId() == null) {//一级菜单
                firstMenu.add(menu);
            }
        }
        for (Menu menu : menus) {
            if (menu.getParentId() != null && menuMap.containsKey(menu.getParentId())) {
                menuMap.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        return firstMenu;
    }

    @Override
    public void deleteMenus(Integer[] ids) {
        for (Integer id : ids) {
            menuMapper.updateParentId(id);//将子菜单变成一级菜单
            roleMapper.deleteRoleMenuByMenuId(id);//删除角色菜单
            menuMapper.deleteMenu(id);//删除菜单

        }
    }

    @Override
    public void addMenu(Menu menu) {
        menuMapper.addMenu(menu);
    }

    @Override
    public Menu getMenuById(Integer id) {
        return menuMapper.getMenuById(id);
    }


    @Override
    public void updateMenu(Menu menu) {
        menuMapper.updateMenu(menu);
    }

    @Override
    public List<Menu> getUserPermission(Integer userId) {
        List<Menu> menus = menuMapper.getUserMenu(userId);
        return  makeMenuTree(menus);
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menus = menuMapper.getAllMenu();
        List<Menu> firstMenu = new ArrayList<>();
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
            if (menu.getParentId() == null) {//一级菜单
                firstMenu.add(menu);
            }
        }
        for (Menu menu : menus) {
            if (menu.getParentId() != null && menuMap.containsKey(menu.getParentId())) {
                menuMap.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        List<Menu> sortMenu = new ArrayList<>();
        makeMenuList(firstMenu, sortMenu);
        return sortMenu;
    }

    private void makeMenuList(List<Menu> menus, List<Menu> target) {
        for (Menu menu : menus) {
            target.add(menu);
            if (menu.getChildren().size() > 0) {
                makeMenuList(menu.getChildren(), target);
            }
        }
    }

    @Override
    public List<Menu> getUserMenuList(Integer userId) {
        return menuMapper.getUserMenu(userId);
    }

    @Override
    public void setUserMenuList(String key) throws JsonProcessingException {
        //1. 获取用户的id
        Jedis jedis = pool.getResource();
        String value = jedis.get(AdminConstants.SESSION_USER + key);
        ObjectMapper mapper = new ObjectMapper();
        AdminUser adminUser = mapper.readValue(value, AdminUser.class);

        //2. 数据库查询用户权限信息
        List<Menu> menuList = menuMapper.getUserMenu(adminUser.getId());

        //3. 存储到Redis
        String menuKey = AdminConstants.USER_MENU + key;
        String menuValue = mapper.writeValueAsString(menuList);
        jedis.setex(menuKey,600,menuValue);
        jedis.close();
    }

}
