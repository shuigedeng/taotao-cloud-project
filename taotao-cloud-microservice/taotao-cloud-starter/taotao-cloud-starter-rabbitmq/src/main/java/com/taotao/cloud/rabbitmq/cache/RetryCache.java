/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.rabbitmq.cache;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.rabbitmq.common.DetailResponse;
import com.taotao.cloud.rabbitmq.common.FastOcpRabbitMqConstants;
import com.taotao.cloud.rabbitmq.producer.MessageSender;
import com.taotao.cloud.rabbitmq.producer.MessageWithTime;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * retryCache的容器
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/28 17:32
 */
public class RetryCache {

	private MessageSender sender;
	private boolean stop = false;
	private final Map<Long, MessageWithTime> map = new ConcurrentSkipListMap<>();
	private final AtomicLong id = new AtomicLong();

	public void setSender(MessageSender sender) {
		this.sender = sender;
		startRetry();
	}

	public long generateId() {
		return id.incrementAndGet();
	}

	public void add(MessageWithTime messageWithTime) {
		map.putIfAbsent(messageWithTime.getId(), messageWithTime);
	}

	public void del(long id) {
		map.remove(id);
	}

	private void startRetry() {
		new Thread(() -> {
			while (!stop) {
				try {
					Thread.sleep(FastOcpRabbitMqConstants.RETRY_TIME_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				long now = System.currentTimeMillis();

				for (Map.Entry<Long, MessageWithTime> entry : map.entrySet()) {
					MessageWithTime messageWithTime = entry.getValue();

					if (null != messageWithTime) {
						if (messageWithTime.getTime() + 3 * FastOcpRabbitMqConstants.VALID_TIME
							< now) {
							LogUtil.info("send message {} failed after 3 min ", messageWithTime);
							RetryCache.this.del(entry.getKey());
						} else if (messageWithTime.getTime() + FastOcpRabbitMqConstants.VALID_TIME
							< now) {
							DetailResponse res = sender.send(messageWithTime);

							if (!res.isIfSuccess()) {
								LogUtil.info("retry send message failed {} errMsg {}", messageWithTime,
									res.getErrMsg());
							}
						}
					}
				}
			}
		}).start();
	}
}
