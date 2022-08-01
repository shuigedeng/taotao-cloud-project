package com.taotao.cloud.sys.biz.modules.redis.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.core.dtos.param.ConnectParam;
import com.sanri.tools.modules.core.dtos.param.RedisConnectParam;
import com.sanri.tools.modules.redis.service.ColonCommandReply;
import com.sanri.tools.modules.redis.service.CommandReply;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import redis.clients.jedis.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 单台 redis 节点
 */
@Data
public class RedisNode {
    private String id;
    private HostAndPort hostAndPort;
    private String role;
    /**
     * 父级 id
     */
    private String masterId;

    /**
     * 槽位范围,只有集群模式才会有
     */
    private int slotStart;
    private int slotEnd;

    /**
     * 当前节点数据量
     */
    private Map<String,Long> dbSizes = new LinkedHashMap<>();

    /**
     * 数据库数量
     */
    private int dbs;

    /**
     * redis 连接
     */
    @JsonIgnore
    private JedisPool jedisPool;
    private int index;

    @JsonIgnore
    private RedisNode masterNode;
    private List<RedisNode> slaveNodes = new ArrayList<>();

    /**
     * info 信息, 避免重复翻译
     */
    private Info info = new Info();

    public RedisNode() {
    }

    public Jedis browerJedis(){
        if (jedisPool == null){
            return null;
        }
        final Jedis resource = jedisPool.getResource();
        resource.select(index);
        return resource;
    }

    public boolean isMaster(){
        return "master".equals(role);
    }

    public boolean isSlave(){
        return "slave".equals(role);
    }

    /**
     * 级联关闭所有从节点
     */
    public void close(){
        if (CollectionUtils.isNotEmpty(slaveNodes)) {
            for (RedisNode slave : slaveNodes) {
                try {
                    slave.close();
                }catch (Exception e){}
            }
        }
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    static JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    static {
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(2);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);//jedis 第一次启动时，会报错
        jedisPoolConfig.setTestOnReturn(true);
    }

    /**
     * 使用 jedisPool 刷新节点数据
     * @param jedisPool
     * @param jedisPoolConfig
     */
    public void refresh(HostAndPort hostAndPort,RedisConnectParam redisConnectParam){
        refreshSelf(hostAndPort,redisConnectParam);
        refreshMaster(redisConnectParam);
        refreshSlaves(redisConnectParam);
    }

    public void refreshSelf(HostAndPort hostAndPort,RedisConnectParam redisConnectParam){
        final ConnectParam connectParam = redisConnectParam.getConnectParam();

        String password = redisConnectParam.getAuthParam().getPassword();
        if (StringUtils.isBlank(password)){
            password = null;
        }
        this.jedisPool = new JedisPool(jedisPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(), connectParam.getConnectionTimeout(),password);
        final Jedis jedis = jedisPool.getResource();
        try{
            id = hostAndPort.toString();
            this.hostAndPort = hostAndPort;

            final boolean cluster = isCluster(jedis);
            if (cluster){
                dbs = 1 ;
                dbSizes.put("0",jedis.dbSize());
            }else {
                final List<String> databases = jedis.configGet("databases");
                dbs = NumberUtils.toInt(databases.get(1));
                for (int i = 0; i < dbs; i++) {
                    jedis.select(i);
                    dbSizes.put(i+"",jedis.dbSize());
                }
            }
            final Replication replication = replicationInfo(jedis);
            role = replication.getRole();
        }finally {
            jedis.close();
        }
    }

    public void refreshSlaves(RedisConnectParam redisConnectParam) {
        this.slaveNodes.clear();
        final List<HostAndPort> slaves = info.replication.getSlaves();
        if (CollectionUtils.isNotEmpty(slaves)){
            for (HostAndPort slaveHostAndPort : slaves) {
                RedisNode slaveRedisNode = new RedisNode();
                this.slaveNodes.add(slaveRedisNode);
                slaveRedisNode.masterNode = this;
                slaveRedisNode.refreshSelf(slaveHostAndPort, redisConnectParam);
                slaveRedisNode.refreshSlaves(redisConnectParam);
            }
        }
    }

    public void refreshMaster(RedisConnectParam redisConnectParam) {
        final HostAndPort masterHostAndPort = info.replication.getMaster();
        if (masterHostAndPort != null) {
            RedisNode masterRedisNode = new RedisNode();
            masterNode = masterRedisNode;
            masterRedisNode.refreshSelf(masterHostAndPort, redisConnectParam);
            masterRedisNode.refreshMaster(redisConnectParam);
        }
    }

    /**
     * 在从节点里面根据 id 去找一个节点
     * @param nodeId
     * @return
     */
    public RedisNode findNodeInSlavesById(String nodeId){
        for (RedisNode slave : slaveNodes) {
            if (slave.getId().equals(nodeId)){
                return slave;
            }
            final RedisNode nodeInSlavesById = slave.findNodeInSlavesById(nodeId);
            if (nodeInSlavesById != null){
                return nodeInSlavesById;
            }
        }
        return null;
    }

    /**
     * 外部拿 Replication 数据
     * @return
     */
    public Replication calcReplication(){
        if (info.getReplication() != null){
            return info.getReplication();
        }
        final Jedis jedis = browerJedis();
        try {
            info.replication =  replicationInfo(jedis);
            return info.replication;
        }finally {
            jedis.close();
        }
    }

    public boolean calcIsCluster(){
        if (info.getCluster() != null){
            return info.getCluster();
        }
        final Jedis jedis = browerJedis();
        try {
            return isCluster(jedis);
        }finally {
            jedis.close();
        }
    }

    public Memory calcMemory(){
        if (info.memory != null){
            return info.memory;
        }
        final Jedis jedis = browerJedis();
        try{
            info.memory = memory(jedis);
            return info.memory;
        }finally {
            jedis.close();
        }
    }

    public Server calcServer(){
        if (info.server != null){
            return info.server;
        }
        final Jedis jedis = browerJedis();
        try{
            info.server = server(jedis);
            return info.server;
        }finally {
            jedis.close();
        }
    }

    private Server server(Jedis jedis){
        String info = jedis.info("Server");
        List<String[]> parser = CommandReply.colonCommandReply.parser(info);
        Server server = new Server();
        for (String[] line : parser) {
            if (line.length == 2){
                if ("redis_version".equals(line[0])){
                    server.setVersion(line[1]);
                }else if ("redis_mode".equals(line[0])){
                    server.setMode(line[1]);
                }else if ("os".equals(line[0])){
                    server.setOs(line[1]);
                }else if ("arch_bits".equals(line[0])){
                    server.setArchBits(line[1]);
                }else if ("process_id".equals(line[0])){
                    server.setPid(line[1]);
                }else if ("tcp_port".equals(line[0])){
                    server.setPort(line[1]);
                }else if ("uptime_in_seconds".equals(line[0])){
                    server.setUpTime(NumberUtils.toInt(line[1]));
                }
            }
        }

        return server;
    }

    private Memory memory(Jedis jedis){
        String info = jedis.info("Memory");
        List<String[]> parser = CommandReply.colonCommandReply.parser(info);
        Memory memory = new Memory();
        for (String[] line : parser) {
            if (line.length == 2){
                if ("used_memory_rss".equals(line[0])){
                    memory.setRss(NumberUtils.toLong(line[1]));
                }else if ("used_memory_lua".equals(line[0])){
                    memory.setLua(NumberUtils.toLong(line[1]));
                }else if ("maxmemory".equals(line[0])){
                    memory.setMax(NumberUtils.toLong(line[1]));
                }else if ("total_system_memory".equals(line[0])){
                    memory.setSystem(NumberUtils.toLong(line[1]));
                }else if ("maxmemory_policy".equals(line[0])){
                    memory.setPolicy(line[1]);
                }
            }
        }
        this.info.setMemory(memory);
        return memory;
    }


    private boolean isCluster(Jedis jedis){
        String info = jedis.info("Cluster");
        Map<String, String> properties = ColonCommandReply.colonCommandReply.parserKeyValue(info);
        String clusterEnabled = properties.get("cluster_enabled");
        if("1".equals(clusterEnabled)){
            this.info.setCluster(true);
        }else{
            this.info.setCluster(false);
        }
        return this.info.getCluster();
    }

    /**
     * 解析 redis 的 Replication 数据
     * @param currentJedis
     * @return
     */
    static final Pattern pattern = Pattern.compile("slave\\d+");
    private Replication replicationInfo(Jedis currentJedis) {
        String info = currentJedis.info("Replication");
        final Replication replication = new Replication();
        List<String[]> parser = CommandReply.colonCommandReply.parser(info);
        String masterHost = null; int masterPort = -1;

        for (String[] line : parser) {
            if (line.length == 2){
                if ("role".equals(line[0])){
                    replication.setRole(line[1]);
                }
                if (pattern.matcher(line[0]).find()){
                    String[] split = StringUtils.split(line[1], ',');
                    String host = split[0].split("=")[1];
                    int port = NumberUtils.toInt(split[1].split("=")[1]);
                    final HostAndPort hostAndPort = new HostAndPort(host, port);
                    replication.addSlave(hostAndPort);
                }
                if ("master_host".equals(line[0])){
                    masterHost = line[1];
                }
                if ("master_port".equals(line[0])){
                    masterPort = NumberUtils.toInt(line[1]);
                }
            }
        }
        if (masterHost != null && masterPort != -1){
            replication.setMaster(new HostAndPort(masterHost,masterPort));
        }

        this.info.setReplication(replication);
        return replication;
    }

    public String getMark(){
        return id+":"+hostAndPort;
    }

    @Data
    public static final class Replication{
        private String role;
        private HostAndPort master;
        private List<HostAndPort> slaves = new ArrayList<>();

        public void addSlave(HostAndPort hostAndPort){
            slaves.add(hostAndPort);
        }

    }

    @Data
    public static final class Memory{
        private long rss;
        private long max;
        private String policy;
        private long system;
        private long lua;
        private long dbSize;
        private String role;
    }

    @Data
    public static final class Server {
        private String version;
        private String mode;
        private String os;
        private String archBits;
        private String pid;
        private String port;
        private int upTime;
    }

    @Data
    public static final class Info{
        private Boolean cluster;
        private Replication replication;
        private Memory memory;
        private Server server;
    }
}
