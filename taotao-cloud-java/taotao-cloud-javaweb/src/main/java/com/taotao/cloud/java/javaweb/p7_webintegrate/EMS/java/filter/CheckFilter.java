package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.filter;


import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.EmpManager;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "CheckFilter",value = "/manager/safe/*")
public class CheckFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession  session = request.getSession();
        EmpManager empManager = (EmpManager) session.getAttribute("empManager");
        if(empManager!=null){
            chain.doFilter(req, resp);
        }else{
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
