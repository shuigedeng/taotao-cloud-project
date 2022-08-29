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
package com.taotao.cloud.web.configuration;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.web.servlet.filter.TenantFilter;
import com.taotao.cloud.web.servlet.filter.TraceFilter;
import com.taotao.cloud.web.servlet.filter.VersionFilter;
import com.taotao.cloud.web.servlet.filter.WebContextFilter;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.servlet.listener.MyListener;
import com.taotao.cloud.web.servlet.servlet.MyAsyncServlet;
import com.taotao.cloud.web.servlet.servlet.MyServlet;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.WebApplicationInitializer;

/**
 * ServletConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:50
 */
@AutoConfiguration
public class ServletAutoConfiguration implements WebApplicationInitializer, InitializingBean {
	/**
	 * filterProperties
	 */
	@Autowired
	private FilterProperties filterProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(ServletAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		LogUtils.info("servletContext.getServerInfo=== {}", servletContext.getServerInfo());

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

	//注册 Servlet 到容器中
	@Bean
	public ServletRegistrationBean<MyServlet> myServlet(){
		ServletRegistrationBean<MyServlet> servletRegistrationBean = new ServletRegistrationBean<>();
		servletRegistrationBean.setServlet(new MyServlet());
		servletRegistrationBean.setLoadOnStartup(1);
		servletRegistrationBean.setAsyncSupported(true);
		servletRegistrationBean.setUrlMappings(List.of("/myServlet"));
		LogUtils.info("注册servlet成功，名称: {}", MyServlet.class.getName());
		return servletRegistrationBean;
	}
	@Bean
	public ServletRegistrationBean<MyAsyncServlet> myAsyncServlet(){
		ServletRegistrationBean<MyAsyncServlet> servletRegistrationBean = new ServletRegistrationBean<>();
		servletRegistrationBean.setServlet(new MyAsyncServlet());
		servletRegistrationBean.setLoadOnStartup(1);
		servletRegistrationBean.setAsyncSupported(true);
		servletRegistrationBean.setUrlMappings(List.of("/my/asyncServlet"));
		LogUtils.info("注册servlet成功，名称: {}", MyAsyncServlet.class.getName());
		return servletRegistrationBean;
	}

	@Bean
	public ServletListenerRegistrationBean<MyListener> listenerServletListenerRegistrationBean() {
		ServletListenerRegistrationBean<MyListener> bean = new ServletListenerRegistrationBean<>();
		bean.setListener(new MyListener());
		return bean;
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "version", havingValue = "true")
	public FilterRegistrationBean<VersionFilter> lbIsolationFilter() {
		FilterRegistrationBean<VersionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new VersionFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(VersionFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 6);
		return registrationBean;
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "tenant", havingValue = "true")
	public FilterRegistrationBean<TenantFilter> tenantFilter() {
		FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TenantFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 5);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TraceFilter> traceFilter() {
		FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TraceFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TraceFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 4);
		return registrationBean;
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "webContext", havingValue = "true")
	public FilterRegistrationBean<WebContextFilter> webContextFilter() {
		FilterRegistrationBean<WebContextFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new WebContextFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(WebContextFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
		return registrationBean;
	}





}
