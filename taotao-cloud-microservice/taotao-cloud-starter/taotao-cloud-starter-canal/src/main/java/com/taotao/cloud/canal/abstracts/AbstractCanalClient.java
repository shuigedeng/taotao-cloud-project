package com.taotao.cloud.canal.abstracts;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.taotao.cloud.canal.interfaces.CanalClient;
import com.taotao.cloud.canal.interfaces.TransponderFactory;
import com.taotao.cloud.canal.transfer.DefaultMessageTransponder;
import com.taotao.cloud.canal.properties.CanalProperties;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Canal 客户端抽象类
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 15:02
 * @Modified_By 阿导 2018/5/28 15:02
 */
public abstract class AbstractCanalClient implements CanalClient {

	/**
	 * 运行状态
	 */
	private volatile boolean running;

	/**
	 * canal 配置
	 */
	private CanalProperties canalProperties;

	/**
	 * 转换工厂类
	 */
	protected final TransponderFactory factory;

	/**
	 * 构造方法，初始化 canal 的配置以及转换信息的工厂实例
	 *
	 * @param canalProperties
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:04
	 * @CopyRight 万物皆导
	 */
	protected AbstractCanalClient(CanalProperties canalProperties) {
		//参数校验
		Objects.requireNonNull(canalProperties, "canalConfig 不能为空!");
		Objects.requireNonNull(canalProperties, "transponderFactory 不能为空!");
		//初始化配置
		this.canalProperties = canalProperties;
		this.factory = (connector, config,listeners, annoListeners) -> new DefaultMessageTransponder(connector, config, listeners, annoListeners);
		;
	}

	/**
	 * 别拦我，我想开启 canal 客户端
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:05
	 * @CopyRight 万物皆导
	 */
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
	 * @param connector
	 * @param config
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:06
	 * @CopyRight 万物皆导
	 */
	protected abstract void process(CanalConnector connector, Map.Entry<String, CanalProperties.Instance> config);

	/**
	 * 处理 canal 连接实例
	 *
	 * @param instanceEntry
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:11
	 * @CopyRight 万物皆导
	 */
	private CanalConnector processInstanceEntry(Map.Entry<String, CanalProperties.Instance> instanceEntry) {
		//获取配置
		CanalProperties.Instance instance = instanceEntry.getValue();
		//声明连接
		CanalConnector connector;
		//是否是集群模式
		if (instance.isClusterEnabled()) {

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
			connector = CanalConnectors.newClusterConnector(addresses, instanceEntry.getKey(), instance.getUserName(), instance.getPassword());
		} else {
			//若不是集群的话，使用 newSingleConnector 初始化
			connector = CanalConnectors.newSingleConnector(new InetSocketAddress(instance.getHost(), instance.getPort()), instanceEntry.getKey(), instance.getUserName(), instance.getPassword());
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
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:09
	 * @CopyRight 万物皆导
	 */
	protected Map<String, CanalProperties.Instance> getConfig() {
		//canal 配置
		CanalProperties config = canalProperties;
		Map<String, CanalProperties.Instance> instanceMap;
		if (config != null && (instanceMap = config.getInstances()) != null && !instanceMap.isEmpty()) {
			//返回配置实例
			return config.getInstances();
		} else {
			throw new CanalClientException("无法解析 canal 的连接信息，请联系开发人员!");
		}
	}

	/**
	 * 停止 canal 客户端
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:08
	 * @CopyRight 万物皆导
	 */
	@Override
	public void stop() {
		setRunning(false);
	}

	/**
	 * 返回 canal 客户端的状态
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:08
	 * @CopyRight 万物皆导
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/**
	 * 设置 canal 客户端状态
	 *
	 * @param running
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 15:07
	 * @CopyRight 万物皆导
	 */
	private void setRunning(boolean running) {
		this.running = running;
	}
}
