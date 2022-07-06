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
package com.taotao.cloud.dingtalk.constant;

/**
 * 常量类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:18:42
 */
public interface DingerConstant {

	/**
	 * 全垒打前缀
	 */
	String DINGER_PREFIX = "D";
	/**
	 * 默认线程名称前缀
	 * 默认线程池中线程名称前缀
	 */
	String DEFAULT_THREAD_NAME_PREFIX = "dinger-";


	/**
	 * 短信
	 * bean name
	 */
	String TEXT_MESSAGE = "textMessage";
	/**
	 * 减价消息
	 * bean name
	 */
	String MARKDOWN_MESSAGE = "markDownMessage";
	/**
	 * 全垒打执行人
	 * bean name
	 */
	String DINGER_EXECUTOR = "dingerExecutor";
	/**
	 * 全垒打其他模板
	 * 自定义restTemplate名称
	 */
	String DINGER_REST_TEMPLATE = "dingerRestTemplate";

	/**
	 * 新行
	 */
	String NEW_LINE = "\r\n";
	/**
	 * 现货分离器
	 */
	String SPOT_SEPERATOR = ".";

	/**
	 * 全垒打道具前缀
	 */
	String DINGER_PROP_PREFIX = "spring.dinger";
	/**
	 * 全垒打属性前缀
	 */
	String DINGER_PROPERTIES_PREFIX = DINGER_PROP_PREFIX + SPOT_SEPERATOR;
}
