package com.taotao.cloud.java.javaee.s1.c11_web.java.service;



import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Menu;
import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Menu> getMenuTree();

    List<Menu> getMenuList();

    List<Menu> getFullMenuTree();

    void deleteMenus(Integer[] ids);

    void addMenu(Menu menu);

    Menu getMenuById(Integer id);

    void updateMenu(Menu menu);

    List<Menu> getUserPermission(Integer userId);

    List<Menu> getUserMenuList(Integer userId);

}
