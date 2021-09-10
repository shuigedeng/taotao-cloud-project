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
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;

/**
 * DingdingWarn
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:42:43
 */
public class DingdingWarn extends AbstractWarn {

	private final WarnProperties warnProperties;

	public DingdingWarn(WarnProperties warnProperties) {
		this.warnProperties = warnProperties;
	}

	@Override
	public void notify(Message message) {
		DingerRobot dingerRobot = ContextUtil.getBean(DingerRobot.class, true);
		if (dingerRobot != null) {
			String ip = RequestUtil.getIpAddress();
			String dingDingFilterIP = warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String title =
					"[" + message.getWarnType().getDescription() + "]" + StringUtil.subString3(
						message.getTitle(), 100);

				String context = StringUtil.subString3(message.getTitle(), 100) + "\n" +
					"详情: " + RequestUtil.getBaseUrl() + "/health/\n" +
					StringUtil.subString3(message.getContent(), 500);

				MessageSubType messageSubType = MessageSubType.TEXT;
				DingerRequest request = DingerRequest.request(context, title);

				dingerRobot.send(messageSubType, request);
			}
		}
	}
}
