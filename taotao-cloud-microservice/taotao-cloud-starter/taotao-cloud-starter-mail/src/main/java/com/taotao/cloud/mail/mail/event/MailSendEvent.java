package com.taotao.cloud.mail.mail.event;

import com.taotao.cloud.mail.mail.model.MailSendInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 18:00
 */
public class MailSendEvent extends ApplicationEvent {

	public MailSendEvent(MailSendInfo mailSendInfo) {
		super(mailSendInfo);
	}


	@Override
	public String toString() {
		return super.toString();
	}
}
