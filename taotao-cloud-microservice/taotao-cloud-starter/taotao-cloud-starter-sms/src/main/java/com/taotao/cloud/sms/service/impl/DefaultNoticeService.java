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
package com.taotao.cloud.sms.service.impl;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.exception.NotFindSendHandlerException;
import com.taotao.cloud.sms.exception.SmsException;
import com.taotao.cloud.sms.executor.SendAsyncThreadPoolExecutor;
import com.taotao.cloud.sms.handler.SendHandler;
import com.taotao.cloud.sms.loadbalancer.ILoadBalancer;
import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.service.NoticeService;
import com.taotao.cloud.sms.properties.SmsAsyncProperties;
import com.taotao.cloud.sms.properties.SmsProperties;
import com.taotao.cloud.sms.utils.StringUtils;
import java.util.Collection;
import java.util.List;
import org.springframework.lang.Nullable;

/**
 * 短信通知服务实现
 *
 * @author shuigedeng
 */
public class DefaultNoticeService implements NoticeService {

	private final SmsProperties config;

	private final SmsAsyncProperties asyncConfig;

	private final ILoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer;

	private final SendAsyncThreadPoolExecutor executor;

	public DefaultNoticeService(SmsProperties config, SmsAsyncProperties asyncConfig,
		ILoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer,
		@Nullable SendAsyncThreadPoolExecutor executor) {
		this.config = config;
		this.asyncConfig = asyncConfig;
		this.smsSenderLoadbalancer = smsSenderLoadbalancer;
		this.executor = executor;
	}

	@Override
	public boolean phoneRegValidation(String phone) {
		return StringUtils.isNotBlank(phone) && (StringUtils.isBlank(config.getReg())
			|| phone.matches(
			config.getReg()));
	}

	@Override
	public boolean send(NoticeData noticeData, Collection<String> phones) {
		SendResult result = send0(noticeData, phones);

		if (result.exception != null) {
			throw result.exception;
		}

		return result.result;
	}

	@Override
	public void asyncSend(NoticeData noticeData, Collection<String> phones) {
		if (!asyncConfig.isEnable() || executor == null) {
			send(noticeData, phones);
			return;
		}

		executor.submit(() -> send0(noticeData, phones));
	}

	private SendResult send0(NoticeData noticeData, Collection<String> phones) {
		SendResult result = new SendResult();
		if (phones.isEmpty()) {
			LogUtil.debug("phones is empty");
			return result;
		}

		List<String> phoneList = phones.stream().filter(this::phoneRegValidation).toList();

		if (phoneList.isEmpty()) {
			LogUtil.debug("after filter phones is empty");
			return result;
		}

		SendHandler sendHandler = smsSenderLoadbalancer.choose(noticeData);

		if (sendHandler == null) {
			result.exception = new NotFindSendHandlerException();
		} else {
			try {
				result.result = sendHandler.send(noticeData, phones);
			} catch (RuntimeException e) {
				result.exception = e;
			} catch (Exception e) {
				result.exception = new SmsException(e.getLocalizedMessage(), e);
			}
		}

		if (result.exception != null) {
			LogUtil.debug(result.exception.getLocalizedMessage());
		}

		return result;
	}

	private static class SendResult {

		boolean result;

		RuntimeException exception;
	}
}
