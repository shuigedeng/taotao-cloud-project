package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.annotation.ConditionalOnExceptionNotice;
import com.taotao.cloud.prometheus.enums.DingdingTextType;
import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.text.ExceptionNoticeMarkdownMessageResolver;
import com.taotao.cloud.prometheus.text.ExceptionNoticeResolver;
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

	//@Bean("dingdingSendingComponent")
	//@ConditionalOnMissingBean(name = "dingdingSendingComponent")
	//public INoticeSendComponent<ExceptionNotice> sendComponent(DingdingHttpClient dingdingHttpClient,
	//		ExceptionNoticeResolver exceptionNoticeResolver, DingDingNoticeProperty dingDingNoticeProperty) {
	//	logger.debug("注册钉钉通知");
	//	INoticeSendComponent<ExceptionNotice> component = new DingDingNoticeSendComponent<ExceptionNotice>(
	//			dingdingHttpClient, exceptionNoticeResolver, dingDingNoticeProperty);
	//	return component;
	//}
	//
	//@Bean
	//@ConditionalOnMissingBean
	//public ExceptionNoticeResolver ExceptionNoticeTextResolver(DingDingNoticeProperty dingDingNoticeProperty) {
	//	if (dingDingNoticeProperty.getDingdingTextType() == DingdingTextType.MARKDOWN)
	//		return new ExceptionNoticeMarkdownMessageResolver();
	//	return x -> x.createText();
	//}

}
