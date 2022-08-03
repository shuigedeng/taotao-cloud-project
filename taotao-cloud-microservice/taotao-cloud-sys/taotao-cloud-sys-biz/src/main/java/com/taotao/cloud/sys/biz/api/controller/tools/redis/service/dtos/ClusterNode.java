package com.taotao.cloud.sys.biz.api.controller.tools.redis.service.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.ConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.RedisConnectParam;
import org.apache.commons.collections.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClusterNode {
    private List<RedisNode> redisNodes = new ArrayList<>();
    /**
     * jedisCluster 线程安全, jedis 非线程安全
     */
    private JedisCluster jedisCluster;

    public void close() {
        if (jedisCluster != null){
            try {
                jedisCluster.close();
            } catch (IOException e) {}
        }
        if (CollectionUtils.isNotEmpty(redisNodes)){
            for (RedisNode redisNode : redisNodes) {
                try {
                    redisNode.close();
                }catch (Exception e){}
            }
        }
    }

    public void addNode(RedisNode redisNode){
        redisNodes.add(redisNode);
    }

    public List<RedisNode> getMasterNodes(){
        return redisNodes.stream().filter(RedisNode::isMaster).collect(Collectors.toList());
    }

    public void createJedisCluster(RedisConnectParam redisConnectParam){
        final ConnectParam connectParam = redisConnectParam.getConnectParam();
        final Set<HostAndPort> hostAndPorts = redisNodes.stream().map(RedisNode::getHostAndPort).collect(Collectors.toSet());
        this.jedisCluster = new JedisCluster(hostAndPorts, connectParam.getConnectionTimeout(), connectParam.getSessionTimeout(), connectParam.getMaxAttempts(),redisConnectParam.getAuthParam().getPassword(), RedisNode.jedisPoolConfig);
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }
}
