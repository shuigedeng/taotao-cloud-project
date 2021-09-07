package com.taotao.cloud.prometheus.message;

import com.taotao.cloud.prometheus.httpclient.DingdingHttpClient;
import com.taotao.cloud.prometheus.model.DingDingNotice;
import com.taotao.cloud.prometheus.model.DingDingResult;
import com.taotao.cloud.prometheus.model.PromethuesNotice;
import com.taotao.cloud.prometheus.properties.DingDingNoticeProperties;
import com.taotao.cloud.prometheus.text.NoticeTextResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DingDingNoticeSendComponent<T extends PromethuesNotice> implements INoticeSendComponent<T> {

	private final DingdingHttpClient httpClient;

	private final NoticeTextResolver<T> noticeResolver;

	private final DingDingNoticeProperties dingDingNoticeProperty;

	private final Log logger = LogFactory.getLog(DingDingNoticeSendComponent.class);

	/**
	 * @param httpClient
	 * @param exceptionNoticeResolver
	 * @param dingDingNoticeProperty
	 */
	public DingDingNoticeSendComponent(DingdingHttpClient httpClient, NoticeTextResolver<T> noticeResolver,
			DingDingNoticeProperties dingDingNoticeProperty) {
		this.httpClient = httpClient;
		this.noticeResolver = noticeResolver;
		this.dingDingNoticeProperty = dingDingNoticeProperty;
	}

	/**
	 * @return the httpClient
	 */
	public DingdingHttpClient getHttpClient() {
		return httpClient;
	}

	@Override
	public void send(T exceptionNotice) {
		String noticeText = noticeResolver.resolve(exceptionNotice);
		DingDingNotice dingDingNotice = dingDingNoticeProperty.generateDingdingNotice(noticeText,
				exceptionNotice.getTitle());
		DingDingResult result = httpClient.doSend(dingDingNotice);
		logger.debug(result);
	}

}
