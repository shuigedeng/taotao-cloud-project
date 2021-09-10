/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.mail.template;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ObjectUtils;

/**
 * JavaMailTemplate
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-09 11:41:05
 */
public class JavaMailTemplate implements MailTemplate {

	private final JavaMailSender mailSender;

	private final MailProperties mailProperties;

	public JavaMailTemplate(JavaMailSender mailSender,
		MailProperties mailProperties) {
		this.mailSender = mailSender;
		this.mailProperties = mailProperties;
	}

	@Override
	public void sendSimpleMail(String to, String subject, String content, String... cc) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailProperties.getUsername());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		if (!ObjectUtils.isEmpty(cc)) {
			message.setCc(cc);
		}
		mailSender.send(message);
	}

	@Override
	public void sendHtmlMail(String to, String subject, String content, String... cc)
		throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
		mailSender.send(message);
	}

	@Override
	public void sendAttachmentsMail(String to, String subject, String content, String filePath,
		String... cc) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
		FileSystemResource file = new FileSystemResource(new File(filePath));
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
		helper.addAttachment(fileName, file);
		mailSender.send(message);
	}

	@Override
	public void sendResourceMail(String to, String subject, String content, String rscPath,
		String rscId, String... cc) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
		FileSystemResource res = new FileSystemResource(new File(rscPath));
		helper.addInline(rscId, res);
		mailSender.send(message);
	}

	/**
	 * 统一封装MimeMessageHelper
	 *
	 * @param to      收件人地址
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param message 消息对象
	 * @param cc      抄送地址
	 * @return MimeMessageHelper
	 * @throws MessagingException 异常
	 */
	private MimeMessageHelper buildHelper(String to, String subject, String content,
		MimeMessage message, String... cc) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(mailProperties.getUsername());
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content, true);
		if (!ObjectUtils.isEmpty(cc)) {
			helper.setCc(cc);
		}
		return helper;
	}
}
