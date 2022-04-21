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
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.taotao.cloud.canal.interfaces.CanalClient;
import com.taotao.cloud.canal.interfaces.TransponderFactory;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.canal.transfer.DefaultMessageTransponder;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

/**
 * Canal 客户端抽象类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:44:30
 */
public abstract class AbstractCanalClient implements CanalClient {

	/**
	 * 运行状态
	 */
	private volatile boolean running;

	/**
	 * canal 配置
	 */
	private final CanalProperties canalProperties;

	/**
	 * 转换工厂类
	 */
	protected final TransponderFactory factory;

	protected AbstractCanalClient(CanalProperties canalProperties) {
		//参数校验
		Objects.requireNonNull(canalProperties, "canalConfig 不能为空!");
		Objects.requireNonNull(canalProperties, "transponderFactory 不能为空!");
		//初始化配置
		this.canalProperties = canalProperties;
		this.factory = DefaultMessageTransponder::new;
	}

	@Override
	public void start() {
		//可能有多个客户端
		Map<String, CanalProperties.Instance> instanceMap = getConfig();
		//遍历开启进程
		for (Map.Entry<String, CanalProperties.Instance> instanceEntry : instanceMap.entrySet()) {
			process(processInstanceEntry(instanceEntry), instanceEntry);
		}
	}

	/**
	 * 初始化 canal 连接
	 *
	 * @param connector connector
	 * @param config    config
	 * @since 2021-09-03 20:44:39
	 */
	protected abstract void process(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config);

	/**
	 * 处理 canal 连接实例
	 *
	 * @param instanceEntry instanceEntry
	 * @return {@link com.alibaba.otter.canal.client.CanalConnector }
	 * @since 2021-09-03 20:44:46
	 */
	private CanalConnector processInstanceEntry(
		Map.Entry<String, CanalProperties.Instance> instanceEntry) {
		//获取配置
		CanalProperties.Instance instance = instanceEntry.getValue();

		//声明连接
		CanalConnector connector;

		//是否是集群模式
		if (instance.getClusterEnabled()) {
			//zookeeper 连接集合
			List<SocketAddress> addresses = new ArrayList<>();
			for (String s : instance.getZookeeperAddress()) {
				String[] entry = s.split(":");
				if (entry.length != 2) {
					throw new CanalClientException("zookeeper 地址格式不正确，应该为 ip:port....:" + s);
				}
				//若符合设定规则，先加入集合
				addresses.add(new InetSocketAddress(entry[0], Integer.parseInt(entry[1])));
			}

			//若集群的话，使用 newClusterConnector 方法初始化
			connector = CanalConnectors.newClusterConnector(addresses, instanceEntry.getKey(),
				instance.getUserName(), instance.getPassword());
		} else {
			//若不是集群的话，使用 newSingleConnector 初始化
			connector = CanalConnectors.newSingleConnector(
				new InetSocketAddress(instance.getHost(), instance.getPort()),
				instanceEntry.getKey(), instance.getUserName(), instance.getPassword());
		}

		//canal 连接
		connector.connect();
		if (!StringUtils.isEmpty(instance.getFilter())) {
			//canal 连接订阅，包含过滤规则
			connector.subscribe(instance.getFilter());
		} else {
			//canal 连接订阅，无过滤规则
			connector.subscribe();
		}

		//canal 连接反转
		connector.rollback();

		//返回 canal 连接
		return connector;
	}


	/**
	 * 获取 canal 配置
	 *
	 * @return {@link java.util.Map }
	 * @since 2021-09-03 20:45:01
	 */
	protected Map<String, CanalProperties.Instance> getConfig() {
		//canal 配置
		CanalProperties config = canalProperties;
		Map<String, CanalProperties.Instance> instanceMap;
		if ((instanceMap = config.getInstances()) != null && !instanceMap.isEmpty()) {
			//返回配置实例
			return config.getInstances();
		} else {
			throw new CanalClientException("无法解析 canal 的连接信息，请联系开发人员!");
		}
	}

	@Override
	public void stop() {
		setRunning(false);
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	/**
	 * 设置 canal 客户端状态
	 *
	 * @param running running
	 * @since 2021-09-03 20:45:13
	 */
	private void setRunning(boolean running) {
		this.running = running;
	}
}
