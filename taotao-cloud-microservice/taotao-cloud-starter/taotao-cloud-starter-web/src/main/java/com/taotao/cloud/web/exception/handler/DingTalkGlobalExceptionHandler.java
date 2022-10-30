package com.taotao.cloud.web.exception.handler;

import com.taotao.cloud.dingtalk.model.DingerSender;
import com.taotao.cloud.web.exception.ExceptionHandleProperties;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 */
public class DingTalkGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final DingerSender sender;

	public DingTalkGlobalExceptionHandler(ExceptionHandleProperties config, DingerSender sender,
		String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		//DingTalkResponse response = sender
		//	.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()).atAll());
		//return new ExceptionNoticeResponse().setErrMsg(response.getResponse())
		//	.setSuccess(response.isSuccess());
		return null;
	}

}
