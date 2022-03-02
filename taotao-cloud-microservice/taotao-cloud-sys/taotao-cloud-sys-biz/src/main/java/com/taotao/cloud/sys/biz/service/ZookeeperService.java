package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.dtos.UpdateConnectEvent;
import com.taotao.cloud.sys.biz.tools.core.dtos.param.ConnectParam;
import com.taotao.cloud.sys.biz.tools.core.dtos.param.SimpleConnectParam;
import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.tools.core.service.connect.events.DeleteSecurityConnectEvent;
import com.taotao.cloud.sys.biz.tools.serializer.SerializerConstants;
import com.taotao.cloud.sys.biz.tools.serializer.service.Serializer;
import com.taotao.cloud.sys.biz.tools.serializer.service.SerializerChoseService;
import com.taotao.cloud.sys.api.dto.zookeeper.ZooNodeACL;
import com.taotao.cloud.sys.biz.tools.zookeeper.service.ZkSerializerAdapter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;


@Service
public class ZookeeperService implements ApplicationListener {

	// connName ==> ZkClient
	Map<String, ZkClient> zkClientMap = new ConcurrentHashMap<>();

	@Autowired
	private ConnectService connectService;
	@Autowired
	private SerializerChoseService serializerChoseService;

	public static final String module = "zookeeper";

	/**
	 * 检查路径是否存在
	 *
	 * @param connName
	 * @param path
	 */
	public boolean exists(String connName, String path) throws IOException {
		ZkClient zkClient = zkClient(connName);
		path = resolvePath(path);
		boolean exists = zkClient.exists(path);
		return exists;
	}

	/**
	 * 列出直接子节点
	 */
	public List<String> childrens(String connName, String path) throws IOException {
		ZkClient zkClient = zkClient(connName);
		path = resolvePath(path);
		return zkClient.getChildren(path);
	}

	public int countChildren(String connName, String path) throws IOException {
		ZkClient zkClient = zkClient(connName);
		return zkClient.countChildren(path);
	}

	/**
	 * 获取节点元数据
	 *
	 * @param connName
	 * @param path
	 */
	public Stat meta(String connName, String path) throws IOException {
		path = resolvePath(path);
		ZkClient zkClient = zkClient(connName);
		Map.Entry<List<ACL>, Stat> acl = zkClient.getAcl(path);
		return acl.getValue();
	}

	/**
	 * 获取 acl 权限列表
	 *
	 * @param connName
	 * @param path
	 */
	public List<ZooNodeACL> acls(String connName, String path) throws IOException {
		path = resolvePath(path);

		ZkClient zkClient = zkClient(connName);
		Map.Entry<List<ACL>, Stat> entry = zkClient.getAcl(path);
		List<ACL> acls = entry.getKey();

		List<ZooNodeACL> zooNodeACLS = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(acls)) {
			for (ACL acl : acls) {
				Id id = acl.getId();
				ZooNodeACL zooNodeACL = new ZooNodeACL(id.getScheme(), id.getId(), acl.getPerms());
				zooNodeACLS.add(zooNodeACL);
			}
		}
		return zooNodeACLS;
	}

	// 默认序列化工具
	private static final ZkSerializer DEFAULT_ZKSERIALIZER = new BytesPushThroughSerializer();

	/**
	 * 获取一个客户端
	 */
	ZkClient zkClient(String connName) throws IOException {
		ZkClient zkClient = zkClientMap.get(connName);
		if (zkClient == null) {
			SimpleConnectParam zookeeperConnectParam = (SimpleConnectParam) connectService.readConnParams(
				module, connName);
			ConnectParam connectParam = zookeeperConnectParam.getConnectParam();
			int connectionTimeout = connectParam.getConnectionTimeout();
			int sessionTimeout = connectParam.getSessionTimeout();
			zkClient = new ZkClient(connectParam.getConnectString(), sessionTimeout,
				connectionTimeout, DEFAULT_ZKSERIALIZER);
			zkClientMap.put(connName, zkClient);
		}
		return zkClient;
	}

	/**
	 * 删除节点
	 *
	 * @param connName
	 * @param path
	 */
	public void deleteNode(String connName, String path) throws IOException {
		path = resolvePath(path);

		ZkClient zkClient = zkClient(connName);
		zkClient.deleteRecursive(path);
	}

	/**
	 * 读取数据
	 *
	 * @param connName
	 * @param path
	 * @param serializer 序列化工具
	 */
	public Object readData(String connName, String path, String serializer) throws IOException {
		path = resolvePath(path);

		ZkClient zkClient = zkClient(connName);
		Object data = zkClient.readData(path, true);
		if (data == null) {
			return "";
		}
		byte[] dataBytes = (byte[]) data;
		Serializer serializerChose = serializerChoseService.choseSerializer(serializer);
		if (serializerChose == null) {
			// 如果找不到序列化工具,选择 string 序列化
			serializerChose = serializerChoseService.choseSerializer(SerializerConstants.STRING);
		}
		ZkSerializerAdapter zkSerializerAdapter = new ZkSerializerAdapter(serializerChose);
		return zkSerializerAdapter.deserialize(dataBytes);
	}

	/**
	 * 写入字符串格式数据
	 *
	 * @param connName
	 * @param path
	 * @param data
	 */
	public void writeData(String connName, String path, String data) throws IOException {
		path = resolvePath(path);

		ZkClient zkClient = zkClient(connName);
		zkClient.writeData(path, data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 重置一些可能路径不规范的 path
	 *
	 * @param path
	 */
	private String resolvePath(String path) {
		if (StringUtils.isBlank(path)) {
			path = "/";
		} else if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (path.startsWith("//")) {
			path = path.substring(1);
		}
		return org.springframework.util.StringUtils.cleanPath(path);
	}

	@PreDestroy
	public void destory() {
		LogUtil.info("清除 {} 客户端列表:{}", module, zkClientMap.keySet());
		for (ZkClient next : zkClientMap.values()) {
			try {
				next.close();
			} catch (Exception ignored) {
			}
		}
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof UpdateConnectEvent) {
			UpdateConnectEvent.ConnectInfo connectInfo = (UpdateConnectEvent.ConnectInfo) event.getSource();
			if (connectInfo.getClazz() == SimpleConnectParam.class && module.equals(
				connectInfo.getModule())) {
				String connName = connectInfo.getConnName();
				zkClientMap.remove(connName);
				LogUtil.info("[{}]模块[{}]配置变更,将移除存储的元数据信息", module, connName);
			}
		} else if (event instanceof DeleteSecurityConnectEvent) {
			final ConnectOutput connectOutput = (ConnectOutput) event.getSource();
			if (ZookeeperService.module.equals(connectOutput.getConnectInput().getModule())) {
				final String baseName = connectOutput.getConnectInput().getBaseName();
				LogUtil.info("zookeeper 删除连接[{}]", baseName);
				zkClientMap.remove(baseName);
			}
		}
	}
}
