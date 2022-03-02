//package com.taotao.cloud.sys.biz.tools.redis.service;
//
//import com.taotao.cloud.sys.biz.tools.core.dtos.UpdateConnectEvent;
//import com.taotao.cloud.sys.biz.tools.core.dtos.param.RedisConnectParam;
//import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
//import com.taotao.cloud.sys.biz.tools.core.service.classloader.ClassloaderService;
//import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.HashKeyScanResult;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.KeyScanResult;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.SetScanResult;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.ZSetScanResult;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.ZSetTuple;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.BaseKeyScanParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.ConnParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.DelFieldsParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.HashKeyScanParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.KeyScanParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.SerializerParam;
//import com.taotao.cloud.sys.biz.tools.redis.dtos.in.ValueParam;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisConnection;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisNode;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisRunMode;
//import com.taotao.cloud.sys.biz.tools.redis.service.dtos.RedisType;
//import com.taotao.cloud.sys.biz.tools.serializer.service.Serializer;
//import com.taotao.cloud.sys.biz.tools.serializer.service.SerializerChoseService;
//import java.io.IOException;
//import java.net.SocketTimeoutException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.annotation.PreDestroy;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Service;
//
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.ScanParams;
//import redis.clients.jedis.ScanResult;
//import redis.clients.jedis.Tuple;
//import redis.clients.jedis.exceptions.JedisConnectionException;
//import redis.clients.util.JedisClusterCRC16;
//
//@Service
//public class RedisService implements ApplicationListener<UpdateConnectEvent> {
//    // 保存的 redis 连接信息
//    private Map<String, RedisConnection> clientMap = new ConcurrentHashMap<>();
//    @Autowired
//    private ConnectService connectService;
//
//    public static final String MODULE = "redis";
//
//    @Autowired
//    private SerializerChoseService serializerChoseService;
//    @Autowired
//    private ClassloaderService classloaderService;
//
//    /**
//     * 扫描 key 信息
//     * @param connParam
//     * @param redisScanParam
//     * @param serializerParam
//     * @throws IOException
//     * @return
//     */
//    public KeyScanResult scan(ConnParam connParam, KeyScanParam redisScanParam, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
//        final RedisConnection redisConnection = redisConnection(connParam);
//        KeyScanResult allScanKeyResult = new KeyScanResult();
//
//        final List<RedisNode> masterNodes = redisConnection.getMasterNodes();
//        final Iterator<RedisNode> iterator = masterNodes.iterator();
//        boolean searchBegin = StringUtils.isNotBlank(redisScanParam.getNodeId());
//        while (iterator.hasNext()){
//            final RedisNode redisNode = iterator.next();
//            // 找到第一个搜索的节点
//            if (searchBegin && !redisNode.getId().equals(redisScanParam.getNodeId())){
//                continue;
//            }
//            searchBegin = false;
//
//            final KeyScanResult keyScanResult = nodeScan(redisScanParam, serializerParam, redisNode);
//            allScanKeyResult.getKeys().addAll(keyScanResult.getKeys());
//            allScanKeyResult.setNodeId(keyScanResult.getNodeId());
//            allScanKeyResult.setCursor(keyScanResult.getCursor());
//            if (allScanKeyResult.getKeys().size() >= redisScanParam.getLimit()) {
//                // 已经扫描到足够的数据
//                if (keyScanResult.isFinish()) {
//                    // 如果当前节点已经完成 , 则下次需要扫描下一个节点
//                    if (iterator.hasNext()) {
//                        allScanKeyResult.setNodeId(iterator.next().getId());
//                    }else{
//                        // 没有下一个节点了, 扫描完成
//                        allScanKeyResult.setDone(true);
//                    }
//                }
//                break;
//            }
//
//        }
//
//        // 如果所有节点扫描完成, 还是没有足够的数据,则所有节点扫描完成
//        if (allScanKeyResult.getKeys().size() < redisScanParam.getLimit()){
//            allScanKeyResult.setDone(true);
//        }
//
//        return allScanKeyResult;
//    }
//
//    /**
//     * 删除 key
//     * @param connParam
//     * @param keys
//     * @param serializerParam
//     * @return
//     * @throws IOException
//     */
//    public Long delKeys(ConnParam connParam, List<String> keys, SerializerParam serializerParam) throws IOException {
//        Serializer keySerializer = serializerChoseService.choseSerializer(serializerParam.getKeySerializer());
//        final RedisConnection redisConnection = redisConnection(connParam);
//        byte[][] delKeys = new byte[keys.size()][];
//        for (int i = 0; i < keys.size(); i++) {
//            final byte[] serialize = keySerializer.serialize(keys.get(i));
//            delKeys[i] = serialize;
//        }
//        return redisConnection.del(delKeys);
//    }
//
//    /**
//     * hash 数据扫描
//     * @param connParam
//     * @param hashKeyScanParam
//     * @param serializerParam
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    public HashKeyScanResult hscan(ConnParam connParam, HashKeyScanParam hashKeyScanParam,SerializerParam serializerParam) throws IOException, ClassNotFoundException {
//        final RedisConnection redisConnection = redisConnection(connParam);
//        ClassLoader classloader = classloaderService.getClassloader(serializerParam.getClassloaderName());
//        Serializer keySerializer = serializerChoseService.choseSerializer(serializerParam.getKeySerializer());
//        Serializer hashKeySerializer = serializerChoseService.choseSerializer(serializerParam.getHashKey());
//        final Serializer hashValueSerializer = serializerChoseService.choseSerializer(serializerParam.getHashValue());
//
//        byte[] keyBytes = keySerializer.serialize(hashKeyScanParam.getKey());
//        final RedisType redisType = redisConnection.type(keyBytes);
//        if (redisType != RedisType.Hash){
//            throw new ToolException("不支持的 redis 类型[" + redisType + "],在子键[" + hashKeyScanParam.getKey() + "]扫描");
//        }
//
//        // 查询全部
//        if (hashKeyScanParam.isAll()){
//            HashKeyScanResult hashKeyScanResult = new HashKeyScanResult(hashKeyScanParam.getKey());
//            final Map<byte[], byte[]> allData = redisConnection.hgetAll(keyBytes);
//            final Iterator<Map.Entry<byte[], byte[]>> iterator = allData.entrySet().iterator();
//            while (iterator.hasNext()){
//                final Map.Entry<byte[], byte[]> entry = iterator.next();
//                final Object key = hashKeySerializer.deserialize(entry.getKey(), classloader);
//                final Object value = hashValueSerializer.deserialize(entry.getValue(), classloader);
//                hashKeyScanResult.getData().put(Objects.toString(key),value);
//            }
//            hashKeyScanResult.setFields(new ArrayList<>(hashKeyScanResult.getData().keySet()));
//            hashKeyScanResult.setFinish(true);
//            hashKeyScanResult.setCursor("0");
//            return hashKeyScanResult;
//        }
//
//        // 查询部分 fields
//        final String[] queryFields = hashKeyScanParam.getFields();
//        if (ArrayUtils.isNotEmpty(queryFields)){
//            HashKeyScanResult hashKeyScanResult = new HashKeyScanResult(hashKeyScanParam.getKey());
//            hashKeyScanResult.setFinish(true);
//            hashKeyScanResult.setFields(Arrays.asList(queryFields));
//            List<byte[]> queryFieldBytes = new ArrayList<>();
//            for (String queryField : queryFields) {
//                final byte[] serialize = hashKeySerializer.serialize(queryField);
//                queryFieldBytes.add(serialize);
//            }
//            final List<byte[]> hmget = redisConnection.hmget(keyBytes, queryFieldBytes.toArray(new byte[][]{}));
//            for (int i = 0; i < queryFields.length; i++) {
//                Object value = hashValueSerializer.deserialize(hmget.get(i),classloader);
//                hashKeyScanResult.getData().put(queryFields[i],value);
//            }
//            return hashKeyScanResult;
//        }
//
//        // scan 查询
//        int limit = hashKeyScanParam.getLimit();
//        ScanParams scanParams = new ScanParams();
//        scanParams.match(hashKeyScanParam.getPattern()).count(limit);
//        String cursor = hashKeyScanParam.getCursor();
//        List<byte[]> keys = new ArrayList<>();
//        do {
//            ScanResult<Map.Entry<byte[], byte[]>> hscan = redisConnection.hscan(keyBytes, cursor.getBytes(), scanParams);
//            List<Map.Entry<byte[], byte[]>> hscanResult = hscan.getResult();
//            for (Map.Entry<byte[], byte[]> stringStringEntry : hscanResult) {
//                byte[] hashKey = stringStringEntry.getKey();
//                keys.add(hashKey);
//            }
//
//            cursor = hscan.getStringCursor();
//            scanParams.count(limit - hscanResult.size());
//        } while (keys.size() < limit && NumberUtils.toLong(cursor) != 0L);
//
//        List<String> fields = new ArrayList<>();
//        for (byte[] bytes : keys) {
//            Object deserialize = hashKeySerializer.deserialize(bytes, classloader);
//            fields.add(Objects.toString(deserialize));
//        }
//
//        HashKeyScanResult hashKeyScanResult = new HashKeyScanResult(hashKeyScanParam.getKey(),fields, cursor);
//        hashKeyScanResult.setCursor(cursor);
//        if ("0".equals(cursor)) {
//            hashKeyScanResult.setFinish(true);
//        }
//
//        List<byte[]> fieldsBytes = new ArrayList<>();
//        for (String field : fields) {
//            fieldsBytes.add(hashKeySerializer.serialize(field));
//        }
//        if(CollectionUtils.isNotEmpty(fieldsBytes)) {
//            final List<byte[]> hmget = redisConnection.hmget(keyBytes, fieldsBytes.toArray(new byte[][]{}));
//            for (int i = 0; i < fieldsBytes.size(); i++) {
//                String key = Objects.toString(hashKeySerializer.deserialize(fieldsBytes.get(i), classloader));
//                Object value = hashValueSerializer.deserialize(hmget.get(i), classloader);
//                hashKeyScanResult.getData().put(key, value);
//            }
//        }
//        return hashKeyScanResult;
//    }
//
//    /**
//     * 查询除 hash 类型外的其它数据
//     * @param valueParam
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    public Object data(ValueParam valueParam) throws IOException, ClassNotFoundException {
//        final ConnParam connParam = valueParam.getConnParam();
//        final String key = valueParam.getKey();
//        final SerializerParam serializerParam = valueParam.getSerializerParam();
//        final ValueParam.RangeParam rangeParam = valueParam.getRangeParam();
//        final BaseKeyScanParam keyScanParam = valueParam.getKeyScanParam();
//        final ClassLoader classloader = classloaderService.getClassloader(serializerParam.getClassloaderName());
//        final Serializer keySerializer = serializerChoseService.choseSerializer(serializerParam.getKeySerializer());
//        Serializer valueSerializer = serializerChoseService.choseSerializer(serializerParam.getValue());
//        Serializer hashKeySerializer = serializerChoseService.choseSerializer(serializerParam.getHashKey());
//        Serializer hashValueSerializer = serializerChoseService.choseSerializer(serializerParam.getHashValue());
//
//        final RedisConnection redisConnection = redisConnection(connParam);
//        final byte[] keyBytes = keySerializer.serialize(key);
//
//        final RedisType redisType = redisConnection.type(keyBytes);
//        switch (redisType){
//            case string:
//                final byte[] bytes = redisConnection.get(keyBytes);
//                return valueSerializer.deserialize(bytes,classloader);
//            case List:
//                if (valueParam.isAll()){
//                    rangeParam.setEnable(true);
//                    rangeParam.setStop(redisConnection.llen(keyBytes));
//                }
//                final List<byte[]> lrange = redisConnection.lrange(keyBytes, rangeParam.getStart(), rangeParam.getStop());
//                List<Object> values = new ArrayList<>(lrange.size());
//                for (byte[] valueByte : lrange) {
//                    final Object deserialize = valueSerializer.deserialize(valueByte, classloader);
//                    values.add(deserialize);
//                }
//                return values;
//            case Set:
//                if (valueParam.isAll()){
//                    final Set<byte[]> smembers = redisConnection.smembers(keyBytes);
//                    List<Object> setValues = new ArrayList<>(smembers.size());
//                    for (byte[] smember : smembers) {
//                        final Object deserialize = valueSerializer.deserialize(smember, classloader);
//                        setValues.add(deserialize);
//                    }
//                    return setValues;
//                }
//
//                String setCursor = keyScanParam.getCursor();
//                final int setLimit = keyScanParam.getLimit();
//                ScanParams setScanParams = new ScanParams();
//                setScanParams.match(keyScanParam.getPattern()).count(setLimit);
//                List<byte[]> smembers = new ArrayList<>();
//                do {
//                    ScanResult<byte[]> scan = redisConnection.sscan(keyBytes, setCursor.getBytes(), setScanParams);
//                    List<byte[]> subScan = scan.getResult();
//                    for (byte[] setValue : subScan) {
//                        smembers.add(setValue);
//                    }
//
//                    setCursor = scan.getStringCursor();
//                    setScanParams.count(setLimit - subScan.size());
//                } while (smembers.size() < setLimit && NumberUtils.toLong(setCursor) != 0L);
//
//                List<Object> members = new ArrayList<>();
//                for (byte[] smember : smembers) {
//                    final Object deserialize = valueSerializer.deserialize(smember, classloader);
//                    members.add(deserialize);
//                }
//                return new SetScanResult(members,setCursor);
//            case ZSet:
//                if (valueParam.isAll()){
//                    rangeParam.setEnable(true);
//                    rangeParam.setStop(redisConnection.zcard(keyBytes));
//                }
//                if (rangeParam != null && rangeParam.isEnable()){
//                    Set<Tuple> tuples = redisConnection.zrangeWithScores(keyBytes, rangeParam.getStart(), rangeParam.getStop());
//                    return mapperToCustomTuple(classloader, valueSerializer, tuples);
//                }
//                final ValueParam.ScoreRangeParam scoreRangeParam = valueParam.getScoreRangeParam();
//                if (scoreRangeParam != null && scoreRangeParam.isEnable()){
//                    Set<Tuple> tuples = redisConnection.zrangeByScoreWithScores(keyBytes, scoreRangeParam.getMin(), scoreRangeParam.getMax());
//                    return mapperToCustomTuple(classloader, valueSerializer, tuples);
//                }
//
//                String cursor = keyScanParam.getCursor();
//                final int limit = keyScanParam.getLimit();
//                ScanParams scanParams = new ScanParams();
//                scanParams.match(keyScanParam.getPattern()).count(limit);
//
//                Set<Tuple> tuples = new HashSet<>();
//                do {
//                    ScanResult<Tuple> zscan = redisConnection.zscan(keyBytes, cursor.getBytes(), scanParams);
//                    List<Tuple> subScan = zscan.getResult();
//                    tuples.addAll(subScan);
//
//                    cursor = zscan.getStringCursor();
//                    scanParams.count(limit - subScan.size());
//                }while (tuples.size() < limit && NumberUtils.toLong(cursor) != 0L);
//
//                final List<ZSetTuple> zSetTuples = mapperToCustomTuple(classloader, valueSerializer, tuples);
//                return new ZSetScanResult(zSetTuples,cursor);
//            default:
//        }
//
//        log.info("暂时还不支持当前类型的数据获取或者序列化方式错误:{}",key);
//        throw new ToolException("暂时还不支持当前类型的数据获取或者序列化方式错误:"+key);
//    }
//
//
//    /**
//     * 映射成自定义的 tuple
//     * @param classloader
//     * @param valueSerializer
//     * @param tuples
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    private List<ZSetTuple> mapperToCustomTuple(ClassLoader classloader, Serializer valueSerializer, Set<Tuple> tuples) throws IOException, ClassNotFoundException {
//        List<ZSetTuple> zSetTuples = new ArrayList<>(tuples.size());
//        for (Tuple tuple : tuples) {
//            final ZSetTuple zSetTuple = new ZSetTuple(tuple, valueSerializer, classloader);
//            zSetTuples.add(zSetTuple);
//        }
//        return zSetTuples;
//    }
//
//    private KeyScanResult nodeScan(KeyScanParam redisScanParam, SerializerParam serializerParam, RedisNode redisNode) throws IOException, ClassNotFoundException {
//        Set<byte[]> keys = new HashSet<>();
//
//        // 开始搜索 key
//        final Jedis jedis = redisNode.browerJedis();
//        try{
//            int limit = redisScanParam.getLimit();
//            String cursor = redisScanParam.getCursor();
//
//            ScanParams scanParams = new ScanParams();
//            scanParams.count(limit);
//            if(StringUtils.isNotBlank(redisScanParam.getPattern())) {
//                scanParams.match(redisScanParam.getPattern());
//            }
//
//            // key 的序列化和类加载器
//            String serializerParamKey = serializerParam.getKeySerializer();
//            String classloaderName = serializerParam.getClassloaderName();
//            Serializer serializer = serializerChoseService.choseSerializer(serializerParamKey);
//            ClassLoader classloader = classloaderService.getClassloader(classloaderName);
//
//            // 统计搜索用时,如超时, 直接结束
//            long startSearchTime = System.currentTimeMillis();
//            do {
//                if (redisScanParam.isFast()){
//                    // 如果是快速搜索,不需要考虑每次的搜索数量 ,一次查找 1 万条数据
//                    scanParams.count(10000);
//                }
//
//                ScanResult scanResult = jedis.scan(cursor.getBytes(), scanParams);
//                List<byte[]> result = scanResult.getResult();
//                for (byte[] bytes : result) {
//                    keys.add(bytes);
//                }
//                cursor = scanResult.getStringCursor();
//                scanParams.count(limit - keys.size());
//
//                if (redisScanParam.getTimeout() != -1 ){
//                    if (System.currentTimeMillis() - startSearchTime > redisScanParam.getTimeout()){
//                        log.warn("当前搜索超时,可能是查找的 pattern[{}] 并不存在,在连接 [{}] ", redisScanParam.getPattern(), redisNode.getMark());
//                        break;
//                    }
//                }
//            }while (keys.size() < limit && NumberUtils.toLong(cursor) != 0L );
//
//            // 获取各 key 的属性信息
//            List<KeyScanResult.KeyResult> keyWraps = new ArrayList<>();
//            for (byte[] key : keys) {
//                String type = jedis.type(key);
//                Long ttl = jedis.ttl(key);
//                Long pttl = jedis.pttl(key);
//                long length = keyLength(jedis, key);
//                String keySerializer = Objects.toString(serializer.deserialize(key, classloader));
//                KeyScanResult.KeyResult keyResult = new KeyScanResult.KeyResult(keySerializer, type, ttl, pttl, length);
//                int slot = JedisClusterCRC16.getSlot(key);
//                keyResult.setSlot(slot);
//                keyWraps.add(keyResult);
//            }
//
//            KeyScanResult keyScanResult = new KeyScanResult(keyWraps, cursor, redisNode.getId());
//            if ("0".equals(cursor)){
//                keyScanResult.setFinish(true);
//            }
//
//            return keyScanResult;
//        }finally {
//            jedis.close();
//        }
//
//    }
//
//    /**
//     * 获取 key 长度
//     * @param client
//     * @param keyBytes
//     * @return
//     */
//    public long keyLength(Jedis client, byte[] keyBytes) {
//        String type = client.type(keyBytes);
//        RedisType redisType = RedisType.parse(type);
//        if (redisType == null){
//            return 0 ;
//        }
//        switch (redisType){
//            case string:
//                return client.strlen(keyBytes);
//            case Set:
//                return client.scard(keyBytes);
//            case ZSet:
//                return client.zcard(keyBytes);
//            case List:
//                return client.llen(keyBytes);
//            case Hash:
//                return client.hlen(keyBytes);
//            default:
//        }
//
//        return 0;
//    }
//
////    @PostConstruct
////    public void register(){
////        pluginManager.register(PluginDto.builder().module("monitor").name(MODULE).logo("redis.jpg").desc("Redis 数据查看(不再维护,请使用分模块的 Redis 工具),集群信息管理").help("Redis.md").author("sanri").envs("default").build());
////        pluginManager.register(PluginDto.builder().module("monitor").name(MODULE +"2").logo("redis.jpg").desc("Redis 数据查看,集群信息管理,分模块的 Redis 工具").help("Redis.md").author("sanri").envs("default").build());
////    }
//
//    @PreDestroy
//    public void destory(){
//        log.info("清除 {} 客户端列表:{}", MODULE, clientMap.keySet());
//        Iterator<RedisConnection> iterator = clientMap.values().iterator();
//        while (iterator.hasNext()){
//            final RedisConnection redisConnection = iterator.next();
//            try {
//                redisConnection.close();
//            }catch (Exception e){}
//        }
//    }
//
//    @Override
//    public void onApplicationEvent(UpdateConnectEvent updateConnectEvent) {
//        UpdateConnectEvent.ConnectInfo connectInfo = (UpdateConnectEvent.ConnectInfo) updateConnectEvent.getSource();
//        if (connectInfo.getClazz() == RedisConnectParam.class) {
//            String connName = connectInfo.getConnName();
//            final RedisConnection redisConnection = clientMap.remove(connName);
//            if (redisConnection != null) {
//                redisConnection.close();
//            }
//            log.info("[{}]模块[{}]配置变更,将移除存储的元数据信息", MODULE,connName);
//        }
//    }
//
//    /**
//     * 获取 redis 连接
//     * @param connParam
//     * @return
//     */
//    public RedisConnection redisConnection(ConnParam connParam) throws IOException {
//        final String connName = connParam.getConnName();
//        final int index = connParam.getIndex();
//
//        if (clientMap.containsKey(connName)){
//            final RedisConnection redisConnection = clientMap.get(connName);
//            if (redisConnection.getRunMode() != RedisRunMode.cluster){
//                redisConnection.select(index);
//            }
//            return redisConnection;
//        }
//
//        // 创建连接
//        final RedisConnectParam redisConnectParam = (RedisConnectParam) connectService.readConnParams(MODULE,connName);
//        final RedisConnection redisConnection = new RedisConnection();
//        try {
//            redisConnection.refresh(redisConnectParam);
//        }catch (JedisConnectionException e){
//            Throwable cause = e;Throwable parent = e;
//            int deep = 0 ;
//            while ((cause = cause.getCause()) != null){
//                parent = cause;
//                if (++deep > 10){
//                    break;
//                }
//            }
//            if (parent instanceof SocketTimeoutException){
//                // 连接超时, 直接删除当前连接
//                final RedisConnection remove = clientMap.remove(connName);
//                try {
//                    remove.close();
//                }catch (Exception e2){
//                    log.error("关闭连接时异常:{}",e2.getMessage());
//                }
//                log.error("连接 {} 网络超时,将移除连接",connName);
//                throw new ToolException("连接 "+connName+" 网络超时,将移除此连接,刷新可以重新加载此连接");
//            }
//            throw e;
//        }
//        redisConnection.select(index);
//        clientMap.put(connName,redisConnection);
//
//        return redisConnection;
//    }
//
//    public Object collectionMethods(ConnParam connParam, String[] members, String command, SerializerParam serializerParam) throws IOException, ClassNotFoundException {
//        final RedisConnection redisConnection = redisConnection(connParam);
//
//        Serializer choseSerializer = serializerChoseService.choseSerializer(serializerParam.getValue());
//        ClassLoader classloader = classloaderService.getClassloader(serializerParam.getClassloaderName());
//
//        byte [][] keyBytes = new byte [members.length][];
//        for (int i = 0; i < members.length; i++) {
//            keyBytes[i] = choseSerializer.serialize(members[i]);
//        }
//
//        Set<byte[]> result = new HashSet<>();
//        switch (command){
//            case "inter":
//                result = redisConnection.sinter(keyBytes);
//                break;
//            case "diff":
//                result = redisConnection.sdiff(keyBytes);
//                break;
//            case "union":
//                result = redisConnection.sunion(keyBytes);
//                break;
//            default:
//        }
//
//        Set<Object> collect = new HashSet<>();
//        for (byte[] bytes : result) {
//            Object deserialize = choseSerializer.deserialize(bytes, classloader);
//            collect.add(deserialize);
//        }
//        return collect;
//    }
//
//    public Long hdel(DelFieldsParam delFieldsParam) throws IOException {
//        final ConnParam connParam = delFieldsParam.getConnParam();
//        final RedisConnection redisConnection = redisConnection(connParam);
//
//        final SerializerParam serializerParam = delFieldsParam.getSerializerParam();
//        Serializer keySerializer = serializerChoseService.choseSerializer(serializerParam.getKeySerializer());
//        Serializer hashKeySerializer = serializerChoseService.choseSerializer(serializerParam.getHashKey());
//        ClassLoader classloader = classloaderService.getClassloader(serializerParam.getClassloaderName());
//
//        final byte[] key = keySerializer.serialize(delFieldsParam.getKey());
//        byte[][] fields = new byte[delFieldsParam.getFields().size()][];
//        for (int i = 0; i < delFieldsParam.getFields().size(); i++) {
//            fields[i] = hashKeySerializer.serialize(delFieldsParam.getFields().get(i));
//        }
//
//        return redisConnection.hdel(key,fields);
//    }
//
//    public void flushdb(ConnParam connParam) throws IOException {
//        final RedisConnection redisConnection = redisConnection(connParam);
//        final RedisRunMode runMode = redisConnection.getRunMode();
//        if (runMode == RedisRunMode.cluster){
//            throw new ToolException("不支持集群模式的 flushdb");
//        }
//        final Jedis jedis = redisConnection.getMasterNode().browerJedis();
//        try{
//            jedis.flushDB();
//        }finally {
//            jedis.close();
//        }
//    }
//
//    public void flushall(ConnParam connParam) throws IOException {
//        final RedisConnection redisConnection = redisConnection(connParam);
//        final RedisRunMode runMode = redisConnection.getRunMode();
//        if (runMode == RedisRunMode.cluster){
//            throw new ToolException("不支持集群模式的 flushdb");
//        }
//        final Jedis jedis = redisConnection.getMasterNode().browerJedis();
//        try{
//            jedis.flushAll();
//        }finally {
//            jedis.close();
//        }
//    }
//}
