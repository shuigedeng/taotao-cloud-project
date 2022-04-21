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
package com.taotao.cloud.canal.abstracts;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import com.taotao.cloud.canal.interfaces.MessageTransponder;
import com.taotao.cloud.canal.model.ListenerPoint;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AbstractMessageTransponder
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:46:33
 */
public abstract class AbstractMessageTransponder implements MessageTransponder {

	/**
	 * canal 连接器
	 */
	private final CanalConnector connector;

	/**
	 * custom 连接配置
	 */
	protected final CanalProperties.Instance config;

	/**
	 * canal 服务指令
	 */
	protected final String destination;

	/**
	 * 实现接口的 canal 监听器(上：表内容，下：表结构)
	 */
	protected final List<CanalEventListener> implListeners = new ArrayList<>();

	/**
	 * 通过注解方式的 canal 监听器
	 */
	protected final List<ListenerPoint> annotationListeners = new ArrayList<>();

	/**
	 * canal 客户端的运行状态
	 */
	private volatile boolean running = true;

	public AbstractMessageTransponder(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config,
		List<CanalEventListener> implListeners,
		List<ListenerPoint> annotationListeners) {
		//参数处理
		Objects.requireNonNull(connector, "连接器不能为空!");
		Objects.requireNonNull(config, "配置信息不能为空!");

		//参数初始化
		this.connector = connector;
		this.destination = config.getKey();
		this.config = config.getValue();
		if (implListeners != null) {
			this.implListeners.addAll(implListeners);
		}

		if (annotationListeners != null) {
			this.annotationListeners.addAll(annotationListeners);
		}
	}


	@Override
	public void run() {
		//错误重试次数
		int errorCount = config.getRetryCount();
		//捕获信息的心跳时间
		final long interval = config.getAcquireInterval();
		//当前线程的名字
		final String threadName = Thread.currentThread().getName();
		//若线程正在进行
		while (running && !Thread.currentThread().isInterrupted()) {
			try {
				//获取消息
				Message message = connector.getWithoutAck(config.getBatchSize());
				//获取消息 ID
				long batchId = message.getId();
				//消息数
				int size = message.getEntries().size();
				//debug 模式打印消息数
				LogUtil.debug("{}: 从 canal 服务器获取消息： >>>>> 数:{}", threadName, size);

				//若是没有消息
				if (batchId == -1 || size == 0) {
					LogUtil.debug("{}: 没有任何消息啊，我休息{}毫秒", threadName, interval);
					//休息
					Thread.sleep(interval);
				} else {
					//处理消息
					distributeEvent(message);
				}

				//确认消息已被处理完
				connector.ack(batchId);

				//若是 debug模式
				LogUtil.debug("{}: 确认消息已被消费，消息ID:{}", threadName, batchId);
			} catch (CanalClientException e) {
				//每次错误，重试次数减一处理
				errorCount--;
				LogUtil.error(threadName + ": 发生错误!! ", e);
				try {
					//等待时间
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					errorCount = 0;
				}
			} catch (InterruptedException e) {
				//线程中止处理
				errorCount = 0;
				connector.rollback();
			} finally {
				//若错误次数小于 0
				if (errorCount <= 0) {
					//停止 canal 客户端
					stop();
					LogUtil.info("{}: canal 客户端已停止... ", Thread.currentThread().getName());
				}
			}
		}

		//停止 canal 客户端
		stop();
		LogUtil.info("{}: canal 客户端已停止. ", Thread.currentThread().getName());
	}

	/**
	 * 处理监听的事件
	 *
	 * @param message message
	 * @since 2021-09-03 20:46:45
	 */
	protected abstract void distributeEvent(Message message);

	/**
	 * 停止 canal 客户端
	 *
	 * @since 2021-09-03 20:46:52
	 */
	void stop() {
		running = false;
	}

}
