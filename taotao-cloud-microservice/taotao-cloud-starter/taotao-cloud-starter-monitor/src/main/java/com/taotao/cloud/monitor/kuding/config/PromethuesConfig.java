package com.taotao.cloud.monitor.kuding.config;

import com.taotao.cloud.monitor.kuding.properties.NoticeProperties;
import com.taotao.cloud.monitor.kuding.config.conditions.PrometheusEnabledCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@Conditional(PrometheusEnabledCondition.class)
@EnableConfigurationProperties(NoticeProperties.class)
public class PromethuesConfig {

}
