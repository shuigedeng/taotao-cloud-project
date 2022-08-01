package com.taotao.cloud.sys.biz.modules.redis.service.dtos;

import java.lang.reflect.Field;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.sanri.tools.modules.core.dtos.param.ConnectParam;
import com.sanri.tools.modules.core.dtos.param.RedisConnectParam;
import com.sanri.tools.modules.redis.service.CommandReply;

import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;
import redis.clients.jedis.*;
import redis.clients.util.Slowlog;

@Data
@Slf4j
public class RedisConnection {
    private RedisRunMode runMode;
    private RedisNode masterNode;
    private ClusterNode clusterNode;

    public RedisConnection() {
    }

    public void close(){
        if (masterNode != null){
            masterNode.close();
        }
        if (clusterNode != null){
            clusterNode.close();
        }

    }

    /**
     * 非集群模式时设置数据库索引
     * @param index
     */
    public void select(int index){
        if (masterNode != null){
            masterNode.setIndex(index);
        }
    }

    /**
     * 刷新节点列表和运行模式
     * @param redisConnectParam
     */
    public void refresh(RedisConnectParam redisConnectParam){
        final ConnectParam connectParam = redisConnectParam.getConnectParam();

        // 根据连接参数创建第一个节点,这个节点不一定是主节点
        RedisNode redisNode = new RedisNode();
        redisNode.refresh(new HostAndPort(connectParam.getHost(),connectParam.getPort()),redisConnectParam);

        // 往上找到 master 节点
        RedisNode master = redisNode;
        while ((master = master.getMasterNode()) != null){
            redisNode = master;
        }

        // 获取运行模式
        final boolean isCluster = redisNode.calcIsCluster();
        if (isCluster){
            runMode = RedisRunMode.cluster;
            clusterNode = new ClusterNode();
            // 获取集群所有节点,并把每个子节点关联上集群控制句柄
            final Jedis jedis = redisNode.browerJedis();
            try{
                final String clusterNodes = jedis.clusterNodes();
                List<String[]> nodeCommandLines = CommandReply.spaceCommandReply.parser(clusterNodes);
                for (String[] line : nodeCommandLines) {
                    final HostAndPort hostAndPort = HostAndPort.parseString(line[1].split("@")[0]);
                    RedisNode node = new RedisNode();
                    this.clusterNode.addNode(node);
                    node.refresh(hostAndPort,redisConnectParam);
                    node.setId(line[0]);
                    node.setMasterId(line[3]);
                    if (node.isMaster() && line.length >= 9) {
                        // 有可能还没有分配槽位
                        String slots = line[8];
                        if (slots.contains("-")) {
                            String[] split = StringUtils.split(slots, '-');
                            int start = NumberUtils.toInt(split[0]);
                            int end = NumberUtils.toInt(split[1]);
                            node.setSlotStart(start);
                            node.setSlotEnd(end);
                        } else {
                            int around = NumberUtils.toInt(slots);
                            node.setSlotStart(around);
                            node.setSlotEnd(around);
                        }
                    }
                }

                this.clusterNode.createJedisCluster(redisConnectParam);
            }finally {
                jedis.close();
            }
        }else{
            // 顶层只添加主节点
            masterNode = redisNode;

            if (CollectionUtils.isNotEmpty(redisNode.getSlaveNodes())){
                runMode = RedisRunMode.masterSlave;
            }else {
                runMode = RedisRunMode.standalone;
            }
        }
    }

    /**
     * 获取所有的主节点列表
     * @return
     */
    public List<RedisNode> getMasterNodes(){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getMasterNodes();
        }
        return Arrays.asList(masterNode);
    }

    public RedisType type(byte [] key){
        if (runMode == RedisRunMode.cluster){
            final String type = clusterNode.getJedisCluster().type(key);
            return RedisType.parse(type);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return RedisType.parse(jedis.type(key));
        }finally {
            jedis.close();
        }
    }

    public byte[] get(byte [] key){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().get(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    public Long del(byte[]... keys){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().del(keys);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.del(keys);
        }finally {
            jedis.close();
        }
    }

    public Long hdel(byte [] key, byte[]... fields){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().hdel(key,fields);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.hdel(key,fields);
        }finally {
            jedis.close();
        }
    }


    public long llen(byte [] key){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().llen(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.llen(key);
        }finally {
            jedis.close();
        }
    }

    public ScanResult<byte[]> sscan(byte [] key, byte[] cursor, ScanParams scanParams){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().sscan(key,cursor,scanParams);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.sscan(key,cursor,scanParams);
        }finally {
            jedis.close();
        }
    }

    public ScanResult<Tuple> zscan(byte [] key, byte[] cursor, ScanParams scanParams){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().zscan(key,cursor,scanParams);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.zscan(key,cursor,scanParams);
        }finally {
            jedis.close();
        }
    }

    public long zcard(byte[] keyBytes) {
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().zcard(keyBytes);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.zcard(keyBytes);
        }finally {
            jedis.close();
        }

    }

    public Set<Tuple> zrangeWithScores(byte [] key, long start, long stop){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().zrangeWithScores(key,start,stop);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.zrangeWithScores(key,start,stop);
        }finally {
            jedis.close();
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().zrangeByScoreWithScores(key,min,max);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.zrangeByScoreWithScores(key,min,max);
        }finally {
            jedis.close();
        }
    }

    public List<byte[]> lrange(byte [] key,long start,long stop){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().lrange(key,start,stop);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.lrange(key,start,stop);
        }finally {
            jedis.close();
        }
    }

    public Set<byte[]> smembers(byte[] key){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().smembers(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.smembers(key);
        }finally {
            jedis.close();
        }
    }

    public Set<byte[]> sinter(byte[][] keys){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().sinter(keys);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.sinter(keys);
        }finally {
            jedis.close();
        }
    }
    public Set<byte[]> sdiff(byte[][] keys){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().sdiff(keys);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.sdiff(keys);
        }finally {
            jedis.close();
        }
    }
    public Set<byte[]> sunion(byte[][] keys){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().sunion(keys);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.sunion(keys);
        }finally {
            jedis.close();
        }
    }

    public Long ttl(byte[] key) {
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().ttl(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.ttl(key);
        }finally {
            jedis.close();
        }
    }

    public Long pttl(byte [] key){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().pttl(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.pttl(key);
        }finally {
            jedis.close();
        }
    }

    public long length(byte [] key){
        final RedisType redisType = type(key);
        if (runMode == RedisRunMode.cluster){
            final JedisCluster jedisCluster = clusterNode.getJedisCluster();
            switch (redisType){
                case string:
                    return jedisCluster.strlen(key);
                case Hash:
                    return jedisCluster.hlen(key);
                case List:
                    return jedisCluster.llen(key);
                case Set:
                    return jedisCluster.scard(key);
                case ZSet:
                    return jedisCluster.zcard(key);
                default:
            }
            log.error("未知类型:{}",jedisCluster.type(key));
            return -1;
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            switch (redisType){
                case string:
                    return jedis.strlen(key);
                case Set:
                    return jedis.scard(key);
                case ZSet:
                    return jedis.zcard(key);
                case List:
                    return jedis.llen(key);
                case Hash:
                    return jedis.hlen(key);
                default:
            }
            log.error("未知类型:{}",jedis.type(key));
            return -1;
        }finally {
            jedis.close();
        }
    }

    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte [] cursor, ScanParams scanParams){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().hscan(key, cursor, scanParams);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.hscan(key,cursor,scanParams);
        }finally {
            jedis.close();
        }
    }

    public Map<byte[], byte[]> hgetAll(byte [] key){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().hgetAll(key);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.hgetAll(key);
        }finally {
            jedis.close();
        }
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields){
        if (runMode == RedisRunMode.cluster){
            return clusterNode.getJedisCluster().hmget(key,fields);
        }
        final Jedis jedis = masterNode.browerJedis();
        try{
            return jedis.hmget(key,fields);
        }finally {
            jedis.close();
        }
    }

    /**
     * 获取当前连接数据库数量
     * @return
     */
    public int dbs() {
        if (runMode == RedisRunMode.cluster){
            return 0;
        }

        final Jedis jedis = masterNode.browerJedis();
        try{
            final List<String> databases = jedis.configGet("databases");
            return NumberUtils.toInt(databases.get(1));
        }finally {
            jedis.close();
        }
    }

    public RedisNode findRedisNodeById(String nodeId) {
        // 先从主节点开始找, 找不到再找从节点
        final List<RedisNode> masterNodes = getMasterNodes();
        for (RedisNode node : masterNodes) {
            if (node.getId().equals(nodeId)){
                return node;
            }
        }
        // 从节点递归查找
        for (RedisNode node : masterNodes) {
            final RedisNode nodeInSlavesById = node.findNodeInSlavesById(nodeId);
            if (nodeInSlavesById != null){
                return nodeInSlavesById;
            }
        }
        return null;
    }

    /**
     * 计算节点当前连接的客户端列表
     * @param redisNode
     * @return
     */
    public List<ConnectClient> clientList(RedisNode redisNode){
        final Jedis jedis = redisNode.browerJedis();
        try{
            List<ConnectClient> clients  = new ArrayList<>();
            Class<ConnectClient> clientClass = ConnectClient.class;
            final String clientList = jedis.clientList();
            List<Map<String, String>> mapList = CommandReply.spaceCommandReply.parserWithHeader(clientList, "id", "addr", "fd", "name", "age", "idle", "flags", "db", "sub", "psub", "multi", "qbuf", "qbuf-free", "obl", "oll", "omem", "events", "cmd");
            for (Map<String, String> props : mapList) {
                ConnectClient client = new ConnectClient();
                clients.add(client);

                Iterator<String> iterator = props.values().iterator();
                while (iterator.hasNext()){
                    String next = iterator.next();
                    String[] split = StringUtils.split(next, "=", 2);
                    if ("addr".equals(split[0]) && StringUtils.isNotBlank(split[1])){
                        HostAndPort hostAndPort = HostAndPort.parseString(split[1]);
                        client.setConnect(hostAndPort);
                        continue;
                    }
                    Field declaredField = FieldUtils.getDeclaredField(clientClass, split[0], true);
                    if (declaredField != null) {
                        try {
                            FieldUtils.writeField(declaredField,client,split[1],true);
                        } catch (IllegalAccessException e) {
                            log.error("redis 客户端信息写入字段[{}],值[{}] 失败",split[0],split[1]);
                        }
                    }
                }
            }
            return clients;
        }finally {
            jedis.close();
        }
    }

    public List<Slowlog> slowlogs(RedisNode redisNode){
        final Jedis jedis = redisNode.browerJedis();
        try{
            return jedis.slowlogGet();
        }finally {
            jedis.close();
        }
    }

    public String killClient(RedisNode redisNode, HostAndPort hostAndPort) {
        final Jedis jedis = redisNode.browerJedis();
        try{
            return jedis.clientKill(hostAndPort.toString());
        }finally {
            jedis.close();
        }
    }

}
