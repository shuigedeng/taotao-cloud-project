package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.httpclient.DefaultDingdingHttpClient;
import com.taotao.cloud.prometheus.httpclient.DingdingHttpClient;
import com.taotao.cloud.prometheus.properties.DingDingNoticeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
@ConditionalOnProperty(value = "prometheus.dingding.enabled", havingValue = "true")
@EnableConfigurationProperties({ DingDingNoticeProperties.class })
public class PromethuesDingDingNoticeConfig {

	@Bean
	@ConditionalOnMissingBean
	public DingdingHttpClient dingdingHttpClient(DingDingNoticeProperties dingDingNoticeProperty, Gson gson) {
		DingdingHttpClient dingdingHttpClient = new DefaultDingdingHttpClient(gson, dingDingNoticeProperty);
		return dingdingHttpClient;
	}
}
