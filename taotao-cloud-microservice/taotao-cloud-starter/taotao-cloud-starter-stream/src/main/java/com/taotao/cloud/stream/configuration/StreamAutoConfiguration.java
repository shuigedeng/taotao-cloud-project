package com.taotao.cloud.stream.configuration;

import com.taotao.cloud.common.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * StreamAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 09:41:50
 */
@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:stream.yml")
public class StreamAutoConfiguration {

}
