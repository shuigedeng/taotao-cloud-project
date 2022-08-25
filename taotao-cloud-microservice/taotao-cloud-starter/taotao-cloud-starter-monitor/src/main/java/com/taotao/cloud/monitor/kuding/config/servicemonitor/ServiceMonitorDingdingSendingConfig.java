package com.taotao.cloud.monitor.kuding.config.servicemonitor;

import com.taotao.cloud.monitor.kuding.httpclient.DingdingHttpClient;
import com.taotao.cloud.monitor.kuding.message.DingDingNoticeSendComponent;
import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.properties.enums.DingdingTextType;
import com.taotao.cloud.monitor.kuding.properties.notice.DingDingNoticeProperty;
import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.ServiceCheckNotice;
import com.taotao.cloud.monitor.kuding.text.ServiceMonitorResolver;
import com.taotao.cloud.monitor.kuding.text.markdown.ServiceMonitorMarkdownResolver;
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
	public INoticeSendComponent<ServiceCheckNotice> addSendComponent(DingdingHttpClient dingdingHttpClient,
                                                                     ServiceMonitorResolver exceptionNoticeResolver, DingDingNoticeProperty dingDingNoticeProperty) {
		logger.debug("注册钉钉通知");
		INoticeSendComponent<ServiceCheckNotice> component = new DingDingNoticeSendComponent<ServiceCheckNotice>(
				dingdingHttpClient, exceptionNoticeResolver, dingDingNoticeProperty);
		return component;
	}

	@Bean
	@ConditionalOnMissingBean
	public ServiceMonitorResolver serviceMonitorResolver(DingDingNoticeProperty dingDingNoticeProperty) {
		if (dingDingNoticeProperty.getDingdingTextType() == DingdingTextType.MARKDOWN)
			return new ServiceMonitorMarkdownResolver();
		return x -> x.generateText();
	}

}
