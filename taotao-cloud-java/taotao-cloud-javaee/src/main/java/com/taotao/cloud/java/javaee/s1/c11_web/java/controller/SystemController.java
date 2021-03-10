package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;

import com.taotao.cloud.java.javaee.s1.c11_web.java.bean.AjaxMessage;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.AdminUser;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Menu;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.AdminUserService;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.MenuService;
import com.taotao.cloud.java.javaee.s1.c11_web.java.util.AdminConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SystemController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private AdminUserService adminUserService;



    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login.html";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public AjaxMessage login(String email, String password, HttpSession session) {
        AdminUser user = adminUserService.doLogin(email, password);
        if (user == null) {
            return new AjaxMessage(false);
        }
        session.setAttribute(AdminConstants.SESSION_USER, user);
        List<Menu> menuList = menuService.getUserMenuList(user.getId());
        session.setAttribute(AdminConstants.USER_MENU,menuList);
        return new AjaxMessage(true);
    }

    @RequestMapping("/auth_error")
    public String error() {
        return "error.html";
    }

    @RequestMapping("/side")
    @ResponseBody
    public AjaxMessage getMenuTree(HttpSession session, HttpServletResponse response) {
        AdminUser user = (AdminUser) session.getAttribute(AdminConstants.SESSION_USER);
        if (user == null) {
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new AjaxMessage(true, null, new ArrayList<>());
        }
        List<Menu> menus = menuService.getUserPermission(user.getId());
        return new AjaxMessage(true, null, menus);
    }
}
