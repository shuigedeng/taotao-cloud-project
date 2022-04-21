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
package com.taotao.cloud.common.http;

import com.taotao.cloud.common.utils.log.LogUtil;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.annotation.Nonnull;

/**
 * OkHttp logger, Slf4j and console log.
 *
 */
public enum HttpLogger implements HttpLoggingInterceptor.Logger {

	/**
	 * http 日志：Slf4j
	 */
	Slf4j() {
		@Override
		public void log(@Nonnull String message) {
			LogUtil.info(message);
		}
	},

	/**
	 * http 日志：Console
	 */
	Console() {
		@Override
		public void log(@Nonnull String message) {
			// 统一添加前缀，方便在茫茫日志中查看
			System.out.print("ConsoleLogger: ");
			System.out.println(message);
		}
	};

}
