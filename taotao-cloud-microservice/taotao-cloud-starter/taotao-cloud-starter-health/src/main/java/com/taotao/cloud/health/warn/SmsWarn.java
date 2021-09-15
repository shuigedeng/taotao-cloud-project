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
package com.taotao.cloud.health.warn;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SmsWarn
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:47:39
 */
public class SmsWarn extends AbstractWarn {

	private final static String CLASS = "com.taotao.cloud.sms.service.SmsService";

	private final boolean driverExist;

	public SmsWarn() {
		this.driverExist = ReflectionUtil.tryClassForName(CLASS) != null;
	}

	@Override
	public void notify(Message message) {
		if (!driverExist) {
			LogUtil.error("未找到SmsService, 不支持短信预警");
			return;
		}

		WarnProperties warnProperties = ContextUtil.getBean(WarnProperties.class, true);
		Object smsService = ContextUtil.getBean(ReflectionUtil.tryClassForName(CLASS), true);

		if (Objects.nonNull(warnProperties) && Objects.nonNull(smsService)) {
			String ip = RequestUtil.getIpAddress();

			String dingDingFilterIP = warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String context = StringUtil.subString3(message.getTitle(), 100)
					+ "\n"
					+ "详情: "
					+ RequestUtil.getBaseUrl()
					+ "/taotao/cloud/health/\n"
					+ StringUtil.subString3(message.getContent(), 500);

				try {
					List<Object> param = new ArrayList<>();
					param.add("phoneNumber");
					param.add("signName");
					param.add("templateCode");
					// templateParam
					param.add(context);
					ReflectionUtil.callMethod(smsService, "sendSms", param.toArray());
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}


}
