/*
 * Copyright 2018-2022 the original author or authors.
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
package com.taotao.cloud.sms.channel.huaweicloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.exception.SendClientException;
import com.taotao.cloud.sms.exception.SendFailedException;
import com.taotao.cloud.sms.handler.AbstractSendHandler;
import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.utils.StringUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 华为云发送处理
 *
 * @author shuigedeng
 */
public class HuaWeiCloudSendHandler extends AbstractSendHandler<HuaWeiCloudProperties> {

	/**
	 * 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
	 */
	private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

	/**
	 * 无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
	 */
	private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

	private static final String DEFAULT_NATION_CODE = "+86";

	private final ObjectMapper objectMapper;

	private final RestTemplate restTemplate;

	public HuaWeiCloudSendHandler(HuaWeiCloudProperties properties,
		ApplicationEventPublisher eventPublisher,
		ObjectMapper objectMapper, RestTemplate restTemplate) {
		super(properties, eventPublisher);
		this.objectMapper = objectMapper;
		this.restTemplate = restTemplate;
	}

	/**
	 * 构造模板参数
	 *
	 * @param params 参数列表
	 * @return 模板参数
	 */
	private static String buildTemplateParas(Collection<String> params) {
		if (params == null || params.isEmpty()) {
			return null;
		}

		boolean firstParam = true;
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (String param : params) {
			if (!firstParam) {
				builder.append(",");
			}
			builder.append("\"");
			builder.append(param);
			builder.append("\"");
			firstParam = false;
		}
		builder.append("]");

		return builder.toString();
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String temp;
		for (byte aByte : bytes) {
			temp = Integer.toHexString(aByte & 0xFF);
			if (temp.length() == 1) {
				sb.append("0");
			}
			sb.append(temp);
		}
		return sb.toString();
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

		StringBuilder receiverBuilder = new StringBuilder();
		for (String phone : phones) {
			if (StringUtils.isBlank(phone)) {
				continue;
			}
			if (!phone.startsWith("+")) {
				phone = DEFAULT_NATION_CODE + phone;
			}
			receiverBuilder.append(phone);
			receiverBuilder.append(",");
		}

		String receiver = receiverBuilder.substring(0, receiverBuilder.length() - 1);
		String templateParas = buildTemplateParas(params);
		String wsseHeader = buildWsseHeader();
		MultiValueMap<String, String> body = buildRequestBody(receiver, templateId, templateParas);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE);
		headers.set("X-WSSE", wsseHeader);

		try {
			ResponseEntity<String> httpResponse = restTemplate.exchange(properties.getUri(),
				HttpMethod.POST, new HttpEntity<>(body, headers), String.class);

			if (httpResponse.getBody() == null) {
				LogUtil.debug("response body ie null");
				publishSendFailEvent(noticeData, phones,
					new SendFailedException("response body ie null"));
				return false;
			}

			String responseContent = httpResponse.getBody();

			LogUtil.debug("responseContent: {}", responseContent);

			HuaWeiCloudResult result = objectMapper.readValue(responseContent,
				HuaWeiCloudResult.class);

			boolean succeed = HuaWeiCloudResult.SUCCESS_CODE.equals(result.getCode());
			if (succeed) {
				publishSendSuccessEvent(noticeData, phones);
			} else {
				publishSendFailEvent(noticeData, phones,
					new SendFailedException(result.getDescription()));
			}
			return succeed;
		} catch (Exception e) {
			LogUtil.debug(e.getLocalizedMessage(), e);
			publishSendFailEvent(noticeData, phones, e);
			return false;
		}
	}

	private MultiValueMap<String, String> buildRequestBody(String receiver, String templateId,
		String templateParas) {
		if (StringUtils.isAnyBlank(receiver, templateId)) {
			throw new SendFailedException("buildRequestBody(): receiver or templateId is null.");
		}

		String signature = StringUtils.trimToNull(properties.getSignature());

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("from", properties.getSender());
		body.add("to", receiver);
		body.add("templateId", templateId);
		if (templateParas != null) {
			body.add("templateParas", templateParas);
		}
		if (signature != null) {
			body.add("signature", signature);
		}

		return body;
	}

	/**
	 * 构造X-WSSE参数值
	 *
	 * @return X-WSSE参数值
	 */
	private String buildWsseHeader() {
		String appKey = properties.getAppKey();
		String appSecret = properties.getAppSecret();
		if (StringUtils.isAnyBlank(appKey, appSecret)) {
			throw new SendClientException("buildWsseHeader(): appKey or appSecret is null.");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String time = sdf.format(new Date());
		String nonce = UUID.randomUUID().toString().replace("-", "");

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			String str = nonce + time + appSecret;
			digest.update(str.getBytes(StandardCharsets.UTF_8));
			String hexDigest = byte2Hex(digest.digest());

			String passwordDigestBase64Str = Base64.getEncoder()
				.encodeToString(hexDigest.getBytes());

			return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
		} catch (Exception e) {
			throw new SendClientException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String getChannelName() {
		return "huaweiCloud";
	}
}
