package com.taotao.cloud.web.exception.handler;

import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.entity.DingerResponse;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.model.DingerSender;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;

/**
 * 钉钉消息通知
 */
public class DingTalkExceptionHandler extends AbstractExceptionHandler {

	private final DingerSender dingerSender;

	public DingTalkExceptionHandler(ExceptionHandleProperties config, DingerSender sender,
									String applicationName) {
		super(config, applicationName);
		this.dingerSender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		DingerResponse dingerResponse = dingerSender.send(MessageSubType.TEXT, DingerRequest.request(sendMessage.toString()));
		ExceptionNoticeResponse response = new ExceptionNoticeResponse();
		response.setErrMsg(dingerResponse.getData());
		response.setSuccess("200".equals(dingerResponse.getCode()));
		return response;
	}

	@Override
	protected void initThread() {
		this.setName("taotao-cloud-dingtalk-exception-task");
		this.start();
	}
}
