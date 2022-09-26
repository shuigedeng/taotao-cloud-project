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
package com.taotao.cloud.sms.tencent;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sms.common.exception.SendFailedException;
import com.taotao.cloud.sms.common.handler.AbstractSendHandler;
import com.taotao.cloud.sms.common.model.NoticeData;
import com.taotao.cloud.sms.common.utils.StringUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 腾讯云发送处理
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:52
 */
public class TencentSendHandler extends AbstractSendHandler<TencentProperties> {

	private static final String DEFAULT_NATION_CODE = "86";

	private final SmsMultiSender sender;

	public TencentSendHandler(TencentProperties properties,
							  ApplicationEventPublisher eventPublisher) {
		super(properties, eventPublisher);
		sender = new SmsMultiSender(properties.getAppId(), properties.getAppkey());
	}

	@Override
	public String getChannelName() {
		return "qCloud";
	}

	@Override
	public boolean send(NoticeData noticeData, Collection<String> phones) {
		String type = noticeData.getType();

		Integer templateId = properties.getTemplates(type);

		if (templateId == null) {
			LogUtils.debug("templateId invalid");
			publishSendFailEvent(noticeData, phones, new SendFailedException("templateId invalid"), null);
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

		Map<String, ArrayList<String>> phoneMap = new HashMap<>(phones.size());

		for (String phone : phones) {
			if (StringUtils.isBlank(phone)) {
				continue;
			}
			if (phone.startsWith("+")) {
				String[] values = phone.split(" ");

				if (values.length == 1) {
					getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
				} else {
					String nationCode = values[0].replace("+", "");
					String phoneNumber = StringUtils.join(values, "", 1, values.length);

					getList(phoneMap, nationCode).add(phoneNumber);
				}

			} else {
				getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
			}
		}

		return phoneMap.entrySet().parallelStream()
			.allMatch(
				entry -> send0(noticeData, templateId, params, entry.getKey(), entry.getValue()));
	}

	private Collection<String> getList(Map<String, ArrayList<String>> phoneMap, String nationCode) {
		return phoneMap.computeIfAbsent(nationCode, k -> new ArrayList<>());
	}

	private boolean send0(NoticeData noticeData, int templateId, ArrayList<String> params,
						  String nationCode,
						  ArrayList<String> phones) {
		SmsMultiSenderResult result = null;
		try {
			result = sender
				.sendWithParam(nationCode, phones, templateId, params, properties.getSmsSign(), "",
					"");

			if (result.result == 0) {
				publishSendSuccessEvent(noticeData, phones, result);
				return true;
			}

			LogUtils.debug("send fail[code={}, errMsg={}]", result.result, result.errMsg);
			publishSendFailEvent(noticeData, phones, new SendFailedException(result.errMsg), result);
		} catch (Exception e) {
			LogUtils.debug(e.getMessage(), e);
			publishSendFailEvent(noticeData, phones, e, result);
		}

		return false;
	}
}
