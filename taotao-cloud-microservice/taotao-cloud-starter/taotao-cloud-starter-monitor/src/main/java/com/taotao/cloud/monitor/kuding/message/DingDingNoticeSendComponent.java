package com.taotao.cloud.monitor.kuding.message;

import com.taotao.cloud.monitor.kuding.httpclient.DingdingHttpClient;
import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingNotice;
import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingResult;
import com.taotao.cloud.monitor.kuding.pojos.notice.Notice;
import com.taotao.cloud.monitor.kuding.properties.notice.DingDingNoticeProperty;
import com.taotao.cloud.monitor.kuding.text.NoticeTextResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DingDingNoticeSendComponent<T extends Notice> implements INoticeSendComponent<T> {

	private final DingdingHttpClient httpClient;

	private final NoticeTextResolver<T> noticeResolver;

	private final DingDingNoticeProperty dingDingNoticeProperty;

	private final Log logger = LogFactory.getLog(DingDingNoticeSendComponent.class);

	/**
	 * @param httpClient
	 * @param dingDingNoticeProperty
	 */
	public DingDingNoticeSendComponent(DingdingHttpClient httpClient,
		NoticeTextResolver<T> noticeResolver,
		DingDingNoticeProperty dingDingNoticeProperty) {
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
