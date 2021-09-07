package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.annotation.ConditionalOnServiceMonitor;
import com.taotao.cloud.prometheus.enums.DingdingTextType;
import com.taotao.cloud.prometheus.httpclient.DingdingHttpClient;
import com.taotao.cloud.prometheus.message.DingDingNoticeSendComponent;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.ServiceCheckNotice;
import com.taotao.cloud.prometheus.properties.DingDingNoticeProperties;
import com.taotao.cloud.prometheus.text.ServiceMonitorMarkdownResolver;
import com.taotao.cloud.prometheus.text.ServiceMonitorResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnServiceMonitor
public class ServiceMonitorDingdingSendingConfig {

	private final Log logger = LogFactory.getLog(ServiceMonitorDingdingSendingConfig.class);

	@Bean
	@ConditionalOnMissingBean(parameterizedContainer = INoticeSendComponent.class)
	public INoticeSendComponent<ServiceCheckNotice> addSendComponent(
		DingdingHttpClient dingdingHttpClient,
			ServiceMonitorResolver exceptionNoticeResolver, DingDingNoticeProperties dingDingNoticeProperty) {
		logger.debug("注册钉钉通知");
		INoticeSendComponent<ServiceCheckNotice> component = new DingDingNoticeSendComponent<ServiceCheckNotice>(
				dingdingHttpClient, exceptionNoticeResolver, dingDingNoticeProperty);
		return component;
	}

	@Bean
	@ConditionalOnMissingBean
	public ServiceMonitorResolver serviceMonitorResolver(
		DingDingNoticeProperties dingDingNoticeProperty) {
		if (dingDingNoticeProperty.getDingdingTextType() == DingdingTextType.MARKDOWN)
			return new ServiceMonitorMarkdownResolver();
		return x -> x.generateText();
	}

}
