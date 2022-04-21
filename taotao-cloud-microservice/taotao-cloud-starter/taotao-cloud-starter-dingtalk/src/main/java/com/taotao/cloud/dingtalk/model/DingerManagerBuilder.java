/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.model;

import com.taotao.cloud.dingtalk.support.CustomMessage;
import com.taotao.cloud.dingtalk.support.DingerAsyncCallback;
import com.taotao.cloud.dingtalk.support.DingerExceptionCallback;
import com.taotao.cloud.dingtalk.support.DingerHttpClient;
import com.taotao.cloud.dingtalk.support.DingerIdGenerator;
import com.taotao.cloud.dingtalk.support.DingerSignAlgorithm;
import java.util.concurrent.Executor;
import org.springframework.web.client.RestTemplate;


/**
 * DingTalk Manager Builder
 *

 * @since 1.0
 */
public class DingerManagerBuilder {

	private RestTemplate dingerRestTemplate;
	private DingerExceptionCallback dingerExceptionCallback;
	private CustomMessage textMessage;
	private CustomMessage markDownMessage;
	private DingerSignAlgorithm dingerSignAlgorithm;
	private DingerIdGenerator dingerIdGenerator;
	private Executor dingTalkExecutor;
	private DingerAsyncCallback dingerAsyncCallback;
	private DingerHttpClient dingerHttpClient;

	public DingerManagerBuilder(RestTemplate dingerRestTemplate,
		DingerExceptionCallback dingerExceptionCallback,
		CustomMessage textMessage,
		CustomMessage markDownMessage,
		DingerSignAlgorithm dingerSignAlgorithm,
		DingerIdGenerator dingerIdGenerator,
		Executor dingTalkExecutor,
		DingerAsyncCallback dingerAsyncCallback,
		DingerHttpClient dingerHttpClient) {
		this.dingerRestTemplate = dingerRestTemplate;
		this.dingerExceptionCallback = dingerExceptionCallback;
		this.textMessage = textMessage;
		this.markDownMessage = markDownMessage;
		this.dingerSignAlgorithm = dingerSignAlgorithm;
		this.dingerIdGenerator = dingerIdGenerator;
		this.dingTalkExecutor = dingTalkExecutor;
		this.dingerAsyncCallback = dingerAsyncCallback;
		this.dingerHttpClient = dingerHttpClient;
	}

	/**
	 * 自定义restTemplate客户端
	 *
	 * @param dingerRestTemplate restTemplate
	 * @return this
	 */
	public DingerManagerBuilder dingerRestTemplate(RestTemplate dingerRestTemplate) {
		if (dingerRestTemplate != null) {
			this.dingerRestTemplate = dingerRestTemplate;
		}
		return this;
	}

	/**
	 * 自定义异常回调
	 *
	 * @param dingerExceptionCallback dingerExceptionCallback
	 * @return this
	 */
	public DingerManagerBuilder dingerExceptionCallback(
		DingerExceptionCallback dingerExceptionCallback) {
		if (dingerExceptionCallback != null) {
			this.dingerExceptionCallback = dingerExceptionCallback;
		}
		return this;
	}

	/**
	 * 自定义text文本消息体-仅限手动发送方式
	 *
	 * <pre>
	 *     // 该方式为手动发送消息体方式
	 *     dingerSender.send(...);
	 *
	 *     // 该方式为统一管理消息体方式
	 *     userDinger.success(...);
	 * </pre>
	 *
	 * @param textMessage textMessage
	 * @return this
	 */
	public DingerManagerBuilder textMessage(CustomMessage textMessage) {
		if (textMessage != null) {
			this.textMessage = textMessage;
		}
		return this;
	}

	/**
	 * 自定义markdown消息体-仅限手动发送方式
	 *
	 * <pre>
	 *     // 该方式为手动发送消息体方式
	 *     dingerSender.send(...);
	 *
	 *     // 该方式为统一管理消息体方式
	 *     userDinger.success(...);
	 * </pre>
	 *
	 * @param markDownMessage markDownMessage
	 * @return this
	 */
	public DingerManagerBuilder markDownMessage(CustomMessage markDownMessage) {
		if (markDownMessage != null) {
			this.markDownMessage = markDownMessage;
		}
		return this;
	}

	/**
	 * 自定义签名算法，仅限钉钉签名算法更改情况下使用
	 *
	 * @param dingerSignAlgorithm dingerSignAlgorithm
	 * @return this
	 */
	public DingerManagerBuilder dingerSignAlgorithm(DingerSignAlgorithm dingerSignAlgorithm) {
		if (dingerSignAlgorithm != null) {
			this.dingerSignAlgorithm = dingerSignAlgorithm;
		}
		return this;
	}

	/**
	 * 自定义DingerId生成器，dingerId为每次调用返回体中的logid值
	 *
	 * @param dingerIdGenerator dingerIdGenerator
	 * @return this
	 */
	public DingerManagerBuilder dingerIdGenerator(DingerIdGenerator dingerIdGenerator) {
		if (dingerIdGenerator != null) {
			this.dingerIdGenerator = dingerIdGenerator;
		}
		return this;
	}

	/**
	 * 自定义异步执行线程池
	 *
	 * @param dingTalkExecutor dingTalkExecutor
	 * @return this
	 */
	public DingerManagerBuilder dingTalkExecutor(Executor dingTalkExecutor) {
		if (dingTalkExecutor != null) {
			this.dingTalkExecutor = dingTalkExecutor;
		}
		return this;
	}

	/**
	 * 自定义异步回调函数-用于异步发送时
	 *
	 * @param dingerAsyncCallback dingerAsyncCallback
	 * @return this
	 */
	public DingerManagerBuilder dingerAsyncCallback(DingerAsyncCallback dingerAsyncCallback) {
		if (dingerAsyncCallback != null) {
			this.dingerAsyncCallback = dingerAsyncCallback;
		}
		return this;
	}


	public RestTemplate getDingerRestTemplate() {
		return dingerRestTemplate;
	}

	public void setDingerRestTemplate(RestTemplate dingerRestTemplate) {
		this.dingerRestTemplate = dingerRestTemplate;
	}

	public DingerExceptionCallback getDingerExceptionCallback() {
		return dingerExceptionCallback;
	}

	public void setDingerExceptionCallback(
		DingerExceptionCallback dingerExceptionCallback) {
		this.dingerExceptionCallback = dingerExceptionCallback;
	}

	public CustomMessage getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(CustomMessage textMessage) {
		this.textMessage = textMessage;
	}

	public CustomMessage getMarkDownMessage() {
		return markDownMessage;
	}

	public void setMarkDownMessage(CustomMessage markDownMessage) {
		this.markDownMessage = markDownMessage;
	}

	public DingerSignAlgorithm getDingerSignAlgorithm() {
		return dingerSignAlgorithm;
	}

	public void setDingerSignAlgorithm(
		DingerSignAlgorithm dingerSignAlgorithm) {
		this.dingerSignAlgorithm = dingerSignAlgorithm;
	}

	public DingerIdGenerator getDingerIdGenerator() {
		return dingerIdGenerator;
	}

	public void setDingerIdGenerator(DingerIdGenerator dingerIdGenerator) {
		this.dingerIdGenerator = dingerIdGenerator;
	}

	public Executor getDingTalkExecutor() {
		return dingTalkExecutor;
	}

	public void setDingTalkExecutor(Executor dingTalkExecutor) {
		this.dingTalkExecutor = dingTalkExecutor;
	}

	public DingerAsyncCallback getDingerAsyncCallback() {
		return dingerAsyncCallback;
	}

	public void setDingerAsyncCallback(
		DingerAsyncCallback dingerAsyncCallback) {
		this.dingerAsyncCallback = dingerAsyncCallback;
	}

	public DingerHttpClient getDingerHttpClient() {
		return dingerHttpClient;
	}

	public void setDingerHttpClient(
		DingerHttpClient dingerHttpClient) {
		this.dingerHttpClient = dingerHttpClient;
	}
}
