package com.taotao.cloud.monitor.kuding.config;

import com.taotao.cloud.monitor.kuding.properties.notice.EmailNoticeProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(value = "prometheus.email.enabled", havingValue = "true")
@EnableConfigurationProperties({ EmailNoticeProperty.class })
public class PromethuesEmailNoticeConfig {

}
