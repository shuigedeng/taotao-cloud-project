package com.taotao.cloud.web.servlet.listener;

import com.taotao.cloud.common.utils.log.LogUtils;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

/**
 * 标准servlet上下文侦听器
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-27 10:16:08
 */
@WebListener("standardServletContextListener")
public class StandardServletContextListener implements ServletContextListener, HttpSessionListener,
	HttpSessionAttributeListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String servletContextName = sce.getServletContext().getServletContextName();
		String serverInfo = sce.getServletContext().getServerInfo();
		int majorVersion = sce.getServletContext().getMajorVersion();
		int minorVersion = sce.getServletContext().getMinorVersion();
		String contextPath = sce.getServletContext().getContextPath();

		LogUtils.info("ServletContextName {} 容器启动 服务信息 {} {}-{}-{}", servletContextName,
			serverInfo, majorVersion, minorVersion, contextPath);
	}
}
