package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.contition.PrometheusEnabledCondition;
import com.taotao.cloud.prometheus.properties.PromethreusNoticeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@Conditional(PrometheusEnabledCondition.class)
@EnableConfigurationProperties(PromethreusNoticeProperties.class)
public class PromethuesConfig {

}
