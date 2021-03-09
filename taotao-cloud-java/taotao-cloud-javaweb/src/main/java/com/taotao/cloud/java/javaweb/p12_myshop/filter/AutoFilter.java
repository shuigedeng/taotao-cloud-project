package com.taotao.cloud.java.javaweb.p12_myshop.filter;

import com.itqf.entity.User;
import com.itqf.service.UserService;
import com.itqf.service.impl.UserServiceImpl;
import com.itqf.utils.Base64Utils;
import com.itqf.utils.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebFilter("/login.jsp")  //默认情况下，过滤器只正常请求和重定向
public class AutoFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            //本地存储了cookie
            String content = null; //为了接存储账号密码的数据

            for (Cookie cookie : cookies) {
                //如果找到存储自动登录cookie的名字就读取存储的账号和密码
                if (cookie.getName().equals(Constants.AUTO_NAME)) {
                    content = cookie.getValue();
                }
            }

            if (content != null) {
                //读取到了存储用户和密码的cookie

                content = Base64Utils.decode(content); //转成识别的字符串

                String[] split = content.split(Constants.FLAG);

                String username = split[0];
                String password = split[1];

                UserService userService = new UserServiceImpl();

                try {
                    User user = userService.login(username, password);

                    if (user != null) {
                        //可以自动登录
                        HttpSession session = request.getSession();
                        session.setAttribute("loginUser",user);
                        HttpServletResponse response = (HttpServletResponse) servletResponse;
                        response.sendRedirect(request.getContextPath()+"/index.jsp");
                    }else{
                        filterChain.doFilter(servletRequest,servletResponse);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else{
                //没有读取
                filterChain.doFilter(servletRequest,servletResponse);
            }

        }else {
            //本地没有cookie放行即可
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
