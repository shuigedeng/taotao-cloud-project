/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.properties.FilterProperties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.WebApplicationInitializer;

/**
 * ServletConfiguration
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/04/08 15:26
 */
@EnableConfigurationProperties({FilterProperties.class})
public class ServletConfiguration implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//注册servlet
//		ServletRegistration.Dynamic myServlet = servletContext
//			.addServlet("myServlet", MyServlet.class);
//		myServlet.addMapping("/myServlet");
//		myServlet.setLoadOnStartup(0);

		//注册filter
//		Dynamic lbIsolationFilter = servletContext
//			.addFilter("lbIsolationFilter", LbIsolationFilter.class);
//		lbIsolationFilter.addMappingForUrlPatterns(
//			EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), false, "/*");
//
//		Dynamic myFilter = servletContext.addFilter("myFilter", TenantFilter.class);
//		myFilter.addMappingForUrlPatterns(
//			EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), false, "/*");
//
//		Dynamic traceFilter = servletContext.addFilter("traceFilter", TraceFilter.class);
//		traceFilter.addMappingForUrlPatterns(
//			EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), false, "/*");
	}

}
