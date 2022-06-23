package com.taotao.cloud.sys.biz.api.controller.tools.monitor;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.web.vo.redis.RedisVO;
import com.taotao.cloud.sys.biz.service.IRedisService;
import com.taotao.cloud.web.idempotent.Idempotent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:13:03
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-redis管理API", description = "工具管理端-redis管理API")
@RequestMapping("/sys/tools/monitor/redis")
public class RedisController {

	private final IRedisService redisService;

	@Operation(summary = "查询Redis缓存", description = "查询Redis缓存")
	@RequestLogger("查询Redis缓存")
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
	public Result<Page> getRedis(String key, Pageable pageable) {
		Page byKey = redisService.findByKey(key, pageable);
		return Result.success(byKey);
	}

	@Operation(summary = "删除Redis缓存", description = "删除Redis缓存")
	@RequestLogger("删除Redis缓存")
	@Idempotent(key = "delete", perFix = "redis")
	@DeleteMapping(value = "/redis")
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
	public Result<Boolean> delete(@RequestBody RedisVO resources) {
		redisService.delete(resources.getKey());
		return Result.success(true);
	}

	@Operation(summary = "清空Redis缓存", description = "清空Redis缓存")
	@RequestLogger("清空Redis缓存")
	@Idempotent(key = "deleteAll", perFix = "redis")
	@DeleteMapping(value = "/redis/all")
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
	public Result<Boolean> deleteAll() {
		redisService.flushdb();
		return Result.success(true);
	}

	///**
	// * 清空当前库
	// * @param connParam
	// */
	//@PostMapping("/flushdb")
	//public void flushdb(@Validated ConnParam connParam) throws IOException {
	//	redisService.flushdb(connParam);
	//}
	//
	///**
	// * 清空所有库
	// * @param connParam
	// */
	//@PostMapping("/flushall")
	//public void flushall(@Validated ConnParam connParam) throws IOException {
	//	redisService.flushall(connParam);
	//}
	//
	///**
	// * Redis 树状 key 节点
	// * @param connParam 连接参数
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/key/tree")
	//public List<TreeKey> treeKeys(@Validated ConnParam connParam) throws IOException {
	//	return redisTreeKeyService.treeKeys(connParam);
	//}
	//
	///**
	// * 查询某个 key 的详细信息
	// * @param connParam 连接参数
	// * @param key key
	// * @return
	// */
	//@GetMapping("/key/info")
	//public KeyScanResult.KeyResult keyInfo(@Validated ConnParam connParam, String key,SerializerParam serializerParam) throws IOException {
	//	return redisTreeKeyService.keyInfo(connParam,key,serializerParam);
	//}
	//
	///**
	// * 按照 key 模式删除一个 key
	// * @param connParam 连接参数
	// * @param keyPattern keyPattern
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/key/del/pattern")
	//public long delKeyPattern(@Validated ConnParam connParam,String keyPattern) throws IOException {
	//	return redisTreeKeyService.dropKeyPattern(connParam, keyPattern);
	//}
	//
	///**
	// * 扫描 key
	// * @param connParam 连接参数
	// * @param keyScanParam 扫描参数
	// * @param serializerParam 序列化参数
	// * @return
	// * @throws IOException
	// * @throws ClassNotFoundException
	// */
	//@GetMapping("/key/scan")
	//public KeyScanResult scan(@Validated ConnParam connParam, KeyScanParam keyScanParam, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
	//	return redisService.scan(connParam,keyScanParam,serializerParam);
	//}
	//
	///**
	// * 批量删除 key
	// * @param delKeysParam 删除 key 参数
	// * @return
	// * @throws IOException
	// */
	//@PostMapping("/key/del")
	//public Long delKeys(@RequestBody DelKeysParam delKeysParam) throws IOException {
	//	return redisService.delKeys(delKeysParam.getConnParam(),delKeysParam.getKeys(),delKeysParam.getSerializerParam());
	//}
	//
	///**
	// * @param connParam 连接参数
	// * @param hashKeyScanParam  hashKey 扫描参数
	// * @param serializerParam 序列化参数
	// * @return
	// */
	//@GetMapping("/key/hscan")
	//public HashKeyScanResult hscan(@Validated ConnParam connParam, HashKeyScanParam hashKeyScanParam, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
	//	return redisService.hscan(connParam,hashKeyScanParam,serializerParam);
	//}
	//
	///**
	// * hash 删除部分 key
	// * @param delFieldsParam 字段删除参数
	// * @return
	// */
	//@PostMapping("/key/hash/hdel")
	//public Long hdel(@RequestBody DelFieldsParam delFieldsParam) throws IOException {
	//	return redisService.hdel(delFieldsParam);
	//}
	//
	///**
	// * 查询数据
	// * @param valueParam 读取值参数
	// * @return
	// * @throws IOException
	// * @throws ClassNotFoundException
	// */
	//@PostMapping("/data")
	//public Object data(@RequestBody ValueParam valueParam) throws IOException, ClassNotFoundException {
	//	return redisService.data(valueParam);
	//}
	//
	///**
	// * 集合操作 , 交(inter),并(union),差(diff)
	// * @param connParam 连接参数
	// * @param members 需要操作的元素列表
	// * @param command 执行的命令,inter,union,diff
	// * @param serializerParam 序列化参数
	// * @return
	// */
	//@GetMapping("/collectionMethods")
	//public Object collectionMethods(@Validated ConnParam connParam,String [] members,String command,
	//	SerializerParam serializerParam) throws IOException, ClassNotFoundException {
	//	return redisService.collectionMethods(connParam,members,command,serializerParam);
	//}
	//
	//
	//
	//
	//
	//
	//
	//
	///**
	// * 运行模式查询 standalone , master-slave, cluster
	// * @param connParam
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/mode")
	//public RedisRunMode mode(@Validated ConnParam connParam) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	return redisConnection.getRunMode();
	//}
	//
	///**
	// * 非集群模式下, 查看节点的数据库数量
	// * @param connParam
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/dbs")
	//public int dbs(@Validated ConnParam connParam) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	return redisConnection.dbs();
	//}
	//
	///**
	// * redis 节点列表
	// * @param connParam
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/nodes")
	//public List<RedisNode> nodes(@Validated ConnParam connParam) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	return redisConnection.getMasterNodes();
	//}
	//
	///**
	// * 聚合接口, 获取连接信息
	// * @param connParam
	// * @return
	// */
	//@GetMapping("/connInfo")
	//public ConnectionInfo connInfo(@Validated ConnParam connParam) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	final RedisRunMode runMode = redisConnection.getRunMode();
	//	final List<RedisNode> masterNodes = redisConnection.getMasterNodes();
	//
	//	return new ConnectionInfo(runMode,masterNodes);
	//}
	//
	///**
	// * 各节点内存使用情况查询
	// * @param connParam
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/memory")
	//public RedisNode.Memory memoryUse(@Validated ConnParam connParam, String nodeId) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	RedisNode redisNode = redisConnection.findRedisNodeById(nodeId);
	//	if (redisNode != null) {
	//		return redisNode.calcMemory();
	//	}
	//	return null;
	//}
	//
	///**
	// * 客户端连接列表
	// * @param connParam
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/clientList")
	//public List<ConnectClient> clientList(@Validated ConnParam connParam,String nodeId) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
	//	if (redisNodeById != null) {
	//		return redisConnection.clientList(redisNodeById);
	//	}
	//	return null;
	//}
	//
	///**
	// * kill 某一个客户端
	// * @param connParam
	// * @param clientId
	// * @return
	// * @throws IOException
	// */
	//@PostMapping("/client/kill")
	//public String killClient(@Validated ConnParam connParam, HostAndPort client, String nodeId) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
	//	if (redisNodeById != null) {
	//		return redisConnection.killClient(redisNodeById,client);
	//	}
	//	return null;
	//}
	//
	///**
	// * 查询 redis 慢查询
	// * @param connParam
	// * @return
	// */
	//@GetMapping("/slowlogs")
	//public List<Slowlog> redisSlowlogs(@Validated ConnParam connParam, String nodeId) throws IOException {
	//	final RedisConnection redisConnection = redisService.redisConnection(connParam);
	//	final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
	//	if (redisNodeById != null) {
	//		return redisConnection.slowlogs(redisNodeById);
	//	}
	//	return null;
	//}
}
