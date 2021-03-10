package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo.Menu;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.util.AdminConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 登陆验证
 */
//@WebFilter("/*")
public class SecurityFilter implements Filter {
    private static String redirectUrl = "/login.html";

    private static final String[] freeUrlList = {"/index.html","/side"};

    private JedisPool pool;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        pool = (JedisPool) context.getBean("jedisPool");
    }

    private static final String[] ignoreUrlList = {"/login.html", "/dologin", ".js", ".css", ".ico", ".jpg", ".png"};

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String servletPath = request.getServletPath();

        if (null != request.getPathInfo()) {
            servletPath += request.getPathInfo();
        }

        for (String str : ignoreUrlList) {
            if (servletPath.indexOf(str) != -1) {
                chain.doFilter(request, response);
                return;
            }
        }

        String key = null;
        // 获取Cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(AdminConstants.USER_COOKIE_KEY)){
                    key = cookie.getValue();
                }
            }
        }
        // 没有认证过，cookie中没有获取到指定的信息
        if(key == null){
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }

        // 从Session域中获取用户认证信息
//        AdminUser user = (AdminUser) session.getAttribute(AdminConstants.SESSION_USER);
        // 从Redis中获取用户的信息
        Jedis jedis = pool.getResource();
        String value = jedis.get(AdminConstants.SESSION_USER + key);
        if (value == null) {
            jedis.close();
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }


        for(String str:freeUrlList){
            if (servletPath.indexOf(str) != -1) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 从Session中获取了用户的权限信息
//        List<Menu> menus = (List<Menu>) session.getAttribute(AdminConstants.USER_MENU);
        String menuValue = jedis.get(AdminConstants.USER_MENU + key);
        jedis.close();
        ObjectMapper mapper = new ObjectMapper();
        List<Menu> menus = mapper.readValue(menuValue, new TypeReference<List<Menu>>() {});
        Boolean hasPermission = false;
        for (Menu menu : menus) {
            if (!StringUtils.isEmpty(menu.getUrl()) && menu.getUrl().contains(servletPath)) {
                hasPermission = true;
                break;
            }
        }
        if (!hasPermission) {
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }
        // 更新Redis
        chain.doFilter(request, response);

    }


}
