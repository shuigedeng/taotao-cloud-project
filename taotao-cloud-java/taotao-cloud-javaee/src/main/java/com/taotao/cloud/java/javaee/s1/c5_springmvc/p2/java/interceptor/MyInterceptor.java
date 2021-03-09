package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyInterceptor implements HandlerInterceptor {
    // 在handler之前执行
    // 再次定义 handler中冗余的功能
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("pre Handle");
        // 判断登录状态
        HttpSession session = request.getSession();
        if(session.getAttribute("state")!=null){
            return true;//放行，执行后续的handler
        }
        // 中断之前，响应请求
        response.sendRedirect("/login.jsp");
        return false;//中断请求，不再执行后续的handler
    }

    // 在handler之后执行，响应之前执行
    // 改动请求中数据
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("post Handle");
    }

    // 在视图渲染完毕后，
    // 资源回收
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("after Completion");
    }
}
