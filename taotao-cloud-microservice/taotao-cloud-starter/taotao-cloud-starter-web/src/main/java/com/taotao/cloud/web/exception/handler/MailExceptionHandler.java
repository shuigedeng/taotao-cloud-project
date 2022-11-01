package com.taotao.cloud.web.exception.handler;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;

/**
 * 异常邮件通知
 */
public class MailExceptionHandler extends AbstractExceptionHandler {

	private final JavaMailSender mailSender;
	private final MailProperties mailProperties;

	public MailExceptionHandler(MailProperties mailProperties, ExceptionHandleProperties config, JavaMailSender sender,
								String applicationName) {
		super(config, applicationName);
		this.mailSender = sender;
		this.mailProperties = mailProperties;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		String[] to = config.getReceiveEmails().toArray(new String[0]);

		sendEmail(to, sendMessage);

		ExceptionNoticeResponse response = new ExceptionNoticeResponse();
		response.setErrMsg("发送成功");
		response.setSuccess(true);

		return response;
	}

	@Override
	protected void initThread() {
		this.setName("taotao-cloud-mail-exception-task");
		this.start();
	}

	private void sendEmail(String[] to, ExceptionMessage message) {
		String title = "请求异常信息监控";
		String content = MessageFormat.format(loadTemplate(),
			message.getApplicationName(),
			message.getTraceId(),
			message.getIp(),
			message.getRequestUri(),
			message.getMessage(),
			message.getNumber(),
			message.getTime(),
			message.getThreadId(),
			message.getStack());

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(mailProperties.getUsername());
			if (to == null || to.length <= 0) {
				helper.setTo(mailProperties.getUsername());
			} else {
				helper.setTo(to);
			}
			helper.setSubject(title);
			helper.setText(content, true);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			LogUtils.error("", e);
		}
	}

	private static String loadTemplate() {
		return "<h5>" + "异常信息" + "：</span>" +
			"<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n"
			+
			"   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
			"      <tr>\n" +
			"         <td width=\"10%\" >" + "服务名" + "</td>\n" +
			"         <td width=\"10%\" >" + "traceId" + "</td>\n" +
			"         <td width=\"5%\" >" + "ip" + "</td>\n" +
			"         <td width=\"10%\" >" + "请求地址" + "</td>\n" +
			"         <td width=\"10%\" >" + "消息" + "</td>\n" +
			"         <td width=\"5%\" >" + "数量" + "</td>\n" +
			"         <td width=\"5%\" >" + "最新触发时间" + "</td>\n" +
			"         <td width=\"5%\" >" + "线程id" + "</td>\n" +
			"         <td width=\"40%\" >" + "堆栈信息" + "</td>\n" +
			"      </tr>\n" +
			"   </thead>\n" +
			"   <tbody>\n" +
			"      <tr>\n" +
			"         <td>{0}</td>\n" +
			"         <td>{1}</td>\n" +
			"         <td>{2}</td>\n" +
			"         <td>{3}</td>\n" +
			"         <td>{4}</td>\n" +
			"         <td>{5}</td>\n" +
			"         <td>{6}</td>\n" +
			"         <td>{7}</td>\n" +
			"         <td>{8}</td>\n" +
			"      </tr>\n" +
			"   </tbody>\n" +
			"</table>";
	}
}
