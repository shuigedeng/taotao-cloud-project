package com.taotao.cloud.web.config;

import com.taotao.cloud.common.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 统一异常处理配置
 * @author xuzhanfu
 */
@Configuration
@ComponentScan(value="vip.mate.core.web.handler")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-error.yml")
public class ExceptionConfiguration {
}
