/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.DumpProperties;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HealthReportFilter
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:11:57
 */
public class HealthReportFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String contextPath = org.springframework.util.StringUtils.trimTrailingCharacter(request.getContextPath(), '/');
		String uri = request.getRequestURI();

		HealthCheckProvider healthProvider = ContextUtil.getBean(HealthCheckProvider.class, true);
		DumpProperties dumpProperties = ContextUtil.getBean(DumpProperties.class, true);
		if (Objects.nonNull(healthProvider)
			&& Objects.nonNull(dumpProperties) && uri.startsWith(contextPath + "/health/report")) {
			try {
				String html;

				boolean isAnalyse = !"false".equalsIgnoreCase(request.getParameter("isAnalyse"));

				Report report = healthProvider.getReport(isAnalyse);
				if (request.getContentType() != null && request.getContentType().contains("json")) {
					response.setHeader("Content-type", "application/json;charset=UTF-8");
					html = report.toJson();
					ResponseUtil.success(response, html);
					return;
				} else {
					response.setHeader("Content-type", "text/html;charset=UTF-8");
					html = report
						.toHtml()
						.replace("\r\n", "<br/>")
						.replace("\n", "<br/>")
						.replace("/n", "\n")
						.replace("/r", "\r");

					if (dumpProperties.isEnabled()) {
						html = "dump信息:<a href='/health/dump/'>查看</a><br/>" + html;
					}
				}

				response.setCharacterEncoding("UTF-8");
				response.getWriter().append(html);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				LogUtil.error(e, StarterName.HEALTH_STARTER, "/health/report打开出错");
				response.getWriter().close();
			}
		}
	}

	@Override
	public void destroy() {

	}
}
