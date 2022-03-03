package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.service.IZookeeperService;
import org.springframework.stereotype.Service;

/**
 * ZookeeperServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-03 14:35:06
 */
@Service
public class ZookeeperServiceImpl implements IZookeeperService {

	//// connName ==> ZkClient
	//Map<String, ZkClient> zkClientMap = new ConcurrentHashMap<>();
	//
	//@Autowired
	//private ConnectService connectService;
	//@Autowired
	//private SerializerChoseService serializerChoseService;
	//
	//public static final String module = "zookeeper";
	//
	//// 路径收藏  connName ==> PathFavorite
	//private static final Map<String, Set<PathFavorite>> pathFavorites = new HashMap<>();
	//
	//@Autowired
	//private FileManager fileManager;
	//
	///**
	// * 添加收藏 ,前端需要把所有的收藏全拿过来,后端直接覆盖
	// */
	//public void addFavorite(String connName, PathFavorite pathFavorite) {
	//	Set<PathFavorite> pathFavorites = ZookeeperExtendService.pathFavorites.computeIfAbsent(
	//		connName, k -> new LinkedHashSet<>());
	//	pathFavorites.add(pathFavorite);
	//	serializer();
	//}
	//
	//public void removeFavorite(String connName, String name) {
	//	Set<PathFavorite> pathFavorites = ZookeeperExtendService.pathFavorites.computeIfAbsent(
	//		connName, k -> new LinkedHashSet<>());
	//	Iterator<PathFavorite> iterator = pathFavorites.iterator();
	//	while (iterator.hasNext()) {
	//		PathFavorite next = iterator.next();
	//		if (next.getName().equals(name)) {
	//			iterator.remove();
	//			break;
	//		}
	//	}
	//
	//	serializer();
	//}
	//
	///**
	// * 列出当前连接关注的路径列表
	// *
	// * @param connName
	// */
	//public Set<PathFavorite> favorites(String connName) {
	//	return ZookeeperExtendService.pathFavorites.computeIfAbsent(connName,
	//		k -> new LinkedHashSet<>());
	//}
	//
	///**
	// * 序列化收藏列表到文件
	// */
	//private void serializer() {
	//	try {
	//		fileManager.writeConfig(ZookeeperServiceImpl.module, "favorites",
	//			JSON.toJSONString(pathFavorites));
	//	} catch (IOException e) {
	//		LogUtil.error("zookeeper serializer favorites error : {}", e.getMessage(), e);
	//	}
	//}
	//
	///**
	// * 检查路径是否存在
	// *
	// * @param connName
	// * @param path
	// */
	//public boolean exists(String connName, String path) throws IOException {
	//	ZkClient zkClient = zkClient(connName);
	//	path = resolvePath(path);
	//	boolean exists = zkClient.exists(path);
	//	return exists;
	//}
	//
	///**
	// * 列出直接子节点
	// */
	//public List<String> childrens(String connName, String path) throws IOException {
	//	ZkClient zkClient = zkClient(connName);
	//	path = resolvePath(path);
	//	return zkClient.getChildren(path);
	//}
	//
	//public int countChildren(String connName, String path) throws IOException {
	//	ZkClient zkClient = zkClient(connName);
	//	return zkClient.countChildren(path);
	//}
	//
	///**
	// * 获取节点元数据
	// *
	// * @param connName
	// * @param path
	// */
	//public Stat meta(String connName, String path) throws IOException {
	//	path = resolvePath(path);
	//	ZkClient zkClient = zkClient(connName);
	//	Map.Entry<List<ACL>, Stat> acl = zkClient.getAcl(path);
	//	return acl.getValue();
	//}
	//
	///**
	// * 获取 acl 权限列表
	// *
	// * @param connName
	// * @param path
	// */
	//public List<ZooNodeACL> acls(String connName, String path) throws IOException {
	//	path = resolvePath(path);
	//
	//	ZkClient zkClient = zkClient(connName);
	//	Map.Entry<List<ACL>, Stat> entry = zkClient.getAcl(path);
	//	List<ACL> acls = entry.getKey();
	//
	//	List<ZooNodeACL> zooNodeACLS = new ArrayList<>();
	//	if (CollectionUtils.isNotEmpty(acls)) {
	//		for (ACL acl : acls) {
	//			Id id = acl.getId();
	//			ZooNodeACL zooNodeACL = new ZooNodeACL(id.getScheme(), id.getId(), acl.getPerms());
	//			zooNodeACLS.add(zooNodeACL);
	//		}
	//	}
	//	return zooNodeACLS;
	//}
	//
	//// 默认序列化工具
	//private static final ZkSerializer DEFAULT_ZKSERIALIZER = new BytesPushThroughSerializer();
	//
	///**
	// * 获取一个客户端
	// */
	//ZkClient zkClient(String connName) throws IOException {
	//	ZkClient zkClient = zkClientMap.get(connName);
	//	if (zkClient == null) {
	//		SimpleConnectParam zookeeperConnectParam = (SimpleConnectParam) connectService.readConnParams(
	//			module, connName);
	//		ConnectParam connectParam = zookeeperConnectParam.getConnectParam();
	//		int connectionTimeout = connectParam.getConnectionTimeout();
	//		int sessionTimeout = connectParam.getSessionTimeout();
	//		zkClient = new ZkClient(connectParam.getConnectString(), sessionTimeout,
	//			connectionTimeout, DEFAULT_ZKSERIALIZER);
	//		zkClientMap.put(connName, zkClient);
	//	}
	//	return zkClient;
	//}
	//
	///**
	// * 删除节点
	// *
	// * @param connName
	// * @param path
	// */
	//public void deleteNode(String connName, String path) throws IOException {
	//	path = resolvePath(path);
	//
	//	ZkClient zkClient = zkClient(connName);
	//	zkClient.deleteRecursive(path);
	//}
	//
	///**
	// * 读取数据
	// *
	// * @param connName
	// * @param path
	// * @param serializer 序列化工具
	// */
	//public Object readData(String connName, String path, String serializer) throws IOException {
	//	path = resolvePath(path);
	//
	//	ZkClient zkClient = zkClient(connName);
	//	Object data = zkClient.readData(path, true);
	//	if (data == null) {
	//		return "";
	//	}
	//	byte[] dataBytes = (byte[]) data;
	//	Serializer serializerChose = serializerChoseService.choseSerializer(serializer);
	//	if (serializerChose == null) {
	//		// 如果找不到序列化工具,选择 string 序列化
	//		serializerChose = serializerChoseService.choseSerializer(SerializerConstants.STRING);
	//	}
	//	ZkSerializerAdapter zkSerializerAdapter = new ZkSerializerAdapter(serializerChose);
	//	return zkSerializerAdapter.deserialize(dataBytes);
	//}
	//
	///**
	// * 写入字符串格式数据
	// *
	// * @param connName
	// * @param path
	// * @param data
	// */
	//public void writeData(String connName, String path, String data) throws IOException {
	//	path = resolvePath(path);
	//
	//	ZkClient zkClient = zkClient(connName);
	//	zkClient.writeData(path, data.getBytes(StandardCharsets.UTF_8));
	//}
	//
	///**
	// * 重置一些可能路径不规范的 path
	// *
	// * @param path
	// */
	//private String resolvePath(String path) {
	//	if (StringUtils.isBlank(path)) {
	//		path = "/";
	//	} else if (!path.startsWith("/")) {
	//		path = "/" + path;
	//	}
	//	if (path.startsWith("//")) {
	//		path = path.substring(1);
	//	}
	//	return org.springframework.util.StringUtils.cleanPath(path);
	//}


}
