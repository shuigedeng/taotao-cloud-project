/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.executor;

/**
 * 发送异步处理线程池接口
 *
 * @author shuigedeng
 */
public interface SendAsyncThreadPoolExecutor {

	/**
	 * 提交异步任务
	 *
	 * @param command 待执行任务
	 */
	void submit(Runnable command);
}
