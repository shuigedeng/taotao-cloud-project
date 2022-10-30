package com.taotao.cloud.web.exception.handler;

import com.taotao.cloud.web.exception.ExceptionHandleProperties;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * 多渠道异常处理类
 *
 * @author lingting 2022/10/28 10:12
 */
public class MultiGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final Object mailSender;

	private final String[] receiveEmails;

	private final List<Object> dingTalkSenders;

	public MultiGlobalExceptionHandler(ExceptionHandleProperties config, String applicationName,
		Object mailSender) {
		super(config, applicationName);
		this.mailSender = mailSender;
		if (CollectionUtils.isEmpty(config.getReceiveEmails())) {
			this.receiveEmails = new String[0];
		} else {
			this.receiveEmails = config.getReceiveEmails().toArray(new String[0]);
		}

		ExceptionHandleProperties.DingTalkProperties dingTalkProperties = config.getDingTalk();
		if (dingTalkProperties == null || CollectionUtils.isEmpty(
			dingTalkProperties.getSenders())) {
			this.dingTalkSenders = new ArrayList<>(0);
		} else {
			this.dingTalkSenders = new ArrayList<>(dingTalkProperties.getSenders().size());
			//for (ExceptionHandleProperties.DingTalkProperties.Sender s : dingTalkProperties.getSenders()) {
			//	DingTalkSender sender = new DingTalkSender(s.getUrl());
			//	sender.setSecret(s.getSecret());
			//	dingTalkSenders.add(sender);
			//}
		}

	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		//if (receiveEmails != null && receiveEmails.length > 0) {
		//	try {
		//		String[] to = config.getReceiveEmails().toArray(new String[0]);
		//		((MailSender) mailSender).sendTextMail("异常警告", sendMessage.toString(), to);
		//	} catch (Exception e) {
		//		log.error("邮箱异常通知发送异常! emails: {}", Arrays.toString(receiveEmails));
		//	}
		//}
		//
		//for (Object obj : dingTalkSenders) {
		//	DingTalkSender sender = (DingTalkSender) obj;
		//	try {
		//		DingTalkTextMessage message = new DingTalkTextMessage().setContent(
		//			sendMessage.toString());
		//		if (Boolean.TRUE.equals(config.getDingTalk().getAtAll())) {
		//			message.atAll();
		//		}
		//		sender.sendMessage(message);
		//	} catch (Exception e) {
		//		log.error("钉钉异常通知发送异常! webHook: {}", sender.getUrl(), e);
		//	}
		//}
		ExceptionNoticeResponse response = new ExceptionNoticeResponse();
		response.setSuccess(true);
		return response;
	}

}
