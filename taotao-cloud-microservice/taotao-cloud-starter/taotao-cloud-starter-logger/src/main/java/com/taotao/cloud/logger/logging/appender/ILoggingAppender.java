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

package com.taotao.cloud.logger.logging.appender;

import ch.qos.logback.classic.LoggerContext;

/**
 * logging Appender 抽象
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:58
 */
public interface ILoggingAppender {

	/**
	 * 启动
	 *
	 * @param context LoggerContext
	 */
	void start(LoggerContext context);

	/**
	 * 重置
	 *
	 * @param context LoggerContext
	 */
	void reset(LoggerContext context);

}
