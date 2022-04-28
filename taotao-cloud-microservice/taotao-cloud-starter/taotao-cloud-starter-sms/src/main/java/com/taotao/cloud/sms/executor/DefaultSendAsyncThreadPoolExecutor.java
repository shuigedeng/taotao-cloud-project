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


import com.taotao.cloud.sms.properties.SmsAsyncProperties;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认发送异步处理线程池实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:48:01
 */
public class DefaultSendAsyncThreadPoolExecutor extends AbstractSendAsyncThreadPoolExecutor {

	private final ThreadPoolExecutor executor;

	public DefaultSendAsyncThreadPoolExecutor(SmsAsyncProperties properties) {
		super(properties);

		executor = new ThreadPoolExecutor(properties.getCorePoolSize(),
			properties.getMaximumPoolSize(),
			properties.getKeepAliveTime(),
			properties.getUnit(),
			new LinkedBlockingQueue<>(properties.getQueueCapacity()),
			new DefaultThreadFactory(),
			buildRejectedExecutionHandler(properties.getRejectPolicy()));
	}

	@Override
	protected void submit0(Runnable command) {
		executor.execute(command);
	}
}
