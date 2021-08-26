package com.taotao.cloud.prometheus.config.exceptionnotice;

import com.taotao.cloud.prometheus.config.annos.ConditionalOnExceptionNotice;
import com.taotao.cloud.prometheus.httpclient.DingdingHttpClient;
import com.taotao.cloud.prometheus.message.DingDingNoticeSendComponent;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.pojos.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.enums.DingdingTextType;
import com.taotao.cloud.prometheus.properties.notice.DingDingNoticeProperty;
import com.taotao.cloud.prometheus.text.ExceptionNoticeResolver;
import com.taotao.cloud.prometheus.text.markdown.ExceptionNoticeMarkdownMessageResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnExceptionNotice
@ConditionalOnProperty(value = "prometheus.dingding.enabled", havingValue = "true")
public class ExceptionNoticeDingdingSendingConfig {

	private final Log logger = LogFactory.getLog(ExceptionNoticeDingdingSendingConfig.class);

	@Bean("dingdingSendingComponent")
	@ConditionalOnMissingBean(name = "dingdingSendingComponent")
	public INoticeSendComponent<ExceptionNotice> sendComponent(DingdingHttpClient dingdingHttpClient,
			ExceptionNoticeResolver exceptionNoticeResolver, DingDingNoticeProperty dingDingNoticeProperty) {
		logger.debug("注册钉钉通知");
		INoticeSendComponent<ExceptionNotice> component = new DingDingNoticeSendComponent<ExceptionNotice>(
				dingdingHttpClient, exceptionNoticeResolver, dingDingNoticeProperty);
		return component;
	}

	@Bean
	@ConditionalOnMissingBean
	public ExceptionNoticeResolver ExceptionNoticeTextResolver(DingDingNoticeProperty dingDingNoticeProperty) {
		if (dingDingNoticeProperty.getDingdingTextType() == DingdingTextType.MARKDOWN)
			return new ExceptionNoticeMarkdownMessageResolver();
		return x -> x.createText();
	}

}
