package com.taotao.cloud.web.version;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * WebMvcRegistrations
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-27 20:20:42
 */
@Configuration
@EnableConfigurationProperties(ApiVersionProperties.class)
public class WebMvcRegistrationsConfig implements WebMvcRegistrations {

	@Autowired
	private ApiVersionProperties apiVersionProperties;

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		if (!apiVersionProperties.isEnabled()) {
			return WebMvcRegistrations.super.getRequestMappingHandlerMapping();
		}

		LogUtil.info(
			"【初始化配置-ApiVersionRequestMappingHandlerMapping】默认配置为true，当前环境为true：RESTful API接口版本控制，执行初始化 ...");
		return new ApiVersionRequestMappingHandlerMapping(apiVersionProperties);
	}

}
