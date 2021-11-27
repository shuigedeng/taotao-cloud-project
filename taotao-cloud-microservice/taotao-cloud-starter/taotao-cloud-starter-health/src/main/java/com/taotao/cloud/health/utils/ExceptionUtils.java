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
package com.taotao.cloud.health.utils;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.core.enums.ExceptionTypeEnum;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClient;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.enums.WarnLevelEnum;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

/**
 * ExceptionUtils
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:35:39
 */
public class ExceptionUtils {

	private final static String exceptionUrl = "taotao.cloud.health.report.exception.url";

	/**
	 * 上报异常
	 *
	 * @param message         message
	 * @param applicationName applictionName
	 * @author shuigedeng
	 * @since 2021-09-10 16:35:51
	 */
	public static void reportException(Message message, String applicationName) {
		if (message.getWarnType() == WarnTypeEnum.ERROR) {
			AtomicReference<String> title = new AtomicReference<>(message.getTitle());
			MonitorThreadPool monitorThreadPool = ContextUtil.getBean(MonitorThreadPool.class,
				false);

			if (Objects.nonNull(monitorThreadPool)) {
				monitorThreadPool.monitorSubmit("系统任务: reportException 异常上报", () -> {
					Map<String, Object> param = new HashMap<>();
					param.put("exceptionTitle", title.get());
					param.put("exceptionType", message.getExceptionType().getCode());
					param.put("exceptionLevel", message.getLevelType().getLevel());

					if (StringUtils.isNotBlank(message.getExceptionCode())) {
						param.put("exceptionCode", message.getExceptionCode());
					}
					if (StringUtils.isNotBlank(message.getBizScope())) {
						param.put("bizScope", message.getBizScope());
					}

					param.put("exceptionContent", String.format("[%s][%s][%s]%s",
						RequestUtil.getIpAddress(),
						PropertyUtil.getProperty(CoreProperties.SpringApplicationName),
						PropertyUtil.getProperty(CoreProperties.SpringProfilesActive),
						message.getContent()));

					if (StringUtils.isNotBlank(applicationName)) {
						param.put("applicationName", applicationName);
					} else {
						param.put("applicationName",
							PropertyUtil.getProperty(CoreProperties.SpringApplicationName));
					}

					HttpClient.Params params = HttpClient
						.Params
						.custom()
						.setContentType(ContentType.APPLICATION_JSON)
						.add(param)
						.build();

					DefaultHttpClient defaultHttpClient = ContextUtil.getBean(
						DefaultHttpClient.class, false);
					if (Objects.nonNull(defaultHttpClient)) {
						defaultHttpClient.post(
							PropertyUtil.getPropertyCache(exceptionUrl, StringUtils.EMPTY), params);
					}
				});
			}
		}
	}

	/**
	 * 上报异常
	 *
	 * @param message message
	 * @author shuigedeng
	 * @since 2021-09-10 16:36:04
	 */
	public static void reportException(Message message) {
		reportException(message, null);
	}

	/**
	 * 上报异常
	 *
	 * @param warnLevelEnum warnLevelEnum
	 * @param title         title
	 * @param content       content
	 * @author shuigedeng
	 * @since 2021-09-10 16:36:09
	 */
	public static void reportException(WarnLevelEnum warnLevelEnum, String title,
		String content) {
		reportException(new Message(
				WarnTypeEnum.ERROR,
				title,
				content,
				warnLevelEnum,
				ExceptionTypeEnum.BE,
				null,
				null),
			null);
	}

	/**
	 * 上报异常
	 *
	 * @param warnLevelEnumType levelEnumType
	 * @param title             title
	 * @param content           content
	 * @param applicationName   applicationName
	 * @author shuigedeng
	 * @since 2021-09-10 16:36:15
	 */
	public static void reportException(WarnLevelEnum warnLevelEnumType, String title,
		String content,
		String applicationName) {
		reportException(new Message(WarnTypeEnum.ERROR,
			title,
			content,
			warnLevelEnumType,
			ExceptionTypeEnum.BE,
			null,
			null), applicationName);
	}
}

