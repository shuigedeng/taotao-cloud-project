package com.taotao.cloud.web.exception.handler;

import com.taotao.cloud.web.exception.ExceptionHandleProperties;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;
import org.springframework.mail.MailSender;

/**
 * 异常邮件通知
 *
 * @author lingting 2020/6/12 0:25
 */
public class MailGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final MailSender sender;

	public MailGlobalExceptionHandler(ExceptionHandleProperties config, MailSender sender,
		String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		String[] to = config.getReceiveEmails().toArray(new String[0]);
		//MailSendInfo mailSendInfo = sender.sendTextMail("异常警告", sendMessage.toString(), to);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		//return new ExceptionNoticeResponse().setSuccess(mailSendInfo.getSuccess())
		//	.setErrMsg(mailSendInfo.getErrorMsg());
		return null;
	}

}
