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
package com.taotao.cloud.dingtalk.configuration;

import static com.taotao.cloud.dingtalk.constant.DingerConstant.MARKDOWN_MESSAGE;
import static com.taotao.cloud.dingtalk.constant.DingerConstant.TEXT_MESSAGE;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.dingtalk.multi.MultiDingerAlgorithmInjectRegister;
import com.taotao.cloud.dingtalk.properties.DingerProperties;
import com.taotao.cloud.dingtalk.support.CustomMessage;
import com.taotao.cloud.dingtalk.support.DefaultDingerAsyncCallable;
import com.taotao.cloud.dingtalk.support.DefaultDingerExceptionCallback;
import com.taotao.cloud.dingtalk.support.DefaultDingerIdGenerator;
import com.taotao.cloud.dingtalk.support.DingerAsyncCallback;
import com.taotao.cloud.dingtalk.support.DingerExceptionCallback;
import com.taotao.cloud.dingtalk.support.DingerIdGenerator;
import com.taotao.cloud.dingtalk.support.MarkDownMessage;
import com.taotao.cloud.dingtalk.support.TextMessage;
import com.taotao.cloud.dingtalk.support.DingTalkSignAlgorithm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 实例化bean配置
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = DingerProperties.PREFIX, value = "enabled", havingValue = "true")
public class BeanConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(BeanConfiguration.class, StarterName.DINGTALK_STARTER);
	}

	/**
	 * 默认Text消息格式配置
	 */
	@Bean(name = TEXT_MESSAGE)
	@ConditionalOnMissingBean(name = TEXT_MESSAGE)
	public CustomMessage textMessage() {
		return new TextMessage();
	}

	/**
	 * 默认markdown消息格式配置
	 */
	@Bean(name = MARKDOWN_MESSAGE)
	@ConditionalOnMissingBean(name = MARKDOWN_MESSAGE)
	public CustomMessage markDownMessage() {
		return new MarkDownMessage();
	}

	/**
	 * 默认签名算法
	 */
	@Bean
	public DingTalkSignAlgorithm dingerSignAlgorithm() {
		return new DingTalkSignAlgorithm();
	}

	/**
	 * 默认dkid生成配置
	 */
	@Bean
	public DingerIdGenerator dingerIdGenerator() {
		return new DefaultDingerIdGenerator();
	}

	/**
	 * 默认异步执行回调实例
	 */
	@Bean
	public DingerAsyncCallback dingerAsyncCallback() {
		return new DefaultDingerAsyncCallable();
	}

	@Bean
	public DingerExceptionCallback dingerExceptionCallback() {
		return new DefaultDingerExceptionCallback();
	}

	@Bean
	public MultiDingerAlgorithmInjectRegister multiDingerAlgorithmInjectRegister() {
		return new MultiDingerAlgorithmInjectRegister();
	}
}
