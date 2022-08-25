package com.taotao.cloud.monitor.kuding.config;

import com.taotao.cloud.monitor.kuding.httpclient.DefaultDingdingHttpClient;
import com.taotao.cloud.monitor.kuding.httpclient.DingdingHttpClient;
import com.taotao.cloud.monitor.kuding.properties.notice.DingDingNoticeProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
@ConditionalOnProperty(value = "prometheus.dingding.enabled", havingValue = "true")
@EnableConfigurationProperties({ DingDingNoticeProperty.class })
public class PromethuesDingDingNoticeConfig {

	@Bean
	@ConditionalOnMissingBean
	public DingdingHttpClient dingdingHttpClient(DingDingNoticeProperty dingDingNoticeProperty, Gson gson) {
		DingdingHttpClient dingdingHttpClient = new DefaultDingdingHttpClient(gson, dingDingNoticeProperty);
		return dingdingHttpClient;
	}
}
