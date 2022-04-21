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
package com.taotao.cloud.health.warn;

import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.common.utils.servlet.RequestUtil;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DingdingWarn
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:42:43
 */
public class DingdingWarn extends AbstractWarn {

	private final static String CLASS = "com.taotao.cloud.dingtalk.model.DingerRobot";
	private final static String MESSAGE_SUB_TYPE = "com.taotao.cloud.dingtalk.enums.MessageSubType";
	private final static String DINGER_REQUEST = "com.taotao.cloud.dingtalk.entity.DingerRequest";

	private final boolean driverExist;

	public DingdingWarn() {
		this.driverExist = ReflectionUtil.tryClassForName(CLASS) != null;
	}

	@Override
	public void notify(Message message) {
		if (!driverExist) {
			LogUtil.error("未找到DingerRobot, 不支持钉钉预警");
			return;
		}

		WarnProperties warnProperties = ContextUtil.getBean(WarnProperties.class, true);
		Object dingerRobot = ContextUtil.getBean(ReflectionUtil.tryClassForName(CLASS), true);

		if (dingerRobot != null) {
			String ip = RequestUtil.getIpAddress();
			String dingDingFilterIP = warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String title = "["
					+ message.getWarnType().getDescription()
					+ "]"
					+ StringUtil.subString3(message.getTitle(), 100);

				String context = StringUtil.subString3(message.getTitle(), 100)
					+ "\n"
					+ "详情: "
					+ RequestUtil.getBaseUrl() + "/health/\n"
					+ StringUtil.subString3(message.getContent(), 500);

				try {
					Object messageSubType = ReflectionUtil.findEnumObjByName(
						Objects.requireNonNull(ReflectionUtil.tryClassForName(MESSAGE_SUB_TYPE)),
						"name", "TEXT");

					List<Object> requestParam = new ArrayList<>();
					requestParam.add(context);
					requestParam.add(title);
					Object request = ReflectionUtil.callMethod(
						ReflectionUtil.tryClassForName(DINGER_REQUEST), "request",
						requestParam.toArray());

					List<Object> param = new ArrayList<>();
					param.add(messageSubType);
					param.add(request);
					ReflectionUtil.callMethod(dingerRobot, "send", param.toArray());
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}
}
