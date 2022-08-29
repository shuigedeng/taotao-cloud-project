/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.monitor.warn;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.reflect.ReflectionUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.monitor.model.Message;
import com.taotao.cloud.monitor.properties.WarnProperties;

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
		this.driverExist = ReflectionUtils.tryClassForName(CLASS) != null;
	}

	@Override
	public void notify(Message message) {
		if (!driverExist) {
			LogUtils.error("未找到SmsService, 不支持短信预警");
			return;
		}

		WarnProperties warnProperties = ContextUtils.getBean(WarnProperties.class, true);
		Object smsService = ContextUtils.getBean(ReflectionUtils.tryClassForName(CLASS), true);

		if (Objects.nonNull(warnProperties) && Objects.nonNull(smsService)) {
			String ip = RequestUtils.getIpAddress();

			String dingDingFilterIP = warnProperties.getDingdingFilterIP();
			if (!StringUtils.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String context = StringUtils.subString3(message.getTitle(), 100)
					+ "\n"
					+ "详情: "
					+ RequestUtils.getBaseUrl()
					+ "/taotao/cloud/health/\n"
					+ StringUtils.subString3(message.getContent(), 500);

				try {
					List<Object> param = new ArrayList<>();
					param.add("phoneNumber");
					param.add("signName");
					param.add("templateCode");
					// templateParam
					param.add(context);
					ReflectionUtils.callMethod(smsService, "sendSms", param.toArray());
				} catch (Exception e) {
					LogUtils.error(e);
				}
			}
		}
	}


}
