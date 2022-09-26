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
package com.taotao.cloud.sms.aliyun;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sms.common.exception.SendFailedException;
import com.taotao.cloud.sms.common.handler.AbstractSendHandler;
import com.taotao.cloud.sms.common.model.NoticeData;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

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

	private final com.aliyun.dysmsapi20170525.Client aliyunSmsClient;
	private final AsyncClient asyncClient;


	/**
	 * 构造阿里云短信发送处理
	 *
	 * @param properties     阿里云短信配置
	 * @param eventPublisher spring应用事件发布器
	 * @param objectMapper   objectMapper
	 */
	public AliyunSendHandler(AliyunProperties properties, ApplicationEventPublisher eventPublisher,
							 ObjectMapper objectMapper) throws Exception {
		super(properties, eventPublisher);
		this.objectMapper = objectMapper;

		String endPoint = properties.getEndpoint();
		String accessKeyId = properties.getAccessKeyId();
		String accessKeySecret = properties.getAccessKeySecret();

		com.aliyun.teaopenapi.models.Config config = new Config()
			// 您的 AccessKey ID
			.setAccessKeyId(accessKeyId)
			// 您的 AccessKey Secret
			.setAccessKeySecret(accessKeySecret);
		// 访问的域名
		config.endpoint = DOMAIN;
		this.aliyunSmsClient = new com.aliyun.dysmsapi20170525.Client(config);

		// ******************************异步客户端******************************
		// Configure Credentials authentication information, including ak, secret, token
		StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
			.accessKeyId(accessKeyId)
			.accessKeySecret(accessKeySecret)
			//.securityToken("<your-token>") // use STS token
			.build());

		// Configure the Client
		this.asyncClient = AsyncClient.builder()
			.region(endPoint) // Region ID
			//.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
			.credentialsProvider(provider)
			//.serviceConfiguration(Configuration.create()) // Service-level configuration
			// Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
			.overrideConfiguration(
				ClientOverrideConfiguration.create()
					.setEndpointOverride(DOMAIN)
				//.setReadTimeout(Duration.ofSeconds(30))
			)
			.build();
	}

	@Override
	public boolean send(NoticeData noticeData, Collection<String> phones) {
		String paramString;
		try {
			paramString = objectMapper.writeValueAsString(noticeData.getParams());
		} catch (Exception e) {
			LogUtils.debug(e.getMessage(), e);
			publishSendFailEvent(noticeData, phones, e, null);
			return false;
		}

		com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
			.setSignName(properties.getSignName())
			.setTemplateCode(properties.getTemplates(noticeData.getType()))
			.setPhoneNumbers(StringUtils.join(phones, ","))
			.setTemplateParam(paramString);

		// Parameter settings for API request
		SendSmsRequest asyncSendSmsRequest = SendSmsRequest.builder()
			.signName(properties.getSignName())
			.templateCode(properties.getTemplates(noticeData.getType()))
			.phoneNumbers(StringUtils.join(phones, ","))
			.templateParam(paramString)
			// Request-level configuration rewrite, can set Http request parameters, etc.
			// .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
			.build();

		com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse asyncSendSmsResponse = null;
		SendSmsResponse sendSmsResponse = null;
		try {
			if (noticeData.isAsnyc()) {
				CompletableFuture<com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse> response = asyncClient.sendSms(asyncSendSmsRequest);
				// Synchronously get the return value of the API request
				asyncSendSmsResponse = response.get();
				com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody body = asyncSendSmsResponse.getBody();
				if (OK.equals(body.getCode())) {
					publishSendSuccessEvent(noticeData, phones, asyncSendSmsResponse);
					return true;
				}

				LogUtils.debug("send fail[code={}, message={}]", body.getCode(), body.getMessage());

				publishSendFailEvent(noticeData, phones, new SendFailedException(body.getMessage()), asyncSendSmsResponse);
			} else {
				sendSmsResponse = aliyunSmsClient.sendSms(sendSmsRequest);
				SendSmsResponseBody body = sendSmsResponse.getBody();
				if (OK.equals(body.getCode())) {
					publishSendSuccessEvent(noticeData, phones, sendSmsResponse);
					return true;
				}

				LogUtils.debug("send fail[code={}, message={}]", body.getCode(), body.getMessage());

				publishSendFailEvent(noticeData, phones, new SendFailedException(body.getMessage()), sendSmsResponse);
			}
		} catch (Exception e) {
			LogUtils.debug(e.getMessage(), e);
			publishSendFailEvent(noticeData, phones, e, noticeData.isAsnyc() ? asyncSendSmsResponse : sendSmsResponse);
		}
		return false;
	}

	@Override
	public String getChannelName() {
		return "aliyun";
	}
}
