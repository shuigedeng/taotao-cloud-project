package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.httpclient.DefaultDingdingHttpClient;
import com.taotao.cloud.prometheus.httpclient.DingdingHttpClient;
import com.taotao.cloud.prometheus.properties.notice.DingDingNoticeProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
