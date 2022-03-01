package com.taotao.cloud.sys.biz.tools.kafka.service;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.sys.biz.tools.core.dtos.param.KafkaConnectParam;
import com.taotao.cloud.sys.biz.tools.core.service.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.core.utils.NetUtil;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.DataConsumerParam;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.KafkaData;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.NearbyDataConsumerParam;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.PartitionKafkaData;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.SendJsonDataParam;
import com.taotao.cloud.sys.biz.tools.kafka.dtos.SendObjectDataParam;
import com.taotao.cloud.sys.biz.tools.serializer.service.Serializer;
import com.taotao.cloud.sys.biz.tools.serializer.service.SerializerChoseService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 用于消费 kafka 数据的
 */
@Service
public class KafkaDataService {
    @Autowired
    private SerializerChoseService serializerChoseService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private ClassloaderService classloaderService;
    @Autowired
    private ConnectService connectService;
    @Autowired
    private FileManager fileManager;

    /**
     * 对主题消费数据建立的索引进行查询
     * @param keyword
     */
    public List<PartitionKafkaData> topicDataIndexQuery(String keyword) throws IOException, DecoderException, ClassNotFoundException {
        String remoteAddr = NetUtil.remoteAddr();
        File dir = fileManager.mkTmpDir("indices/" + remoteAddr);
        if (dir.listFiles().length == 0){
            return null;
        }

        List<PartitionKafkaData> datas = new ArrayList<>();

        MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
        Directory directory = new SimpleFSDirectory(dir);
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Serializer serializer = serializerChoseService.choseSerializer("jdk");
        int numDocs = indexReader.numDocs();
        SortField sortField = new SortField("timestamp", SortField.Type.LONG,true);
        TopDocs search = indexSearcher.search(matchAllDocsQuery, numDocs,new Sort(sortField));
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;
            Document doc = indexSearcher.doc(docId);
            int partition = doc.getField("partition").numericValue().intValue();
            long offset = doc.getField("offset").numericValue().longValue();
            long timestamp = doc.getField("timestamp").numericValue().longValue();
            int dataConvert = doc.getField("dataConvert").numericValue().intValue();
            String dataStringValue = doc.getField("data").stringValue();

            if (StringUtils.isNotBlank(keyword) &&  !dataStringValue.contains(keyword)){
                continue;
            }
            Object data = null;
            switch (dataConvert){
                case 1:
                    data = dataStringValue;
                    break;
                case 2:
                    data = JSON.parse(dataStringValue);
                    break;
                case 3:
                    data = serializer.deserialize(Hex.decodeHex(dataStringValue),ClassLoader.getSystemClassLoader());
                    break;
                default:
            }
            datas.add(new PartitionKafkaData(offset,data,timestamp,partition));
        }

        indexReader.close();
        return datas;
    }

    /**
     * 模仿 kafkatool 的功能, 索引搜索数据
     * 消费某一个主题的附近数据,然后建立索引方便快速查询
     * 对于每个 ip 来说会建立一个单独的存储索引的位置
     * @param dataConsumerParam
     */
    public void consumerDataAndCreateIndex(DataConsumerParam dataConsumerParam) throws IOException, ExecutionException, InterruptedException, ClassNotFoundException {
        String remoteAddr = NetUtil.remoteAddr();
        File dir = fileManager.mkTmpDir("indices/" + remoteAddr);
        // 如果目录存在,清空目录, 重建索引
        FileUtils.cleanDirectory(dir);

        String clusterName = dataConsumerParam.getClusterName();
        String topic = dataConsumerParam.getTopic();

        // 查询有多少分区
        int partitions = kafkaService.partitions(clusterName, topic);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
        for (int i = 0; i < partitions; i++) {
            TopicPartition topicPartition = new TopicPartition(topic, i);
            topicPartitions.add(topicPartition);
            partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
        }

        // 消费主题数据
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        log.debug("clusterName:[{}],topic:[{}]开始加载数据,每个分区准备加载[{}] 条",clusterName,topic,dataConsumerParam.getPerPartitionSize());
        List<PartitionKafkaData> kafkaData = loadData(clusterName, topicPartitions, partitionOffsetsMap, dataConsumerParam.getClassloaderName(), dataConsumerParam.getSerializer());

        // 建立数据索引
        stopWatch.stop();
        log.debug("clusterName:[{}],topic:[{}],查询到数据量[{}],用时[{} ms],开始在目录[{}]建立索引",clusterName,topic,kafkaData.size(),stopWatch.getTime(),dir);
        Directory directory = new SimpleFSDirectory(dir);
        Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_42);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        // 使用 jdk 序列化写入索引
        Serializer serializer = serializerChoseService.choseSerializer("jdk");

        for (PartitionKafkaData kafkaDatum : kafkaData) {
            Document doc = new Document();

            Object data = kafkaDatum.getData();
            int dataConvert = 1;            // 1 是原始数据, 2 是 json 转出来的 , 3 是 hex 数据
            String textData = "";
            if (data instanceof String){
                textData = (String) data;
            }else {
                try {
                    textData = JSON.toJSONString(data);
                    dataConvert = 2;
                } catch (Exception e) {
                    Hex.encodeHexString(serializer.serialize(textData));
                    dataConvert = 3;
                }
            }

            doc.add(new IntField("partition", kafkaDatum.getPartition(), Field.Store.YES));
            doc.add(new LongField("offset", kafkaDatum.getOffset(), Field.Store.YES));
            doc.add(new LongField("timestamp", kafkaDatum.getTimestamp(), Field.Store.YES));
            doc.add(new TextField("data", textData, Field.Store.YES));
            doc.add(new IntField("dataConvert",dataConvert, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.commit();
        writer.close();
    }


    /**
     * 加载一条数据,用于数据模拟
     * @param dataConsumerParam
     * @return
     */
    public KafkaData onlyOneData(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, IOException, ClassNotFoundException {
        String clusterName = dataConsumerParam.getClusterName();
        String topic = dataConsumerParam.getTopic();

        int partitions = kafkaService.partitions(clusterName, topic);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
        for (int i = 0; i < partitions; i++) {
            TopicPartition topicPartition = new TopicPartition(topic, i);
            topicPartitions.add(topicPartition);
            partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,1));
        }
        List<PartitionKafkaData> kafkaData = loadData(clusterName, topicPartitions, partitionOffsetsMap, dataConsumerParam.getClassloaderName(), dataConsumerParam.getSerializer());
        if (CollectionUtils.isNotEmpty(kafkaData)){
            return kafkaData.get(0);
        }
        return null;
    }

    /**
     * 消费主题最后面的数据
     * @param clusterName
     * @param topic
     * @param partition -1 时取全部分区
     * @param perPartitionSize
     * @param classloaderName
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public List<PartitionKafkaData> lastDatas(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, IOException, ClassNotFoundException {
        int partition = dataConsumerParam.getPartition();
        String clusterName = dataConsumerParam.getClusterName();
        String topic = dataConsumerParam.getTopic();

        Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
        List<TopicPartition> topicPartitions = new ArrayList<>();
        if (partition == -1){
            int partitions = kafkaService.partitions(clusterName, topic);
            for (int i = 0; i < partitions; i++) {
                TopicPartition topicPartition = new TopicPartition(topic, i);
                topicPartitions.add(topicPartition);
                partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
            }
        }else{
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            topicPartitions.add(topicPartition);
            partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
        }

        return loadData(clusterName,topicPartitions,partitionOffsetsMap,dataConsumerParam.getClassloaderName(),dataConsumerParam.getSerializer());
    }

    /**
     * 消费消费组主题附近的数据
     * @param clusterName
     * @param topic
     * @param partition
     * @param offset
     * @param fetchSize 查询前后多少条
     * @param serializer
     * @param classloaderName
     * @return
     */
    public List<PartitionKafkaData> nearbyDatas(NearbyDataConsumerParam nearbyDataConsumerParam) throws IOException, ClassNotFoundException {
        String clusterName = nearbyDataConsumerParam.getClusterName();
        String topic = nearbyDataConsumerParam.getTopic();
        int partition = nearbyDataConsumerParam.getPartition();
        int perPartitionSize = nearbyDataConsumerParam.getPerPartitionSize();

        List<TopicPartition> topicPartitions = new ArrayList<>();
        Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        topicPartitions.add(topicPartition);
        partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,nearbyDataConsumerParam.getPerPartitionSize()));

        List<PartitionKafkaData> datas = loadData(clusterName, topicPartitions, partitionOffsetsMap, nearbyDataConsumerParam.getClassloaderName(), nearbyDataConsumerParam.getSerializer());
        return datas;
    }

    /**
     * 从哪个 offset 开始加载, 加载数量是多少
     */
    public static class PartitionOffset{
        private TopicPartition topicPartition;
        private long offset = -1;
        private long loadSize;

        public PartitionOffset(TopicPartition topicPartition, long loadSize) {
            this.topicPartition = topicPartition;
            this.loadSize = loadSize;
        }

        public PartitionOffset(TopicPartition topicPartition, long offset, long loadSize) {
            this.topicPartition = topicPartition;
            this.offset = offset;
            this.loadSize = loadSize;
        }

	    public TopicPartition getTopicPartition() {
		    return topicPartition;
	    }

	    public void setTopicPartition(TopicPartition topicPartition) {
		    this.topicPartition = topicPartition;
	    }

	    public long getOffset() {
		    return offset;
	    }

	    public void setOffset(long offset) {
		    this.offset = offset;
	    }

	    public long getLoadSize() {
		    return loadSize;
	    }

	    public void setLoadSize(long loadSize) {
		    this.loadSize = loadSize;
	    }
    }

    private List<PartitionKafkaData> loadData(String clusterName,List<TopicPartition> topicPartitions,Map<TopicPartition,PartitionOffset> partitionOffsets,String classloaderName,String serializer) throws IOException, ClassNotFoundException {
        // 获取序列化工具和类加载器
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        if(classloader == null){classloader = ClassLoader.getSystemClassLoader();}
        Serializer choseSerializer = serializerChoseService.choseSerializer(serializer);

        // 加载消费者
        KafkaConsumer<byte[], byte[]> consumer = kafkaService.loadConsumerClient(clusterName);
        List<PartitionKafkaData> datas = new ArrayList<>();

        final int loadTimes = 5; // 加载 5 次, 每次加载 20ms
        long perLoadTimeInMillis =  20 ;

        try {
            consumer.assign(topicPartitions);
            Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(topicPartitions);
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
            Iterator<Map.Entry<TopicPartition, Long>> iterator = beginningOffsets.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<TopicPartition, Long> entry = iterator.next();
                TopicPartition key = entry.getKey();

                Long beginningOffset = entry.getValue();
                Long endOffset = endOffsets.get(key);
                if (endOffset.equals(beginningOffset)){
                    log.warn("主题-分区[{}-{}]目前无数据可消费",key.topic(),key.partition());
                    continue;
                }

                PartitionOffset partitionOffset = partitionOffsets.get(key);
                final long offset = partitionOffset.getOffset();
                final long loadSize = partitionOffset.getLoadSize();
                long seekOffsetComputed = -1;long loadSizeComputed = -1;
                if (offset == -1){          // 表示消费尾部数据
                    seekOffsetComputed = endOffset - loadSize;
                    if (seekOffsetComputed < beginningOffset){
                        seekOffsetComputed = beginningOffset;
                    }
                    loadSizeComputed = endOffset - seekOffsetComputed;
                }else{
                    // 消费 offset 附近数据 , 对 loadSize 对半 ,取前一半数据,后一半数据
                    long half = loadSize / 2;
                    seekOffsetComputed = offset - half;
                    if (seekOffsetComputed < beginningOffset){
                        seekOffsetComputed = beginningOffset;
                    }
                    long seekEndOffset = offset + half;
                    if (seekEndOffset > endOffset){
                        seekEndOffset = endOffset;
                    }
                    loadSizeComputed = seekEndOffset - seekOffsetComputed;
                }

                consumer.seek(key,seekOffsetComputed);
                int currentLoadTimes = loadTimes;
                // 根据数据量计算加载时间 15 + 数据量 * 0.8 最大 100ms
                perLoadTimeInMillis = 15 + Math.round(loadSizeComputed * 0.8);
                if (perLoadTimeInMillis > 100){perLoadTimeInMillis = 100;}
                log.info("开始加载 [{}-{}] 的数据,加载 [{}] 次 , 初始加载时长 [{} ms],从[{}->{}]开始加载,加载数量[{}->{}]",key.topic(),key.partition(),loadTimes,perLoadTimeInMillis,offset,seekOffsetComputed,loadSize,loadSizeComputed);

                while (currentLoadTimes--> 0 && loadSizeComputed > 0){
                    log.info("第 [{}] 次加载 [{}-{}] 的数据,还剩[{}] 条数据,当前加载时间[{} ms]",(5 - currentLoadTimes),key.topic(),key.partition(),loadSizeComputed,perLoadTimeInMillis);
                    ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(perLoadTimeInMillis));
                    Iterator<ConsumerRecord<byte[], byte[]>> consumerRecordIterator = consumerRecords.iterator();
                    long currentLoadCount = 0 ;
                    while (consumerRecordIterator.hasNext()){
                        ConsumerRecord<byte[], byte[]> consumerRecord = consumerRecordIterator.next();
                        byte[] value = consumerRecord.value();
                        Object deserialize = choseSerializer.deserialize(value, classloader);
                        PartitionKafkaData partitionKafkaData = new PartitionKafkaData(consumerRecord.offset(), deserialize, consumerRecord.timestamp(), consumerRecord.partition());
                        datas.add(partitionKafkaData);
                        currentLoadCount ++;
                    }

                    loadSizeComputed -= currentLoadCount;
                    if (currentLoadCount == 0){
                        // 动态修改加载时间,微调 , 每次增长 1.2 倍
                        perLoadTimeInMillis = Math.round(perLoadTimeInMillis * 1.2);
                    }else{
                        // 本次查询有加载数据,则以此为基础计算剩余数据的加载时间
                        perLoadTimeInMillis = Math.round(perLoadTimeInMillis / currentLoadCount ) * loadSizeComputed;
                    }
                }

                if (loadSizeComputed != 0){
                    log.warn("[{}-{}] 剩余 [{}] 条数据加载失败,加载 [{}] 次, 最后一次加载时长 [{} ms]",key.topic(),key.partition(),loadSizeComputed,loadTimes,perLoadTimeInMillis);
                }
            }
        }finally {
            if(consumer != null) {
                consumer.close();
            }
        }

        //数据排序
        Collections.sort(datas);
        return datas;
    }

    /**
     * 发送数据到 kafka , 这里只支持 json 数据
     *
     * @param sendJsonDataParam@return
     */
    public void sendJsonData(SendJsonDataParam sendJsonDataParam) throws IOException, ExecutionException, InterruptedException {
        String clusterName = sendJsonDataParam.getClusterName();

//        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(KafkaService.MODULE,clusterName);
        final KafkaConnectParam kafkaConnectParam = kafkaService.convertToKafkaConnectParam(clusterName);
        Map<String, Object> properties = kafkaConnectParam.getKafka().buildProducerProperties();
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        ProducerRecord producerRecord = new ProducerRecord<>(sendJsonDataParam.getTopic(), sendJsonDataParam.getKey(), sendJsonDataParam.getData());
        Future send = kafkaProducer.send(producerRecord);
        send.get();     //阻塞，直到发送成功
        kafkaProducer.close();
    }

    /**
     * 发送对象数据
     * @param sendObjectDataParam
     */
    public void sendObjectData(SendObjectDataParam sendObjectDataParam) throws ClassNotFoundException, IOException, ExecutionException, InterruptedException {
        ClassLoader classloader = classloaderService.getClassloader(sendObjectDataParam.getClassloaderName());
        if(classloader == null){classloader = ClassLoader.getSystemClassLoader();}
        Class<?> clazz = classloader.loadClass(sendObjectDataParam.getClassName());
        Object object = JSON.parseObject(sendObjectDataParam.getData(), clazz);
        Serializer serializerChose = serializerChoseService.choseSerializer(sendObjectDataParam.getSerializer());
        byte[] serialize = serializerChose.serialize(object);

        String clusterName = sendObjectDataParam.getClusterName();

        final KafkaConnectParam kafkaConnectParam = kafkaService.convertToKafkaConnectParam(clusterName);
//        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(KafkaService.MODULE,clusterName);
        Map<String, Object> properties = kafkaConnectParam.getKafka().buildProducerProperties();
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        ProducerRecord producerRecord = new ProducerRecord<>(sendObjectDataParam.getTopic(), sendObjectDataParam.getKey(), serialize);
        Future send = kafkaProducer.send(producerRecord);
        send.get();     //阻塞，直到发送成功
        kafkaProducer.close();
    }
}
