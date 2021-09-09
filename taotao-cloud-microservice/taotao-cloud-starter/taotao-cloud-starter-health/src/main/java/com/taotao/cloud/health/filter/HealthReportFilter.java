package com.taotao.cloud.health.filter;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.model.Report;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: chejiangyi
 * @version: 2019-07-25 21:51
 **/
public class HealthReportFilter implements Filter {

	private HealthCheckProvider healthProvider;

	public HealthReportFilter(HealthCheckProvider healthProvider) {
		this.healthProvider = healthProvider;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String conetextPath = org.springframework.util.StringUtils.trimTrailingCharacter(
			request.getContextPath(), '/');
		String uri = request.getRequestURI();

		if (uri.startsWith(conetextPath + "/taotao/cloud/health/report")) {
			try {
				String html;

				if (Objects.nonNull(healthProvider)) {
					Report report;
					boolean isAnalyse = !"false".equalsIgnoreCase(
						request.getParameter("isAnalyse"));

					report = healthProvider.getReport(isAnalyse);
					if (request.getContentType() != null && request.getContentType()
						.contains("json")) {
						response.setHeader("Content-type", "application/json;charset=UTF-8");
						html = report.toJson();
					} else {
						response.setHeader("Content-type", "text/html;charset=UTF-8");
						html = report.toHtml().replace("\r\n", "<br/>").replace("\n", "<br/>")
							.replace("/n", "\n").replace("/r", "\r");
						if (PropertyUtil.getPropertyCache("taotao.cloud.health.dump.enabled",
							false)) {
							html = "dump信息:<a href='dump/'>查看</a><br/>" + html;
						}
					}
				} else {
					response.setHeader("Content-type", "text/html;charset=UTF-8");
					html = "请配置taotao.cloud.health.enabled=true,taotao.cloud.health.check.enabled=true";
				}

				response.setCharacterEncoding("UTF-8");
				response.getWriter().append(html);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				LogUtil.error(StarterNameConstant.HEALTH_STARTER, "/taotao/cloud/health/打开出错", e);
				response.getWriter().close();
			}
		}
	}

	@Override
	public void destroy() {

	}
}
