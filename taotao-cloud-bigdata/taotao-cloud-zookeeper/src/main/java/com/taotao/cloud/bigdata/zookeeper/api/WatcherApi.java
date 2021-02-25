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
package com.taotao.cloud.bigdata.zookeeper.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dengtao
 * @since 2020/12/3 下午6:26
 * @version 1.0.0
 */
public class WatcherApi implements Watcher {

	private static final Logger logger = LoggerFactory.getLogger(WatcherApi.class);

	@Override
	public void process(WatchedEvent event) {
		logger.info("【Watcher监听事件】={}", event.getState());
		logger.info("【监听路径为】={}", event.getPath());
		logger.info("【监听的类型为】={}", event.getType()); //  三种监听类型： 创建，删除，更新
	}
}
