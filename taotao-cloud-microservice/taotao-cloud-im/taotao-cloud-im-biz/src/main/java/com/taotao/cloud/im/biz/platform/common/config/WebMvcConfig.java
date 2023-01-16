package com.taotao.cloud.im.biz.platform.common.config;

import com.platform.common.version.VersionInterceptor;
import com.platform.common.web.interceptor.DeviceInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Resource
	private VersionInterceptor versionInterceptor;

	@Resource
	private DeviceInterceptor deviceInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	}

	/**
	 * 自定义拦截规则
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(versionInterceptor).addPathPatterns("/**");
		registry.addInterceptor(deviceInterceptor).addPathPatterns("/**");
	}

}
