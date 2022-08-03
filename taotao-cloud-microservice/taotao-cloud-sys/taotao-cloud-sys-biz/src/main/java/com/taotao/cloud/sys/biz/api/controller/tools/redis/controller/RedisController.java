package com.taotao.cloud.sys.biz.api.controller.tools.redis.controller;

import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.HashKeyScanResult;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.KeyScanResult;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.TreeKey;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.ConnParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.DelFieldsParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.DelKeysParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.HashKeyScanParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.KeyScanParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.SerializerParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in.ValueParam;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.service.RedisService;
import com.taotao.cloud.sys.biz.api.controller.tools.redis.service.RedisTreeKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/redis")
@RestController
@Validated
public class RedisController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTreeKeyService redisTreeKeyService;

    /**
     * 连接重建
     * @param connParam
     */
    @PostMapping("/connect/rebuild")
    public void rebuildConnect(@Validated ConnParam connParam) throws IOException {
        redisService.rebuildConnect(connParam);
    }

    /**
     * 清空当前库
     * @param connParam
     */
    @PostMapping("/flushdb")
    public void flushdb(@Validated ConnParam connParam) throws IOException {
        redisService.flushdb(connParam);
    }

    /**
     * 清空所有库
     * @param connParam
     */
    @PostMapping("/flushall")
    public void flushall(@Validated ConnParam connParam) throws IOException {
        redisService.flushall(connParam);
    }

    /**
     * Redis 树状 key 节点
     * @param connParam 连接参数
     * @return
     * @throws IOException
     */
    @GetMapping("/key/tree")
    public List<TreeKey> treeKeys(@Validated ConnParam connParam) throws IOException {
        return redisTreeKeyService.treeKeys(connParam);
    }

    /**
     * 查询某个 key 的详细信息
     * @param connParam 连接参数
     * @param key key
     * @return
     */
    @GetMapping("/key/info")
    public KeyScanResult.KeyResult keyInfo(@Validated ConnParam connParam, String key, SerializerParam serializerParam) throws IOException {
        return redisTreeKeyService.keyInfo(connParam,key,serializerParam);
    }

    /**
     * 按照 key 模式删除一个 key
     * @param connParam 连接参数
     * @param keyPattern keyPattern
     * @return
     * @throws IOException
     */
    @GetMapping("/key/del/pattern")
    public long delKeyPattern(@Validated ConnParam connParam,String keyPattern) throws IOException {
        return redisTreeKeyService.dropKeyPattern(connParam, keyPattern);
    }

    /**
     * 扫描 key
     * @param connParam 连接参数
     * @param keyScanParam 扫描参数
     * @param serializerParam 序列化参数
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @GetMapping("/key/scan")
    public KeyScanResult scan(@Validated ConnParam connParam, KeyScanParam keyScanParam, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
        return redisService.scan(connParam,keyScanParam,serializerParam);
    }

    /**
     * 批量删除 key
     * @param delKeysParam 删除 key 参数
     * @return
     * @throws IOException
     */
    @PostMapping("/key/del")
    public Long delKeys(@RequestBody DelKeysParam delKeysParam) throws IOException {
        return redisService.delKeys(delKeysParam.getConnParam(),delKeysParam.getKeys(),delKeysParam.getSerializerParam());
    }

    /**
     * @param connParam 连接参数
     * @param hashKeyScanParam  hashKey 扫描参数
     * @param serializerParam 序列化参数
     * @return
     */
    @GetMapping("/key/hscan")
    public HashKeyScanResult hscan(@Validated ConnParam connParam, HashKeyScanParam hashKeyScanParam, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
        return redisService.hscan(connParam,hashKeyScanParam,serializerParam);
    }

    /**
     * hash 删除部分 key
     * @param delFieldsParam 字段删除参数
     * @return
     */
    @PostMapping("/key/hash/hdel")
    public Long hdel(@RequestBody DelFieldsParam delFieldsParam) throws IOException {
        return redisService.hdel(delFieldsParam);
    }

    /**
     * 查询数据
     * @param valueParam 读取值参数
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @PostMapping("/data")
    public Object data(@RequestBody ValueParam valueParam) throws IOException, ClassNotFoundException {
        return redisService.data(valueParam);
    }

    /**
     * 集合操作 , 交(inter),并(union),差(diff)
     * @param connParam 连接参数
     * @param members 需要操作的元素列表
     * @param command 执行的命令,inter,union,diff
     * @param serializerParam 序列化参数
     * @return
     */
    @GetMapping("/collectionMethods")
    public Object collectionMethods(@Validated ConnParam connParam,String [] members,String command,SerializerParam serializerParam) throws IOException, ClassNotFoundException {
        return redisService.collectionMethods(connParam,members,command,serializerParam);
    }
}
