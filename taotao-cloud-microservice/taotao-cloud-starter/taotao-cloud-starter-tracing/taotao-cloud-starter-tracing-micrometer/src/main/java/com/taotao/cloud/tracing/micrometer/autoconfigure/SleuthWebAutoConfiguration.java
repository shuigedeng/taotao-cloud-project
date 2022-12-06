package com.taotao.cloud.tracing.micrometer.autoconfigure;// package com.taotao.cloud.opentracing.zipkin.autoconfigure;
//
// import com.taotao.cloud.common.utils.log.LogUtils;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * Sleuth Web 自动装配
//  *
//  */
// @ConditionalOnProperty(value = "spring.sleuth.web.servlet.enabled", matchIfMissing = true)
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
// @EnableConfigurationProperties(CustomSleuthWebProperties.class)
// @Configuration(proxyBeanMethods = false)
// public class SleuthWebAutoConfiguration {
//
// 	public static final String AUTOWIRED_WEB_MVC_HANDLER_PARSER = "Autowired WebMvcHandlerParser";
//
// 	@Bean
// 	public WebMvcHandlerParser webMvcHandlerParser(CustomSleuthWebProperties customSleuthWebProperties) {
// 		LogUtils.debug(AUTOWIRED_WEB_MVC_HANDLER_PARSER);
// 		WebMvcHandlerParser webMvcHandlerParser = new WebMvcHandlerParser();
// 		if (customSleuthWebProperties.getIgnoreHeaders() != null) {
// 			webMvcHandlerParser.setIgnoreHeaders(customSleuthWebProperties.getIgnoreHeaders());
// 		}
// 		if (customSleuthWebProperties.getIgnoreParameters() != null) {
// 			webMvcHandlerParser.setIgnoreParameters(customSleuthWebProperties.getIgnoreParameters());
// 		}
// 		return webMvcHandlerParser;
// 	}
// }
