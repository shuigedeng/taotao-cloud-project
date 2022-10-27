package com.taotao.cloud.web.servlet.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 自定义Servlet
 */
//指定vlaue属性 和 urlPattern属性均可以
@WebServlet(urlPatterns = "/standardServlet", description = "自定义MyServlet")
public class StandardServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
//        super.doGet(req,resp);
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		Enumeration enu = req.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			System.out.println(paraName + ": " + req.getParameter(paraName));
		}
		//输出 Hello Servlet 到页面
		resp.getWriter().write("Hello Servlet");
		//重定向到 baidu
//        resp.sendRedirect("http://www.baidu.com");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		super.service(req, resp);
	}
}
