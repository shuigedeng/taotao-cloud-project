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
package com.taotao.cloud.sms.netease;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.common.exception.SendFailedException;
import com.taotao.cloud.sms.common.handler.AbstractSendHandler;
import com.taotao.cloud.sms.common.model.NoticeData;
import com.taotao.cloud.sms.common.utils.RandomUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网易云信发送处理
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:36
 */
public class NeteaseCloudSendHandler extends AbstractSendHandler<NeteaseCloudProperties> {

	/**
	 * 请求路径URL
	 */
	private static final String SERVER_URL = "https://api.netease.im/sms/sendtemplate.action";

	private final ObjectMapper objectMapper;

	private final RestTemplate restTemplate;

	public NeteaseCloudSendHandler(NeteaseCloudProperties properties,
		ApplicationEventPublisher eventPublisher,
		ObjectMapper objectMapper, RestTemplate restTemplate) {
		super(properties, eventPublisher);
		this.objectMapper = objectMapper;
		this.restTemplate = restTemplate;
	}

	private String buildStringArray(Collection<String> items) {
		boolean firstParam = true;
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (String item : items) {
			if (!firstParam) {
				builder.append(",");
			}
			builder.append("'");
			builder.append(item);
			builder.append("'");
			firstParam = false;
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean send(NoticeData noticeData, Collection<String> phones) {
		String type = noticeData.getType();

		String templateId = properties.getTemplates(type);

		if (templateId == null) {
			LogUtil.debug("templateId invalid");
			publishSendFailEvent(noticeData, phones, new SendFailedException("templateId invalid"));
			return false;
		}

		List<String> paramsOrder = properties.getParamsOrder(type);

		ArrayList<String> params = new ArrayList<>();

		if (!paramsOrder.isEmpty()) {
			Map<String, String> paramMap = noticeData.getParams();
			for (String paramName : paramsOrder) {
				String paramValue = paramMap.get(paramName);

				params.add(paramValue);
			}
		}

		String nonce = RandomUtils.nextString(6);
		String paramsString = buildStringArray(params);
		String mobilesString = buildStringArray(phones);
		String curTime = String.valueOf((new Date()).getTime() / 1000L);
		String checkSum = CheckSumBuilder.getCheckSum(properties.getAppSecret(), nonce, curTime);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("AppKey", properties.getAppKey());
		headers.set("CurTime", curTime);
		headers.set("CheckSum", checkSum);
		headers.set("Nonce", nonce);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("templateid", templateId);
		body.add("mobiles", mobilesString);
		body.add("params", paramsString);

		try {
			ResponseEntity<String> httpResponse = restTemplate.exchange(SERVER_URL, HttpMethod.POST,
				new HttpEntity<>(body, headers), String.class);

			if (httpResponse.getBody() == null) {
				LogUtil.debug("response body ie null");
				publishSendFailEvent(noticeData, phones,
					new SendFailedException("response body ie null"));
				return false;
			}

			String responseContent = httpResponse.getBody();

			LogUtil.debug("responseContent: {}", responseContent);

			NeteaseCloudResult result = objectMapper.readValue(responseContent,
				NeteaseCloudResult.class);

			boolean succeed = NeteaseCloudResult.SUCCESS_CODE.equals(result.getCode());
			if (succeed) {
				publishSendSuccessEvent(noticeData, phones);
			} else {
				publishSendFailEvent(noticeData, phones, new SendFailedException(result.getMsg()));
			}
			return succeed;
		} catch (Exception e) {
			LogUtil.debug(e.getLocalizedMessage(), e);
			publishSendFailEvent(noticeData, phones, e);
			return false;
		}
	}

	@Override
	public String getChannelName() {
		return "netease";
	}
}
