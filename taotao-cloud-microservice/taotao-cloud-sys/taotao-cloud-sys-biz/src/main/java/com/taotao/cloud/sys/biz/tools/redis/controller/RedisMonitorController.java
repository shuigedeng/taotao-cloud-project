package com.taotao.cloud.sys.biz.tools.redis.controller;

import java.io.IOException;
import java.util.List;

import com.sanri.tools.modules.redis.controller.dtos.ConnectionInfo;
import com.sanri.tools.modules.redis.service.dtos.ConnectClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sanri.tools.modules.redis.dtos.in.ConnParam;
import com.sanri.tools.modules.redis.service.RedisService;
import com.sanri.tools.modules.redis.service.dtos.RedisConnection;
import com.sanri.tools.modules.redis.service.dtos.RedisNode;
import com.sanri.tools.modules.redis.service.dtos.RedisRunMode;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.util.Slowlog;

@RequestMapping("/redis/monitor")
@RestController
@Validated
public class RedisMonitorController {
    @Autowired
    private RedisService redisService;

    /**
     * 运行模式查询 standalone , master-slave, cluster
     * @param connParam
     * @return
     * @throws IOException
     */
    @GetMapping("/mode")
    public RedisRunMode mode(@Validated ConnParam connParam) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        return redisConnection.getRunMode();
    }

    /**
     * 非集群模式下, 查看节点的数据库数量
     * @param connParam
     * @return
     * @throws IOException
     */
    @GetMapping("/dbs")
    public int dbs(@Validated ConnParam connParam) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        return redisConnection.dbs();
    }

    /**
     * redis 节点列表
     * @param connParam
     * @return
     * @throws IOException
     */
    @GetMapping("/nodes")
    public List<RedisNode> nodes(@Validated ConnParam connParam) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        return redisConnection.getMasterNodes();
    }

    /**
     * 聚合接口, 获取连接信息
     * @param connParam
     * @return
     */
    @GetMapping("/connInfo")
    public ConnectionInfo connInfo(@Validated ConnParam connParam) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        final RedisRunMode runMode = redisConnection.getRunMode();
        final List<RedisNode> masterNodes = redisConnection.getMasterNodes();

        return new ConnectionInfo(runMode,masterNodes);
    }

    /**
     * 各节点内存使用情况查询
     * @param connParam
     * @return
     * @throws IOException
     */
    @GetMapping("/memory")
    public RedisNode.Memory memoryUse(@Validated ConnParam connParam, String nodeId) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        RedisNode redisNode = redisConnection.findRedisNodeById(nodeId);
        if (redisNode != null) {
            return redisNode.calcMemory();
        }
        return null;
    }

    /**
     * 客户端连接列表
     * @param connParam
     * @return
     * @throws IOException
     */
    @GetMapping("/clientList")
    public List<ConnectClient> clientList(@Validated ConnParam connParam,String nodeId) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
        if (redisNodeById != null) {
            return redisConnection.clientList(redisNodeById);
        }
        return null;
    }

    /**
     * kill 某一个客户端
     * @param connParam
     * @param clientId
     * @return
     * @throws IOException
     */
    @PostMapping("/client/kill")
    public String killClient(@Validated ConnParam connParam, HostAndPort client, String nodeId) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
        if (redisNodeById != null) {
            return redisConnection.killClient(redisNodeById,client);
        }
        return null;
    }

    /**
     * 查询 redis 慢查询
     * @param connParam
     * @return
     */
    @GetMapping("/slowlogs")
    public List<Slowlog> redisSlowlogs(@Validated ConnParam connParam, String nodeId) throws IOException {
        final RedisConnection redisConnection = redisService.redisConnection(connParam);
        final RedisNode redisNodeById = redisConnection.findRedisNodeById(nodeId);
        if (redisNodeById != null) {
            return redisConnection.slowlogs(redisNodeById);
        }
        return null;
    }
}
