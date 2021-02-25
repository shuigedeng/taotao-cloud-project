/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.bigdata.zookeeper.api.configuration;

import com.taotao.cloud.bigdata.zookeeper.api.properties.ZkProperties;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

/**
 * ZookeeperConfiguration
 *
 * @author dengtao
 * @date 2020/12/3 下午3:16
 * @since v1.0
 */
@EnableConfigurationProperties(ZkProperties.class)
public class ZookeeperConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConfiguration.class);

	@Bean(name = "zkClient")
	public ZooKeeper zkClient(ZkProperties zkProperties) {
		ZooKeeper zooKeeper = null;
		try {
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			//连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
			//可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
			zooKeeper = new ZooKeeper(zkProperties.getAddress(), zkProperties.getTimeout(), event -> {
				if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
					//如果收到了服务端的响应事件,连接成功
					countDownLatch.countDown();
				}
			});

			countDownLatch.await();
			LOGGER.info("[初始化ZooKeeper连接状态....]={}", zooKeeper.getState());
		} catch (Exception e) {
			LOGGER.error("[初始化ZooKeeper连接异常....]={}", e);
		}
		return zooKeeper;
	}
}
