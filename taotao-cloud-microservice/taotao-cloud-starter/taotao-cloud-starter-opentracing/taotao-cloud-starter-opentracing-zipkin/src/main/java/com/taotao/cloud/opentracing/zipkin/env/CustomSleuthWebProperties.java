package com.taotao.cloud.opentracing.zipkin.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义 Sleuth Web 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties("spring.sleuth.web.servlet")
public class CustomSleuthWebProperties {

	private String ignoreHeaders;

	private String ignoreParameters;
}
