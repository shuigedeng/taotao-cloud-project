package com.taotao.cloud.java.javaee.s1.c11_web.java.filter;

import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.AdminUser;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Menu;
import com.taotao.cloud.java.javaee.s1.c11_web.java.util.AdminConstants;
import org.springframework.util.StringUtils;

import javax.servlet.*;
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
        AdminUser user = (AdminUser) session.getAttribute(AdminConstants.SESSION_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }
        for(String str:freeUrlList){
            if (servletPath.indexOf(str) != -1) {
                chain.doFilter(request, response);
                return;
            }
        }
        List<Menu> menus = (List<Menu>) session.getAttribute(AdminConstants.USER_MENU);
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
