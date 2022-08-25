package com.taotao.cloud.monitor.kuding.config;

import com.taotao.cloud.monitor.kuding.properties.PromethreusNoticeProperties;
import com.taotao.cloud.monitor.kuding.config.conditions.PrometheusEnabledCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@Conditional(PrometheusEnabledCondition.class)
@EnableConfigurationProperties(PromethreusNoticeProperties.class)
public class PromethuesConfig {

}
