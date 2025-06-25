package com.taotao.cloud.ai.alibaba.tool_calling.component.weather.function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author yingzi
 * @date 2025/3/25:15:23
 */
@Configuration
@ConditionalOnClass(WeatherService.class)
@EnableConfigurationProperties(WeatherProperties.class)
@ConditionalOnProperty(prefix = "spring.ai.toolcalling.weather", name = "enabled", havingValue = "true")
public class WeatherAutoConfiguration {

    @Bean(name = "getWeatherFunction")
    @ConditionalOnMissingBean
    @Description("Use api.weather to get weather information.")
    public WeatherService getWeatherServiceFunction(WeatherProperties properties) {
        return new WeatherService(properties);
    }

}
