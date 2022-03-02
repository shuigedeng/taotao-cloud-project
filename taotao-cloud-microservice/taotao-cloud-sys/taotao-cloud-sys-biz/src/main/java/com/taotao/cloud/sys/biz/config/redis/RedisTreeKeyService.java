//package com.taotao.cloud.sys.biz.tools.redis.service;
//
//import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.KeyScanResult;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.TreeKey;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.ConnParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.SerializerParam;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisConnection;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisType;
//import com.taotao.cloud.sys.biz.tools.serializer.service.Serializer;
//import com.taotao.cloud.sys.biz.tools.serializer.service.SerializerChoseService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.ScanParams;
//import redis.clients.jedis.ScanResult;
//
//import java.io.IOException;
//import java.util.*;
//
//@Service
//public class RedisTreeKeyService {
//    @Autowired
//    private RedisService redisService;
//    @Autowired
//    private SerializerChoseService serializerChoseService;
//
//    // 1 万条以下的数据可以使用
//    public static final long supportKeys = 20000;
//
//    /**
//     * 查询某个 key 的详细信息
//     * @param connParam
//     * @param key
//     * @param serializerParam
//     * @return
//     * @throws IOException
//     */
//    public KeyScanResult.KeyResult keyInfo(ConnParam connParam, String key, SerializerParam serializerParam) throws IOException {
//        final RedisConnection redisConnection = redisService.redisConnection(connParam);
//        Serializer serializer = serializerChoseService.choseSerializer(serializerParam.getKeySerializer());
//        final byte[] keyBytes = serializer.serialize(key);
//        final RedisType type = redisConnection.type(keyBytes);
//        final Long ttl = redisConnection.ttl(keyBytes);
//        final Long pttl = redisConnection.pttl(keyBytes);
//        final long length = redisConnection.length(keyBytes);
//        final KeyScanResult.KeyResult keyResult = new KeyScanResult.KeyResult(key, type.getValue(), ttl, pttl, length);
//        return keyResult;
//    }
//
//    /**
//     * 按照 key 的模式进行删除
//     * @param connParam
//     * @param keyPattern
//     * @return
//     */
//    public long dropKeyPattern(ConnParam connParam, String keyPattern) throws IOException {
//        final RedisConnection redisConnection = redisService.redisConnection(connParam);
//        final List<RedisNode> masterNodes = redisConnection.getMasterNodes();
//
//        long count = 0 ;
//        for (RedisNode masterNode : masterNodes) {
//            final Jedis jedis = masterNode.browerJedis();
//            try{
//                String cursor = "0";
//                ScanParams scanParams = new ScanParams();
//                scanParams.match(keyPattern).count(1000);
//                do {
//                    ScanResult<String> scan = null;
//                    try {
//                        scan = jedis.scan(cursor, scanParams);
//                        final List<String> result = scan.getResult();
//                        jedis.del(result.toArray(new String[]{}));
//                        count += result.size();
//                    }finally {
//                        if (scan == null){cursor = "0";}else{
//                            cursor = scan.getStringCursor();
//                        }
//                    }
//
//                }while (!"0".equals(cursor));
//            }finally {
//                if (jedis != null){
//                    jedis.close();
//                }
//            }
//        }
//        return count;
//    }
//
//    /**
//     *
//     * @param connParam
//     * @return
//     * @throws IOException
//     */
//    public List<TreeKey> treeKeys(ConnParam connParam) throws IOException {
//        final RedisConnection redisConnection = redisService.redisConnection(connParam);
//        final List<RedisNode> masterNodes = redisConnection.getMasterNodes();
//        long totalKeys = 0 ;
//        for (RedisNode masterNode : masterNodes) {
//            final Map<String, Long> dbSizes = masterNode.getDbSizes();
//            final Long dbsize = dbSizes.get(connParam.getIndex() + "");
//            totalKeys += dbsize;
//        }
//        if (totalKeys > supportKeys){
//            log.error("数据量过大 {} > {}, 不支持树结构",totalKeys,supportKeys);
//            throw new ToolException("key 数据量"+totalKeys+"超过 "+supportKeys+" 不支持使用树结构");
//        }
//
//        TreeKey top = new TreeKey("","virtual");
//        final TreeKey virtual = new TreeKey("virtual","virtual");
//        for (RedisNode masterNode : masterNodes) {
//            final Jedis jedis = masterNode.browerJedis();
//            try {
//                // 需要支持 keys * 命令
//                Set<String> keys = jedis.keys("*");
//
//                for (String key : keys) {
//                    final String[] parts = StringUtils.split(key, ":");
//                    appendTree(parts,virtual,0);
//                }
//
//                // 对于目录上有值的, 单独添加一个节点
//                for (String key : keys) {
//                    final String[] parts = StringUtils.split(key, ":");
//                    TreeKey treeKey = findPath(parts,virtual);
//                    if (treeKey.isFolder()){
//                        final String[] subarray = ArrayUtils.subarray(parts, 0, parts.length - 1);
//                        final TreeKey parent = findPath(subarray, virtual);
//                        parent.addChild(new TreeKey(treeKey.getKey(),treeKey.getName()));
//                    }
//                }
//
//            }finally {
//                if (jedis != null){
//                    jedis.close();
//                }
//            }
//        }
//        return virtual.getChilds();
//    }
//
//    /**
//     * 追加树
//     * @param parent
//     * @param parts
//     * @param deep
//     */
//    public void appendTree(String [] parts, TreeKey parent, int deep){
//        if (deep >= parts.length){
//            return ;
//        }
//        final String part = parts[deep];
//        final List<TreeKey> childs = parent.getChilds();
//        if (CollectionUtils.isNotEmpty(childs)) {
//            final Iterator<TreeKey> iterator = childs.iterator();
//            while (iterator.hasNext()){
//                final TreeKey child = iterator.next();
//                if (child.getName().equals(part)) {
//                    appendTree(parts, child, ++deep);
//                    return ;
//                }
//            }
//        }
//        addTree(parts,parent,deep);
//    }
//
//    public void addTree(String [] parts,TreeKey parent,int deep){
//        if (deep >= parts.length){
//            return ;
//        }
//        for (int i = deep; i < parts.length; i++) {
//            final TreeKey treeKey = new TreeKey(StringUtils.join(parts,':'),parts[i]);
//            parent.addChild(treeKey);
//            parent.setFolder(true);
//            parent = treeKey;
//        }
//    }
//
//    public TreeKey findPath(String [] parts,TreeKey top){
//        TreeKey parent = top;
//        for (String part : parts) {
//            final List<TreeKey> childs = parent.getChilds();
//            for (TreeKey child : childs) {
//                if (child.getName().equals(part)){
//                    parent = child;
//                    continue;
//                }
//            }
//        }
//        return parent;
//    }
//}
