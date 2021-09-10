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

import javax.mail.MessagingException;

/**
 * MailTemplate
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-09 11:41:17
 */
public interface MailTemplate {

	/**
	 * 发送文本邮件
	 *
	 * @param to      　　　　　收件人地址
	 * @param subject 　　邮件主题
	 * @param content 　　邮件内容
	 * @param cc      　　　　　抄送地址
	 * @author shuigedeng
	 * @since 2021-09-09 11:42:05
	 */
	void sendSimpleMail(String to, String subject, String content, String... cc);

	/**
	 * 发送HTML邮件
	 *
	 * @param to      收件人地址
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param cc      抄送地址
	 * @throws MessagingException 邮件发送异常
	 * @author shuigedeng
	 * @since 2021-09-09 11:41:55
	 */
	void sendHtmlMail(String to, String subject, String content, String... cc)
		throws MessagingException;

	/**
	 * 发送带附件的邮件
	 *
	 * @param to       收件人地址
	 * @param subject  邮件主题
	 * @param content  邮件内容
	 * @param filePath 附件地址
	 * @param cc       抄送地址
	 * @throws MessagingException 邮件发送异常
	 * @author shuigedeng
	 * @since 2021-09-09 11:41:41
	 */
	void sendAttachmentsMail(String to, String subject, String content, String filePath,
		String... cc) throws MessagingException;

	/**
	 * 发送正文中有静态资源的邮件
	 *
	 * @param to      收件人地址
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param rscPath 静态资源地址
	 * @param rscId   静态资源id
	 * @param cc      抄送地址
	 * @throws MessagingException 邮件发送异常
	 * @author shuigedeng
	 * @since 2021-09-09 11:41:26
	 */
	void sendResourceMail(String to, String subject, String content, String rscPath, String rscId,
		String... cc) throws MessagingException;

}
