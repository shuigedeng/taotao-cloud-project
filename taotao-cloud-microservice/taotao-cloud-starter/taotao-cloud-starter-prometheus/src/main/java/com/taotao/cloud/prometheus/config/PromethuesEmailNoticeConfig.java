package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.properties.EmailNoticeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(value = "prometheus.email.enabled", havingValue = "true")
@EnableConfigurationProperties({ EmailNoticeProperties.class })
public class PromethuesEmailNoticeConfig {

}
