/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.exception.SendFailedException;
import com.taotao.cloud.sms.handler.AbstractSendHandler;
import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.utils.StringUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * 阿里云短信发送处理
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:50:18
 */
public class AliyunSendHandler extends AbstractSendHandler<AliyunProperties> {

	private static final String OK = "OK";

	private static final String PRODUCT = "Dysmsapi";

	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	private final ObjectMapper objectMapper;

	private final IAcsClient acsClient;

	/**
	 * 构造阿里云短信发送处理
	 *
	 * @param properties     阿里云短信配置
	 * @param eventPublisher spring应用事件发布器
	 * @param objectMapper   objectMapper
	 */
	public AliyunSendHandler(AliyunProperties properties, ApplicationEventPublisher eventPublisher,
		ObjectMapper objectMapper) {
		super(properties, eventPublisher);
		this.objectMapper = objectMapper;

		String endPoint = properties.getEndpoint();
		String accessKeyId = properties.getAccessKeyId();
		String accessKeySecret = properties.getAccessKeySecret();

		IClientProfile profile = DefaultProfile.getProfile(endPoint, accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint(endPoint, PRODUCT, DOMAIN);

		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		acsClient = new DefaultAcsClient(profile);
	}

	@Override
	public boolean send(NoticeData noticeData, Collection<String> phones) {
		String paramString;
		try {
			paramString = objectMapper.writeValueAsString(noticeData.getParams());
		} catch (Exception e) {
			LogUtil.debug(e.getMessage(), e);
			publishSendFailEvent(noticeData, phones, e);
			return false;
		}

		SendSmsRequest request = new SendSmsRequest();
		request.setSysMethod(MethodType.POST);
		request.setPhoneNumbers(StringUtils.join(phones, ","));
		request.setSignName(properties.getSignName());
		request.setTemplateCode(properties.getTemplates(noticeData.getType()));
		request.setTemplateParam(paramString);

		try {
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

			if (OK.equals(sendSmsResponse.getCode())) {
				publishSendSuccessEvent(noticeData, phones);
				return true;
			}

			LogUtil.debug("send fail[code={}, message={}]", sendSmsResponse.getCode(),
				sendSmsResponse.getMessage());

			publishSendFailEvent(noticeData, phones,
				new SendFailedException(sendSmsResponse.getMessage()));
		} catch (Exception e) {
			LogUtil.debug(e.getMessage(), e);
			publishSendFailEvent(noticeData, phones, e);
		}
		return false;
	}

	@Override
	public String getChannelName() {
		return "aliyun";
	}
}
