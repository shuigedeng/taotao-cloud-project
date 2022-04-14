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
package com.taotao.cloud.sms.channel.chinamobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.exception.SendFailedException;
import com.taotao.cloud.sms.handler.AbstractSendHandler;
import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.utils.StringUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

/**
 * 移动云发送处理
 *
 * @author shuigedeng
 */
public class ChinaMobileSendHandler extends AbstractSendHandler<ChinaMobileProperties> {

	private static final String BODY_TEMPLATE = "{\"ecName\":\"%s\",\"apId\":\"%s\",\"templateId\":\"%s\",\"mobiles\":\"%s\",\"params\":\"%s\",\"sign\":\"%s\",\"addSerial\":\"\",\"mac\":\"%s\"}";

	private final ObjectMapper objectMapper;

	private final RestTemplate restTemplate;

	public ChinaMobileSendHandler(ChinaMobileProperties properties,
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
			return "[\"\"]";
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

	private static String buildMac(String ecName, String apId, String secretKey, String templateId,
		String mobiles,
		String params, String sign) {
		String origin = ecName + apId + secretKey + templateId + mobiles + params + sign;
		return DigestUtils.md5DigestAsHex(origin.getBytes(StandardCharsets.UTF_8));
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

		//StringBuilder receiverBuilder = new StringBuilder();
		//for (String phone : phones) {
		//	if (StringUtils.isBlank(phone)) {
		//		continue;
		//	}
		//	receiverBuilder.append(phone.trim());
		//	receiverBuilder.append(",");
		//}
		//String mobiles = receiverBuilder.substring(0, receiverBuilder.length() - 1);

		String mobiles = StringUtil.join(phones, ",");
		String paramsString = buildTemplateParas(params);
		String body = buildRequestBody(mobiles, templateId, paramsString);

		try {
			HttpEntity<String> httpEntity = new HttpEntity<>(body, new HttpHeaders());

			ResponseEntity<String> httpResponse = restTemplate
				.exchange(properties.getUri(), HttpMethod.POST, httpEntity, String.class);

			if (httpResponse.getBody() == null) {
				LogUtil.debug("response body ie null");
				publishSendFailEvent(noticeData, phones,
					new SendFailedException("response body ie null"));
				return false;
			}

			String responseContent = httpResponse.getBody();

			LogUtil.debug("responseContent: {}", responseContent);

			ChinaMobileResult result = objectMapper.readValue(responseContent,
				ChinaMobileResult.class);

			boolean succeed = ChinaMobileResult.SUCCESS_RSPCOD.equals(result.getRspcod());
			if (succeed) {
				publishSendSuccessEvent(noticeData, phones);
			} else {
				publishSendFailEvent(noticeData, phones,
					new SendFailedException(result.getRspcod()));
			}
			return succeed;
		} catch (Exception e) {
			LogUtil.debug(e.getLocalizedMessage(), e);
			publishSendFailEvent(noticeData, phones, e);
			return false;
		}
	}

	private String buildRequestBody(String mobiles, String templateId, String paramsString) {
		if (StringUtils.isAnyBlank(mobiles, templateId)) {
			throw new SendFailedException("buildRequestBody(): mobiles or templateId is null.");
		}

		String ecName = StringUtils.trimToNull(properties.getEcName());
		String apId = StringUtils.trimToNull(properties.getApId());
		String secretKey = StringUtils.trimToNull(properties.getSecretKey());
		String sign = StringUtils.trimToNull(properties.getSign());
		String mac = buildMac(ecName, apId, secretKey, templateId, mobiles, paramsString, sign);

		String body = String
			.format(BODY_TEMPLATE, ecName, apId, templateId, mobiles,
				paramsString.replace("\"", "\\\""), sign,
				mac);

		return new String(Base64.getEncoder().encode(body.getBytes(StandardCharsets.UTF_8)));
	}

	@Override
	public String getChannelName() {
		return "chinaMobile";
	}
}
