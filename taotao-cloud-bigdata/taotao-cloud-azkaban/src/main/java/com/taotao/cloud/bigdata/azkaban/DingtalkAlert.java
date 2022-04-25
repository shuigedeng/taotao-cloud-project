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
package com.taotao.cloud.bigdata.azkaban;

import azkaban.alert.Alerter;
import azkaban.executor.ExecutableFlow;
import azkaban.sla.SlaOption;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

/**
 * DingtalkAlert
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/21 下午2:10
 */
public class DingtalkAlert implements Alerter {

	private final Properties properties;

	public static final String DINGTALK_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

	public DingtalkAlert(Properties properties) {
		this.properties = properties;
	}

	private boolean isActivated(ExecutableFlow flow, String type) {
		Map<String, String> parameters = flow.getExecutionOptions().getFlowParameters();
		String propertyKey = "ding.alert.on" + type;
		return Boolean.parseBoolean(
			parameters.getOrDefault(parameters.get(propertyKey),
				parameters.getOrDefault(propertyKey, "false")));
	}

	private String getPropertyKey(ExecutableFlow flow, String propertyKey) {
		Map<String, String> parameters = flow.getExecutionOptions().getFlowParameters();
		return parameters.getOrDefault(propertyKey, properties.getProperty(propertyKey));
	}

	private void send(ExecutableFlow executableFlow, String title, String content) {
		String token = getPropertyKey(executableFlow, "ding.token");
		String azkabanHost = getPropertyKey(executableFlow, "ding.link.azkaban.host");
		String linkAddress =
			azkabanHost + "/executor?execid=" + executableFlow.getExecutionId() + "#joblist";

		String authWord = getPropertyKey(executableFlow, "ding.auth.word");

		DefaultDingTalkClient talkClient = new DefaultDingTalkClient(DINGTALK_SERVER_URL + token);
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("link");

		OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
		link.setMessageUrl(linkAddress);
		link.setPicUrl("");
		link.setText(authWord + ":" + content);
		link.setTitle(title);

		request.setLink(link);

		try {
			talkClient.execute(request);
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	@Override
	public void alertOnSuccess(ExecutableFlow executableFlow) throws Exception {
		if (isActivated(executableFlow, "success")) {
			String title =
				"taotao cloud weblog flow " + executableFlow.getFlowId() + "has successed:\n";
			StringBuilder sb = new StringBuilder();

			sb.append("\t-start:")
				.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(executableFlow.getStartTime()))
				.append("\n");

			sb.append("\t-end:")
				.append(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executableFlow.getEndTime()))
				.append("\n");

			sb.append("\t-duration:")
				.append((executableFlow.getEndTime() - executableFlow.getStartTime()) / 1000)
				.append("\n");

			send(executableFlow, title, sb.toString());
		}
	}

	@Override
	public void alertOnError(ExecutableFlow executableFlow, String... strings) throws Exception {
		if (isActivated(executableFlow, "error")) {
			String title =
				"taotao cloud weblog flow " + executableFlow.getFlowId() + "has failed:\n";
			StringBuilder sb = new StringBuilder();

			sb.append("\t-start:")
				.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(executableFlow.getStartTime()))
				.append("\n");

			sb.append("\t-end:")
				.append(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executableFlow.getEndTime()))
				.append("\n");

			sb.append("\t-duration:")
				.append((executableFlow.getEndTime() - executableFlow.getStartTime()) / 1000)
				.append("\n");

			send(executableFlow, title, sb.toString());
		}
	}

	@Override
	public void alertOnFirstError(ExecutableFlow executableFlow) throws Exception {
		if (isActivated(executableFlow, "first error")) {
			String title =
				"taotao cloud weblog flow " + executableFlow.getFlowId() + "has first failed:\n";
			StringBuilder sb = new StringBuilder();

			sb.append("\t-start:")
				.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(executableFlow.getStartTime()))
				.append("\n");

			sb.append("\t-end:")
				.append(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executableFlow.getEndTime()))
				.append("\n");

			sb.append("\t-duration:")
				.append((executableFlow.getEndTime() - executableFlow.getStartTime()) / 1000)
				.append("\n");

			send(executableFlow, title, sb.toString());
		}
	}

	@Override
	public void alertOnSla(SlaOption slaOption, String s) throws Exception {

	}
}
