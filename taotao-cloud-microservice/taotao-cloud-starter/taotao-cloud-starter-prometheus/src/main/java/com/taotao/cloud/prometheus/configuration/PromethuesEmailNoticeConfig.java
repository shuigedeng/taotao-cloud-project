package com.taotao.cloud.prometheus.configuration;

import com.taotao.cloud.prometheus.properties.EmailNoticeProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(value = "prometheus.email.enabled", havingValue = "true")
@EnableConfigurationProperties({ EmailNoticeProperty.class })
public class PromethuesEmailNoticeConfig {

}
