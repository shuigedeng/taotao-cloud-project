package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.openapi.web.master.pojo.AdminUser;
import com.qianfeng.openapi.web.master.pojo.Menu;
import com.qianfeng.openapi.web.master.service.AdminUserService;
import com.qianfeng.openapi.web.master.service.MenuService;
import com.qianfeng.openapi.web.master.util.AdminConstants;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
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
    @Autowired
    private JedisPool pool;
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login.html";
    }
    // 登录
    @RequestMapping("/dologin")
    @ResponseBody
    public AjaxMessage login(String email, String password, HttpSession session,HttpServletResponse resp) throws JsonProcessingException {
        /*//1. 将用户的用户名和密码交给Service做认证
        AdminUser user = adminUserService.doLogin(email, password);
        if (user == null) {
            // 2.1 认证失败
            return new AjaxMessage(false);
        }
        // 2.2 认证成功
        session.setAttribute(AdminConstants.SESSION_USER, user);
        // 3. 查询当前用户的权限信息
        List<Menu> menuList = menuService.getUserMenuList(user.getId());
        session.setAttribute(AdminConstants.USER_MENU,menuList);*/

        //1. 调用service执行认证，返回Redis的key
        String key = adminUserService.doLoginByRedis(email,password);
        //2. 判断返回的key是否为null
        if(StringUtils.isEmpty(key)) {
            //2.1 如果为null -> 认证失败
            return new AjaxMessage(false);
        }

        //3. 查询用户的权限信息，并且放到Redis中
        menuService.setUserMenuList(key);

        //4. 将key作为Cookie写回浏览器端
        Cookie cookie = new Cookie(AdminConstants.USER_COOKIE_KEY,key);
        cookie.setPath("/");
        cookie.setMaxAge(9999999);
        resp.addCookie(cookie);
        //4. 返回认证成功信息
        return new AjaxMessage(true);
    }

    @RequestMapping("/auth_error")
    public String error() {
        return "error.html";
    }



    @RequestMapping("/side")
    @ResponseBody
    public AjaxMessage getMenuTree(@CookieValue(value = AdminConstants.USER_COOKIE_KEY,required = false)String key, HttpSession session, HttpServletResponse response) throws JsonProcessingException {

    //        AdminUser user = (AdminUser) session.getAttribute(AdminConstants.SESSION_USER);
        Jedis jedis = pool.getResource();
        String value = jedis.get(AdminConstants.SESSION_USER + key);
        ObjectMapper mapper = new ObjectMapper();
        AdminUser user = mapper.readValue(value, AdminUser.class);
        if (user == null) {
            try {
                jedis.close();
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new AjaxMessage(true, null, new ArrayList<>());
        }
        jedis.expire(AdminConstants.SESSION_USER + key,600);
        jedis.close();
        List<Menu> menus = menuService.getUserPermission(user.getId());
        return new AjaxMessage(true, null, menus);
    }
}
