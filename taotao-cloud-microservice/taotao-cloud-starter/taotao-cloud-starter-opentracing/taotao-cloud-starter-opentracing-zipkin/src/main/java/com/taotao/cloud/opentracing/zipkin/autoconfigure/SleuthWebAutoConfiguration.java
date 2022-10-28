package com.taotao.cloud.opentracing.zipkin.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sleuth.web.WebMvcHandlerParser;
import org.ylzl.eden.spring.cloud.zipkin.env.CustomSleuthWebProperties;

/**
 * Sleuth Web 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = "spring.sleuth.web.servlet.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(CustomSleuthWebProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SleuthWebAutoConfiguration {

	public static final String AUTOWIRED_WEB_MVC_HANDLER_PARSER = "Autowired WebMvcHandlerParser";

	@Bean
	public WebMvcHandlerParser webMvcHandlerParser(CustomSleuthWebProperties customSleuthWebProperties) {
		log.debug(AUTOWIRED_WEB_MVC_HANDLER_PARSER);
		WebMvcHandlerParser webMvcHandlerParser = new WebMvcHandlerParser();
		if (customSleuthWebProperties.getIgnoreHeaders() != null) {
			webMvcHandlerParser.setIgnoreHeaders(customSleuthWebProperties.getIgnoreHeaders());
		}
		if (customSleuthWebProperties.getIgnoreParameters() != null) {
			webMvcHandlerParser.setIgnoreParameters(customSleuthWebProperties.getIgnoreParameters());
		}
		return webMvcHandlerParser;
	}
}
