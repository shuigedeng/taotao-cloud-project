package com.taotao.cloud.web.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

@WebListener("myListener")
public class MyListener implements ServletContextListener, HttpSessionListener,
	HttpSessionAttributeListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String servletContextName = sce.getServletContext().getServletContextName();
		String serverInfo = sce.getServletContext().getServerInfo();
		int majorVersion = sce.getServletContext().getMajorVersion();
		int minorVersion = sce.getServletContext().getMinorVersion();
		String contextPath = sce.getServletContext().getContextPath();

		//LogUtils.info("ServletContextName {} 容器启动 服务信息 {} {}-{}-{}", servletContextName,
		//	serverInfo, majorVersion, minorVersion, contextPath);
	}
}
