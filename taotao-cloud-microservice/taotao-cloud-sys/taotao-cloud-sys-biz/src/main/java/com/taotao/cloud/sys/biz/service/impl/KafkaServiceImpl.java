package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.service.IKafkaService;
import org.springframework.stereotype.Service;


/**
 * kafka 主题和消费组管理
 */
@Service
public class KafkaServiceImpl implements IKafkaService {
//    @Autowired
//    private ConnectService connectService;
//    @Autowired
//    private IZookeeperService zookeeperService;
//
//    private YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
//
//    public static final String MODULE = "kafka";
//
//    private static final Map<String, AdminClient> adminClientMap = new ConcurrentHashMap<>();
//
//	@Autowired
//	private SerializerChoseService serializerChoseService;
//	@Autowired
//	private IKafkaService kafkaService;
//	@Autowired
//	private ClassloaderService classloaderService;
//	@Autowired
//	private ConnectService connectService;
//	@Autowired
//	private FileManager fileManager;
//
//	/**
//	 * 对主题消费数据建立的索引进行查询
//	 * @param keyword
//	 */
//	public List<PartitionKafkaData> topicDataIndexQuery(String keyword) throws IOException, DecoderException, ClassNotFoundException {
//		String remoteAddr = NetUtil.remoteAddr();
//		File dir = fileManager.mkTmpDir("indices/" + remoteAddr);
//		if (dir.listFiles().length == 0){
//			return null;
//		}
//
//		List<PartitionKafkaData> datas = new ArrayList<>();
//
//		MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
//		Directory directory = new SimpleFSDirectory(dir);
//		IndexReader indexReader = DirectoryReader.open(directory);
//		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//
//		Serializer serializer = serializerChoseService.choseSerializer("jdk");
//		int numDocs = indexReader.numDocs();
//		SortField sortField = new SortField("timestamp", SortField.Type.LONG,true);
//		TopDocs search = indexSearcher.search(matchAllDocsQuery, numDocs,new Sort(sortField));
//		ScoreDoc[] scoreDocs = search.scoreDocs;
//		for (ScoreDoc scoreDoc : scoreDocs) {
//			int docId = scoreDoc.doc;
//			Document doc = indexSearcher.doc(docId);
//			int partition = doc.getField("partition").numericValue().intValue();
//			long offset = doc.getField("offset").numericValue().longValue();
//			long timestamp = doc.getField("timestamp").numericValue().longValue();
//			int dataConvert = doc.getField("dataConvert").numericValue().intValue();
//			String dataStringValue = doc.getField("data").stringValue();
//
//			if (StringUtils.isNotBlank(keyword) &&  !dataStringValue.contains(keyword)){
//				continue;
//			}
//			Object data = null;
//			switch (dataConvert){
//				case 1:
//					data = dataStringValue;
//					break;
//				case 2:
//					data = JSON.parse(dataStringValue);
//					break;
//				case 3:
//					data = serializer.deserialize(Hex.decodeHex(dataStringValue),ClassLoader.getSystemClassLoader());
//					break;
//				default:
//			}
//			datas.add(new PartitionKafkaData(offset,data,timestamp,partition));
//		}
//
//		indexReader.close();
//		return datas;
//	}
//
//	/**
//	 * 模仿 kafkatool 的功能, 索引搜索数据
//	 * 消费某一个主题的附近数据,然后建立索引方便快速查询
//	 * 对于每个 ip 来说会建立一个单独的存储索引的位置
//	 * @param dataConsumerParam
//	 */
//	public void consumerDataAndCreateIndex(DataConsumerParam dataConsumerParam) throws IOException, ExecutionException, InterruptedException, ClassNotFoundException {
//		String remoteAddr = NetUtil.remoteAddr();
//		File dir = fileManager.mkTmpDir("indices/" + remoteAddr);
//		// 如果目录存在,清空目录, 重建索引
//		FileUtils.cleanDirectory(dir);
//
//		String clusterName = dataConsumerParam.getClusterName();
//		String topic = dataConsumerParam.getTopic();
//
//		// 查询有多少分区
//		int partitions = kafkaService.partitions(clusterName, topic);
//		List<TopicPartition> topicPartitions = new ArrayList<>();
//		Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
//		for (int i = 0; i < partitions; i++) {
//			TopicPartition topicPartition = new TopicPartition(topic, i);
//			topicPartitions.add(topicPartition);
//			partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
//		}
//
//		// 消费主题数据
//		StopWatch stopWatch = new StopWatch();stopWatch.start();
//		log.debug("clusterName:[{}],topic:[{}]开始加载数据,每个分区准备加载[{}] 条",clusterName,topic,dataConsumerParam.getPerPartitionSize());
//		List<PartitionKafkaData> kafkaData = loadData(clusterName, topicPartitions, partitionOffsetsMap, dataConsumerParam.getClassloaderName(), dataConsumerParam.getSerializer());
//
//		// 建立数据索引
//		stopWatch.stop();
//		log.debug("clusterName:[{}],topic:[{}],查询到数据量[{}],用时[{} ms],开始在目录[{}]建立索引",clusterName,topic,kafkaData.size(),stopWatch.getTime(),dir);
//		Directory directory = new SimpleFSDirectory(dir);
//		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_42);
//		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
//		IndexWriter writer = new IndexWriter(directory, config);
//
//		// 使用 jdk 序列化写入索引
//		Serializer serializer = serializerChoseService.choseSerializer("jdk");
//
//		for (PartitionKafkaData kafkaDatum : kafkaData) {
//			Document doc = new Document();
//
//			Object data = kafkaDatum.getData();
//			int dataConvert = 1;            // 1 是原始数据, 2 是 json 转出来的 , 3 是 hex 数据
//			String textData = "";
//			if (data instanceof String){
//				textData = (String) data;
//			}else {
//				try {
//					textData = JSON.toJSONString(data);
//					dataConvert = 2;
//				} catch (Exception e) {
//					Hex.encodeHexString(serializer.serialize(textData));
//					dataConvert = 3;
//				}
//			}
//
//			doc.add(new IntField("partition", kafkaDatum.getPartition(), Field.Store.YES));
//			doc.add(new LongField("offset", kafkaDatum.getOffset(), Field.Store.YES));
//			doc.add(new LongField("timestamp", kafkaDatum.getTimestamp(), Field.Store.YES));
//			doc.add(new TextField("data", textData, Field.Store.YES));
//			doc.add(new IntField("dataConvert",dataConvert, Field.Store.YES));
//			writer.addDocument(doc);
//		}
//		writer.commit();
//		writer.close();
//	}
//
//
//	/**
//	 * 加载一条数据,用于数据模拟
//	 * @param dataConsumerParam
//	 * @return
//	 */
//	public KafkaData onlyOneData(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, IOException, ClassNotFoundException {
//		String clusterName = dataConsumerParam.getClusterName();
//		String topic = dataConsumerParam.getTopic();
//
//		int partitions = kafkaService.partitions(clusterName, topic);
//		List<TopicPartition> topicPartitions = new ArrayList<>();
//		Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
//		for (int i = 0; i < partitions; i++) {
//			TopicPartition topicPartition = new TopicPartition(topic, i);
//			topicPartitions.add(topicPartition);
//			partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,1));
//		}
//		List<PartitionKafkaData> kafkaData = loadData(clusterName, topicPartitions, partitionOffsetsMap, dataConsumerParam.getClassloaderName(), dataConsumerParam.getSerializer());
//		if (CollectionUtils.isNotEmpty(kafkaData)){
//			return kafkaData.get(0);
//		}
//		return null;
//	}
//
//	/**
//	 * 消费主题最后面的数据
//	 * @param clusterName
//	 * @param topic
//	 * @param partition -1 时取全部分区
//	 * @param perPartitionSize
//	 * @param classloaderName
//	 * @return
//	 * @throws InterruptedException
//	 * @throws ExecutionException
//	 * @throws IOException
//	 */
//	public List<PartitionKafkaData> lastDatas(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, IOException, ClassNotFoundException {
//		int partition = dataConsumerParam.getPartition();
//		String clusterName = dataConsumerParam.getClusterName();
//		String topic = dataConsumerParam.getTopic();
//
//		Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
//		List<TopicPartition> topicPartitions = new ArrayList<>();
//		if (partition == -1){
//			int partitions = kafkaService.partitions(clusterName, topic);
//			for (int i = 0; i < partitions; i++) {
//				TopicPartition topicPartition = new TopicPartition(topic, i);
//				topicPartitions.add(topicPartition);
//				partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
//			}
//		}else{
//			TopicPartition topicPartition = new TopicPartition(topic, partition);
//			topicPartitions.add(topicPartition);
//			partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,dataConsumerParam.getPerPartitionSize()));
//		}
//
//		return loadData(clusterName,topicPartitions,partitionOffsetsMap,dataConsumerParam.getClassloaderName(),dataConsumerParam.getSerializer());
//	}
//
//	/**
//	 * 消费消费组主题附近的数据
//	 * @param clusterName
//	 * @param topic
//	 * @param partition
//	 * @param offset
//	 * @param fetchSize 查询前后多少条
//	 * @param serializer
//	 * @param classloaderName
//	 * @return
//	 */
//	public List<PartitionKafkaData> nearbyDatas(NearbyDataConsumerParam nearbyDataConsumerParam) throws IOException, ClassNotFoundException {
//		String clusterName = nearbyDataConsumerParam.getClusterName();
//		String topic = nearbyDataConsumerParam.getTopic();
//		int partition = nearbyDataConsumerParam.getPartition();
//		int perPartitionSize = nearbyDataConsumerParam.getPerPartitionSize();
//
//		List<TopicPartition> topicPartitions = new ArrayList<>();
//		Map<TopicPartition,PartitionOffset> partitionOffsetsMap = new HashMap<>();
//		TopicPartition topicPartition = new TopicPartition(topic, partition);
//		topicPartitions.add(topicPartition);
//		partitionOffsetsMap.put(topicPartition,new PartitionOffset(topicPartition,nearbyDataConsumerParam.getPerPartitionSize()));
//
//		List<PartitionKafkaData> datas = loadData(clusterName, topicPartitions, partitionOffsetsMap, nearbyDataConsumerParam.getClassloaderName(), nearbyDataConsumerParam.getSerializer());
//		return datas;
//	}
//
//	/**
//	 * 从哪个 offset 开始加载, 加载数量是多少
//	 */
//	public static class PartitionOffset{
//		private TopicPartition topicPartition;
//		private long offset = -1;
//		private long loadSize;
//
//		public PartitionOffset(TopicPartition topicPartition, long loadSize) {
//			this.topicPartition = topicPartition;
//			this.loadSize = loadSize;
//		}
//
//		public PartitionOffset(TopicPartition topicPartition, long offset, long loadSize) {
//			this.topicPartition = topicPartition;
//			this.offset = offset;
//			this.loadSize = loadSize;
//		}
//
//		public TopicPartition getTopicPartition() {
//			return topicPartition;
//		}
//
//		public void setTopicPartition(TopicPartition topicPartition) {
//			this.topicPartition = topicPartition;
//		}
//
//		public long getOffset() {
//			return offset;
//		}
//
//		public void setOffset(long offset) {
//			this.offset = offset;
//		}
//
//		public long getLoadSize() {
//			return loadSize;
//		}
//
//		public void setLoadSize(long loadSize) {
//			this.loadSize = loadSize;
//		}
//	}
//
//	private List<PartitionKafkaData> loadData(String clusterName,List<TopicPartition> topicPartitions,Map<TopicPartition,PartitionOffset> partitionOffsets,String classloaderName,String serializer) throws IOException, ClassNotFoundException {
//		// 获取序列化工具和类加载器
//		ClassLoader classloader = classloaderService.getClassloader(classloaderName);
//		if(classloader == null){classloader = ClassLoader.getSystemClassLoader();}
//		Serializer choseSerializer = serializerChoseService.choseSerializer(serializer);
//
//		// 加载消费者
//		KafkaConsumer<byte[], byte[]> consumer = kafkaService.loadConsumerClient(clusterName);
//		List<PartitionKafkaData> datas = new ArrayList<>();
//
//		final int loadTimes = 5; // 加载 5 次, 每次加载 20ms
//		long perLoadTimeInMillis =  20 ;
//
//		try {
//			consumer.assign(topicPartitions);
//			Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(topicPartitions);
//			Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
//			Iterator<Map.Entry<TopicPartition, Long>> iterator = beginningOffsets.entrySet().iterator();
//			while (iterator.hasNext()){
//				Map.Entry<TopicPartition, Long> entry = iterator.next();
//				TopicPartition key = entry.getKey();
//
//				Long beginningOffset = entry.getValue();
//				Long endOffset = endOffsets.get(key);
//				if (endOffset.equals(beginningOffset)){
//					log.warn("主题-分区[{}-{}]目前无数据可消费",key.topic(),key.partition());
//					continue;
//				}
//
//				PartitionOffset partitionOffset = partitionOffsets.get(key);
//				final long offset = partitionOffset.getOffset();
//				final long loadSize = partitionOffset.getLoadSize();
//				long seekOffsetComputed = -1;long loadSizeComputed = -1;
//				if (offset == -1){          // 表示消费尾部数据
//					seekOffsetComputed = endOffset - loadSize;
//					if (seekOffsetComputed < beginningOffset){
//						seekOffsetComputed = beginningOffset;
//					}
//					loadSizeComputed = endOffset - seekOffsetComputed;
//				}else{
//					// 消费 offset 附近数据 , 对 loadSize 对半 ,取前一半数据,后一半数据
//					long half = loadSize / 2;
//					seekOffsetComputed = offset - half;
//					if (seekOffsetComputed < beginningOffset){
//						seekOffsetComputed = beginningOffset;
//					}
//					long seekEndOffset = offset + half;
//					if (seekEndOffset > endOffset){
//						seekEndOffset = endOffset;
//					}
//					loadSizeComputed = seekEndOffset - seekOffsetComputed;
//				}
//
//				consumer.seek(key,seekOffsetComputed);
//				int currentLoadTimes = loadTimes;
//				// 根据数据量计算加载时间 15 + 数据量 * 0.8 最大 100ms
//				perLoadTimeInMillis = 15 + Math.round(loadSizeComputed * 0.8);
//				if (perLoadTimeInMillis > 100){perLoadTimeInMillis = 100;}
//				log.info("开始加载 [{}-{}] 的数据,加载 [{}] 次 , 初始加载时长 [{} ms],从[{}->{}]开始加载,加载数量[{}->{}]",key.topic(),key.partition(),loadTimes,perLoadTimeInMillis,offset,seekOffsetComputed,loadSize,loadSizeComputed);
//
//				while (currentLoadTimes--> 0 && loadSizeComputed > 0){
//					log.info("第 [{}] 次加载 [{}-{}] 的数据,还剩[{}] 条数据,当前加载时间[{} ms]",(5 - currentLoadTimes),key.topic(),key.partition(),loadSizeComputed,perLoadTimeInMillis);
//					ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(perLoadTimeInMillis));
//					Iterator<ConsumerRecord<byte[], byte[]>> consumerRecordIterator = consumerRecords.iterator();
//					long currentLoadCount = 0 ;
//					while (consumerRecordIterator.hasNext()){
//						ConsumerRecord<byte[], byte[]> consumerRecord = consumerRecordIterator.next();
//						byte[] value = consumerRecord.value();
//						Object deserialize = choseSerializer.deserialize(value, classloader);
//						PartitionKafkaData partitionKafkaData = new PartitionKafkaData(consumerRecord.offset(), deserialize, consumerRecord.timestamp(), consumerRecord.partition());
//						datas.add(partitionKafkaData);
//						currentLoadCount ++;
//					}
//
//					loadSizeComputed -= currentLoadCount;
//					if (currentLoadCount == 0){
//						// 动态修改加载时间,微调 , 每次增长 1.2 倍
//						perLoadTimeInMillis = Math.round(perLoadTimeInMillis * 1.2);
//					}else{
//						// 本次查询有加载数据,则以此为基础计算剩余数据的加载时间
//						perLoadTimeInMillis = Math.round(perLoadTimeInMillis / currentLoadCount ) * loadSizeComputed;
//					}
//				}
//
//				if (loadSizeComputed != 0){
//					log.warn("[{}-{}] 剩余 [{}] 条数据加载失败,加载 [{}] 次, 最后一次加载时长 [{} ms]",key.topic(),key.partition(),loadSizeComputed,loadTimes,perLoadTimeInMillis);
//				}
//			}
//		}finally {
//			if(consumer != null) {
//				consumer.close();
//			}
//		}
//
//		//数据排序
//		Collections.sort(datas);
//		return datas;
//	}
//
//	/**
//	 * 发送数据到 kafka , 这里只支持 json 数据
//	 *
//	 * @param sendJsonDataParam@return
//	 */
//	public void sendJsonData(SendJsonDataParam sendJsonDataParam) throws IOException, ExecutionException, InterruptedException {
//		String clusterName = sendJsonDataParam.getClusterName();
//
////        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(KafkaService.MODULE,clusterName);
//		final KafkaConnectParam kafkaConnectParam = kafkaService.convertToKafkaConnectParam(clusterName);
//		Map<String, Object> properties = kafkaConnectParam.getKafka().buildProducerProperties();
//		properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//		properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//		KafkaProducer kafkaProducer = new KafkaProducer(properties);
//		ProducerRecord producerRecord = new ProducerRecord<>(sendJsonDataParam.getTopic(), sendJsonDataParam.getKey(), sendJsonDataParam.getData());
//		Future send = kafkaProducer.send(producerRecord);
//		send.get();     //阻塞，直到发送成功
//		kafkaProducer.close();
//	}
//
//	/**
//	 * 发送对象数据
//	 * @param sendObjectDataParam
//	 */
//	public void sendObjectData(SendObjectDataParam sendObjectDataParam) throws ClassNotFoundException, IOException, ExecutionException, InterruptedException {
//		ClassLoader classloader = classloaderService.getClassloader(sendObjectDataParam.getClassloaderName());
//		if(classloader == null){classloader = ClassLoader.getSystemClassLoader();}
//		Class<?> clazz = classloader.loadClass(sendObjectDataParam.getClassName());
//		Object object = JSON.parseObject(sendObjectDataParam.getData(), clazz);
//		Serializer serializerChose = serializerChoseService.choseSerializer(sendObjectDataParam.getSerializer());
//		byte[] serialize = serializerChose.serialize(object);
//
//		String clusterName = sendObjectDataParam.getClusterName();
//
//		final KafkaConnectParam kafkaConnectParam = kafkaService.convertToKafkaConnectParam(clusterName);
////        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(KafkaService.MODULE,clusterName);
//		Map<String, Object> properties = kafkaConnectParam.getKafka().buildProducerProperties();
//		properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//		properties.put("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
//		KafkaProducer kafkaProducer = new KafkaProducer(properties);
//		ProducerRecord producerRecord = new ProducerRecord<>(sendObjectDataParam.getTopic(), sendObjectDataParam.getKey(), serialize);
//		Future send = kafkaProducer.send(producerRecord);
//		send.get();     //阻塞，直到发送成功
//		kafkaProducer.close();
//	}
//
//
//    /**
//     * 读取 brokers 信息
//     * @param clusterName
//     * @return
//     * @throws IOException
//     */
//    public List<BrokerInfo> brokers(String clusterName) throws IOException {
////        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(MODULE, clusterName);
//        KafkaConnectParam kafkaConnectParam = convertToKafkaConnectParam(clusterName);
//        String chroot = kafkaConnectParam.getChroot();
//        List<BrokerInfo> brokerInfos = readZookeeperBrokers(clusterName, chroot);
//        return brokerInfos;
//    }
//
//    /**
//     * 加载连接信息, 并转换成 KafkaConnectParam
//     * @param clusterName
//     * @return
//     * @throws IOException
//     */
//    KafkaConnectParam convertToKafkaConnectParam(String clusterName) throws IOException {
//        final String loadContent = connectService.loadContent(MODULE, clusterName);
//        ByteArrayResource byteArrayResource = new ByteArrayResource(loadContent.getBytes(StandardCharsets.UTF_8));
//        final List<PropertySource<?>> load = yamlPropertySourceLoader.load("a", byteArrayResource);
//        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(load);
//        Binder binder = new Binder(from);
//        BindResult<KafkaConnectParam> bind = binder.bind("", KafkaConnectParam.class);
//        KafkaConnectParam kafkaConnectParam = bind.get();
//        return kafkaConnectParam;
//    }
//
//    /**
//     * 创建主题
//     * @param clusterName
//     * @param topic
//     * @param partitions
//     * @param replication
//     * @return
//     */
//    public void createTopic(String clusterName,String topic,int partitions,int replication) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        NewTopic newTopic = new NewTopic(topic,partitions,(short)replication);
//        CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singletonList(newTopic));
//        KafkaFuture<Void> voidKafkaFuture = createTopicsResult.values().get(topic);
//        voidKafkaFuture.get();
//    }
//
//    /**
//     * 删除主题
//     * @param clusterName
//     * @param topic
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public void deleteTopic(String clusterName,String topic) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topic));
//        deleteTopicsResult.all().get();
//    }
//
//    /**
//     * 所有主题查询
//     *
//     * @return
//     */
//    public List<TopicInfo> topics(String clusterName) throws IOException, ExecutionException, InterruptedException {
//        List<TopicInfo> topicInfos = new ArrayList<>();
//
//        AdminClient adminClient = loadAdminClient(clusterName);
//
//        ListTopicsResult listTopicsResult = adminClient.listTopics();
//        Set<String> topics = listTopicsResult.names().get();
//        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(topics);
//        Map<String, KafkaFuture<TopicDescription>> values = describeTopicsResult.values();
//        Iterator<Map.Entry<String, KafkaFuture<TopicDescription>>> iterator = values.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, KafkaFuture<TopicDescription>> topicDescriptionEntry = iterator.next();
//            String topic = topicDescriptionEntry.getKey();
//            TopicDescription topicDescription = topicDescriptionEntry.getValue().get();
//
//            // 复制数据,因为 TopicDescription 没有 getset
//            TopicInfo topicInfo = new TopicInfo(topicDescription.name(), topicDescription.isInternal());
//            topicInfos.add(topicInfo);
//            List<TopicPartitionInfo> partitions = topicDescription.partitions();
//            for (TopicPartitionInfo partition : partitions) {
//                Node leader = partition.leader();
//                BrokerInfo leaderBroker = null;
//                if (leader != null){
//                    leaderBroker = new BrokerInfo(leader.id(), leader.host(), leader.port());
//                }else{
//                    log.warn("主题[{}],分区[{}] 的 leader 为空",topic,partition.partition());
//                }
//
//                List<Node> replicas = partition.replicas();
//                List<BrokerInfo> replicaBrokers = new ArrayList<>();
//                for (Node replica : replicas) {
//                    replicaBrokers.add(new BrokerInfo(replica.id(), replica.host(), replica.port()));
//                }
//
//                List<Node> isr = partition.isr();
//                List<BrokerInfo> isrBrokers = new ArrayList<>();
//                for (Node node : isr) {
//                    isrBrokers.add(new BrokerInfo(node.id(), node.host(), node.port()));
//                }
//                TopicInfo.TopicPartitionInfo partitionInfo = new TopicInfo.TopicPartitionInfo(partition.partition(), leaderBroker, replicaBrokers, isrBrokers);
//                topicInfo.addPartitionInfo(partitionInfo);
//            }
//        }
//        return topicInfos;
//    }
//
//    /**
//     * 查询某个主题的分区数
//     * @param clusterName
//     * @param topic
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public int partitions(String clusterName, String topic) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(topic));
//        TopicDescription topicDescription = describeTopicsResult.values().get(topic).get();
//        return topicDescription.partitions().size();
//    }
//
//    /**
//     * 查询所有分组
//     * @param clusterName
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public List<String> groups(String clusterName) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        List<String> groupNames = new ArrayList<>();
//
//        ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();
//        Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all().get();
//        for (ConsumerGroupListing consumerGroupListing : consumerGroupListings) {
//            String groupId = consumerGroupListing.groupId();
//            groupNames.add(groupId);
//        }
//        return groupNames;
//    }
//
//    /**
//     * 删除消费组
//     * @param clusterName
//     * @param group
//     * @return
//     */
//    public void deleteGroup(String clusterName,String group) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        DeleteConsumerGroupsResult deleteConsumerGroupsResult = adminClient.deleteConsumerGroups(Collections.singletonList(group));
//        deleteConsumerGroupsResult.all().get();
//    }
//
//    /**
//     * 查询分组订阅的主题列表
//     * @param clusterName
//     * @param group
//     * @return
//     * @throws IOException
//     */
//    public Set<String> groupSubscribeTopics(String clusterName, String group) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        Set<String> subscribeTopics = new HashSet<>();
//
//        DescribeConsumerGroupsResult describeConsumerGroupsResult = adminClient.describeConsumerGroups(Collections.singletonList(group));
//        ConsumerGroupDescription consumerGroupDescription = describeConsumerGroupsResult.describedGroups().get(group).get();
//        // 这里应该可以看到这个分区分配给哪个了
//        String partitionAssignor = consumerGroupDescription.partitionAssignor();
//        ConsumerGroupState state = consumerGroupDescription.state();
//
//        Collection<MemberDescription> members = consumerGroupDescription.members();
//        for (MemberDescription member : members) {
//            MemberAssignment assignment = member.assignment();
//            Set<TopicPartition> topicPartitions = assignment.topicPartitions();
//            Iterator<TopicPartition> iterator = topicPartitions.iterator();
//            while (iterator.hasNext()){
//                TopicPartition topicPartition = iterator.next();
//                subscribeTopics.add(topicPartition.topic());
//            }
//        }
//        return subscribeTopics;
//    }
//
//    /**
//     * 消费组详情查询, 包含分区策略, 当前的组协调器,和每一个主机分到的消费分区
//     * @param clusterName
//     * @param group
//     * @return
//     * @throws IOException
//     */
//    public ConsumerGroupInfo consumerGroupInfo(String clusterName, String group) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//
//        DescribeConsumerGroupsResult describeConsumerGroupsResult = adminClient.describeConsumerGroups(Collections.singletonList(group));
//        Map<String, KafkaFuture<ConsumerGroupDescription>> stringKafkaFutureMap = describeConsumerGroupsResult.describedGroups();
//        ConsumerGroupDescription consumerGroupDescription = stringKafkaFutureMap.get(group).get();
//
//        Node coordinator = consumerGroupDescription.coordinator();
//        BrokerInfo coordinatorBroker = new BrokerInfo(coordinator.id(),coordinator.host(),coordinator.port());
//        String partitionAssignor = consumerGroupDescription.partitionAssignor();
//        ConsumerGroupInfo consumerGroupInfo = new ConsumerGroupInfo(coordinatorBroker,partitionAssignor);
//
//        Map<String,Set<SimpleTopicPartition>> map = new HashMap<>();
//
//        Collection<MemberDescription> members = consumerGroupDescription.members();
//        for (MemberDescription member : members) {
//            String host = member.host();                // 需要加入这个,这样才能知道哪些主题的哪些分区在哪个主机上消费
//            MemberAssignment assignment = member.assignment();
//
//            Set<TopicPartition> topicPartitions = assignment.topicPartitions();
//            Set<SimpleTopicPartition> simpleTopicPartitions = new HashSet<>();
//            for (TopicPartition topicPartition : topicPartitions) {
//                SimpleTopicPartition simpleTopicPartition = new SimpleTopicPartition(topicPartition.topic(), topicPartition.partition());
//                simpleTopicPartitions.add(simpleTopicPartition);
//            }
//            Set<SimpleTopicPartition> existHostTopicPartitions = map.computeIfAbsent(host, k -> new HashSet<>());
//            existHostTopicPartitions.addAll(simpleTopicPartitions);
//        }
//        Iterator<Map.Entry<String, Set<SimpleTopicPartition>>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, Set<SimpleTopicPartition>> entry = iterator.next();
//            String host = entry.getKey();
//            Set<SimpleTopicPartition> simpleTopicPartitions = entry.getValue();
//            consumerGroupInfo.addMember(new ConsumerGroupInfo.MemberInfo(host,simpleTopicPartitions));
//        }
//
//        return consumerGroupInfo;
//    }
//
//    /**
//     * 消费组的所有的主题数据消费情况查询
//     * @param clusterName
//     * @param group
//     * @return
//     */
//    public List<TopicOffset> groupTopicConsumerInfos(String clusterName, String group) throws IOException, ExecutionException, InterruptedException {
//        AdminClient adminClient = loadAdminClient(clusterName);
//        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = adminClient.listConsumerGroupOffsets(group).partitionsToOffsetAndMetadata().get();
//        Set<TopicPartition> topicPartitions = topicPartitionOffsetAndMetadataMap.keySet();
//        KafkaConsumer<byte[], byte[]> kafkaConsumer = loadConsumerClient(clusterName);
//        Map<TopicPartition, Long> topicPartitionLongMap = kafkaConsumer.endOffsets(topicPartitions);
//
//        List<OffsetShow> offsetShows = new ArrayList<>();
//        Iterator<Map.Entry<TopicPartition, OffsetAndMetadata>> iterator = topicPartitionOffsetAndMetadataMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<TopicPartition, OffsetAndMetadata> offsetAndMetadataEntry = iterator.next();
//            TopicPartition topicPartition = offsetAndMetadataEntry.getKey();
//            OffsetAndMetadata offsetAndMetadata = offsetAndMetadataEntry.getValue();
//            Long logSize = topicPartitionLongMap.get(topicPartition);
//            OffsetShow offsetShow = new OffsetShow(topicPartition.topic(), topicPartition.partition(), offsetAndMetadata.offset(),logSize);
//            offsetShows.add(offsetShow);
//        }
//
//        Map<String, List<OffsetShow>> listMap = offsetShows.stream().collect(Collectors.groupingBy(OffsetShow::getTopic));
//
//        // 最后进行统计
//        List<TopicOffset> topicOffsets = new ArrayList<>();
//        Iterator<Map.Entry<String, List<OffsetShow>>> listMapIterator = listMap.entrySet().iterator();
//        while (listMapIterator.hasNext()){
//            Map.Entry<String, List<OffsetShow>> entry = listMapIterator.next();
//            String topic = entry.getKey();
//            List<OffsetShow> offsetShowList = entry.getValue();
//            TopicOffset topicOffset = new TopicOffset(group, topic,offsetShowList);
//
//            long totalLogSize = 0 , totalOffset = 0 ;
//            for (OffsetShow offsetShow : offsetShowList) {
//                long logSize = offsetShow.getLogSize();
//                long offset = offsetShow.getOffset();
//                totalLogSize += logSize;
//                totalOffset += offset;
//            }
//            topicOffset.setLogSize(totalLogSize);
//            topicOffset.setOffset(totalOffset);
//            topicOffset.setLag(totalLogSize - totalOffset);
//            topicOffsets.add(topicOffset);
//        }
//
//        return topicOffsets;
//    }
//
//
//    /**
//     * 消费组主题信息监控; 单个消费组内,单个主题消费情况的查询
//     * @param clusterName
//     * @param group
//     * @param topic
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public List<OffsetShow> groupTopicConsumerInfo(String clusterName, String group, String topic) throws IOException, ExecutionException, InterruptedException {
//        List<OffsetShow> offsetShows = new ArrayList<>();
//        AdminClient adminClient = loadAdminClient(clusterName);
//        KafkaConsumer<byte[], byte[]> consumer = loadConsumerClient(clusterName);
//
//        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(topic));
//        TopicDescription topicDescription = describeTopicsResult.values().get(topic).get();
//        List<TopicPartitionInfo> partitions = topicDescription.partitions();
//        List<TopicPartition> topicPartitions = new ArrayList<>();
//        for (TopicPartitionInfo partition : partitions) {
//            TopicPartition topicPartition = new TopicPartition(topic, partition.partition());
//            topicPartitions.add(topicPartition);
//        }
//
//        //查询  offset 信息
//        ListConsumerGroupOffsetsOptions listConsumerGroupOffsetsOptions = new ListConsumerGroupOffsetsOptions();
//        listConsumerGroupOffsetsOptions.topicPartitions(topicPartitions);
//
//        Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = adminClient.listConsumerGroupOffsets(group,listConsumerGroupOffsetsOptions).partitionsToOffsetAndMetadata().get();
//
//        Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(topicPartitions);
//        Map<TopicPartition, Long> beginningTopicPartitionLongMap = consumer.beginningOffsets(topicPartitions);
//        consumer.close();
//
//        Iterator<Map.Entry<TopicPartition, Long>> iterator = topicPartitionLongMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<TopicPartition, Long> entry = iterator.next();
//            TopicPartition topicPartition = entry.getKey();
//            Long minOffset = beginningTopicPartitionLongMap.get(topicPartition);
//            Long logSize = entry.getValue();
//            OffsetAndMetadata offsetAndMetadata = offsetAndMetadataMap.get(topicPartition);
//            long offset = offsetAndMetadata.offset();
//            int partition = topicPartition.partition();
//            long lag = logSize - offset;
//            OffsetShow offsetShow = new OffsetShow(topic, partition, offset, logSize);
//            offsetShow.setMinOffset(minOffset);
//            offsetShows.add(offsetShow);
//        }
//
//        Collections.sort(offsetShows);
//        return offsetShows;
//    }
//
//    /**
//     * 每一个分区的 logSize
//     * @param clusterName
//     * @param topic
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public List<TopicLogSize> logSizes(String clusterName, String topic) throws IOException, ExecutionException, InterruptedException {
//        List<TopicLogSize> topicLogSizes = new ArrayList<>();
//
//        KafkaConsumer<byte[], byte[]> consumer = loadConsumerClient(clusterName);
//
//        try {
//            // 这个方法巨慢,直接去 zk 上拿分区数据算了
////            KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(MODULE, clusterName);
//            KafkaConnectParam kafkaConnectParam = convertToKafkaConnectParam(clusterName);
//            int partitions = 0;
////            try {
////                partitions = zookeeperService.countChildren(clusterName, kafkaConnectParam.getChroot() + "/brokers/topics/" + topic + "/partitions");
////            }catch (Exception e){
////                log.error("[{}]从 zookeeper 上获取[{}]分区数出错,启用备用方案,从 kafka 上获取",clusterName,topic);
////                List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
////                partitions = partitionInfos.size();
////            }
//            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
//            partitions = partitionInfos.size();
//
//            List<TopicPartition> topicPartitions = new ArrayList<>();
//            for (int i = 0; i < partitions; i++) {
//                topicPartitions.add(new TopicPartition(topic, i));
//            }
//
//            Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(topicPartitions);
//            Map<TopicPartition, Long> beginningTopicPartitionLongMap = consumer.beginningOffsets(topicPartitions);
//            consumer.assign(topicPartitions);
//
//            int hasDataPartitions = 0 ;
//            Iterator<Map.Entry<TopicPartition, Long>> iterator = topicPartitionLongMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<TopicPartition, Long> entry = iterator.next();
//                TopicPartition topicPartition = entry.getKey();
//                Long minOffset = beginningTopicPartitionLongMap.get(topicPartition);
//                Long logSize = entry.getValue();
//
//                // 如果有数据,消费最后一条数据 ,主要得到最后一条数据时间
//                if (!logSize.equals(minOffset)){
//                    consumer.seek(topicPartition,logSize - 1);
//                    // 如果不相等，则一定有一条数据, 后面一定要获取到
//                    hasDataPartitions ++;
//                }
//
//                topicLogSizes.add(new TopicLogSize(topic,topicPartition.partition(),logSize,minOffset));
//            }
//
//            // 最后一条数据的更新时间,如果有这样的分区
//            int loadTimes = 5; // 加载 5 次,每次 10 ms 实在加载不到就放弃
//            Map<Integer, Long> partitionLastTime = new HashMap<>();
//            while (hasDataPartitions != 0 && loadTimes -- > 0) {
//                log.info("第 [{}] 次加载 [{}] 主题的分区最后一条数据 ",(5 - loadTimes),topic);
//                ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(20));
//                Iterator<ConsumerRecord<byte[], byte[]>> recordIterator = consumerRecords.iterator();
//                while (recordIterator.hasNext()) {
//                    ConsumerRecord<byte[], byte[]> consumerRecord = recordIterator.next();
//                    int partition = consumerRecord.partition();
//                    long timestamp = consumerRecord.timestamp();
//                    partitionLastTime.put(partition, timestamp);
//                    // 如果有一个分区加载到数据了
//                    hasDataPartitions--;
//                }
//            }
//            for (TopicLogSize topicLogSize : topicLogSizes) {
//                int partition = topicLogSize.getPartition();
//                Long timestamp = partitionLastTime.get(partition);
//                if (timestamp != null) {
//                    topicLogSize.setTimestamp(timestamp);
//                }
//            }
//        }finally {
//            if(consumer != null) {
//                consumer.close();
//            }
//        }
//        Collections.sort(topicLogSizes,(a,b) -> a.getPartition() - b.getPartition());
//        return topicLogSizes;
//    }
//
//    /**
//     * 加载一个消费客户端
//     * @param clusterName
//     * @return
//     * @throws IOException
//     */
//    public KafkaConsumer<byte[], byte[]> loadConsumerClient(String clusterName) throws IOException {
////        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(MODULE, clusterName);
//        final KafkaConnectParam kafkaConnectParam = convertToKafkaConnectParam(clusterName);
//        Map<String, Object> properties = kafkaConnectParam.getKafka().buildConsumerProperties();
//        // 设置为 byte[] 序列化
//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArrayDeserializer");
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArrayDeserializer");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"console-"+clusterName);
//        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
//        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"500");
////        properties.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG,"1000");
//        return new KafkaConsumer<byte[], byte[]>(properties);
//    }
//
//    public AdminClient loadAdminClient(String clusterName) throws IOException {
//        AdminClient adminClient = adminClientMap.get(clusterName);
//        if(adminClient == null){
////            KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(MODULE, clusterName);
//            final KafkaConnectParam kafkaConnectParam = convertToKafkaConnectParam(clusterName);
//            KafkaProperties kafka = kafkaConnectParam.getKafka();
//            Map<String, Object> kafkaProperties = kafka.buildAdminProperties();
//            log.info("kafka connect properties:\n {}",kafkaProperties);
//            adminClient = AdminClient.create(kafkaProperties);
//            adminClientMap.put(clusterName,adminClient);
//        }
//
//        return adminClient;
//    }
//
//    private static final String relativeBrokerPath = "/brokers/ids";
//    static Pattern ipPort = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)");
//    private List<BrokerInfo> readZookeeperBrokers(String connName,String chroot) throws IOException {
//        List<BrokerInfo> brokerInfos = new ArrayList<>();
//
//        List<String> childrens = zookeeperService.childrens(connName, chroot + relativeBrokerPath);
//        for (String children : childrens) {
//            String brokerInfo = Objects.toString(zookeeperService.readData(connName, chroot + relativeBrokerPath + "/" + children, "string"),"");
//            JSONObject brokerJson = JSONObject.parseObject(brokerInfo);
//            String host = brokerJson.getString("host");
//            int port = brokerJson.getIntValue("port");
//            int jmxPort = brokerJson.getIntValue("jmx_port");
//
//            if(StringUtils.isBlank(host)){
//                //如果没有提供 host 和 port 信息，则从 endpoints 中拿取信息
//                JSONArray endpoints = brokerJson.getJSONArray("endpoints");
//                String endpoint = endpoints.getString(0);
//                Matcher matcher = ipPort.matcher(endpoint);
//                if(matcher.find()) {
//                    host = matcher.group(1);
//                    port = NumberUtils.toInt(matcher.group(2));
//                }
//            }
//
//            brokerInfos.add(new BrokerInfo(NumberUtils.toInt(children),host,port,jmxPort));
//        }
//        return brokerInfos;
//    }
//
//    /**
//     *  kafka 的 mBean 数据监控
//     */
//    private static final String JMX = "service:jmx:rmi:///jndi/rmi://%s/jmxrmi";
//    public Collection<MBeanMonitorInfo> monitor(String clusterName, Class clazz, String topic) throws IOException, MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
////        KafkaConnectParam kafkaConnectParam = (KafkaConnectParam) connectService.readConnParams(MODULE, clusterName);
//        final KafkaConnectParam kafkaConnectParam = convertToKafkaConnectParam(clusterName);
//        List<BrokerInfo> brokers = readZookeeperBrokers(kafkaConnectParam.getConnectIdParam().getConnName(),kafkaConnectParam.getChroot());
//
//        List<MBeanMonitorInfo> mBeanInfos = new ArrayList<>();
//        for (BrokerInfo broker : brokers) {
//            String host = broker.getHost();
//            int jxmPort = broker.getJxmPort();
//            String uri = host+":"+jxmPort;
//            if(jxmPort == -1){
//                return null;
//            }
//
//            JMXServiceURL jmxSeriverUrl = new JMXServiceURL(String.format(JMX, uri));
//            JMXConnector connector = JMXConnectorFactory.connect(jmxSeriverUrl);
//            MBeanServerConnection mbeanConnection = connector.getMBeanServerConnection();
//
//            // 遍历所有的 mBean
//            Constants constants = new Constants(clazz);
//            List<String> mBeans = constansValues(constants);
//            try {
//                for (String mBean : mBeans) {
//                    if (clazz == BrokerTopicMetrics.TopicMetrics.class){
//                        mBean = String.format(mBean,topic);
//                    }
//                    Object fifteenMinuteRate = mbeanConnection.getAttribute(new ObjectName(mBean), BrokerTopicMetrics.MBean.FIFTEEN_MINUTE_RATE);
//                    Object fiveMinuteRate = mbeanConnection.getAttribute(new ObjectName(mBean), BrokerTopicMetrics.MBean.FIVE_MINUTE_RATE);
//                    Object meanRate = mbeanConnection.getAttribute(new ObjectName(mBean), BrokerTopicMetrics.MBean.MEAN_RATE);
//                    Object oneMinuteRate = mbeanConnection.getAttribute(new ObjectName(mBean), BrokerTopicMetrics.MBean.ONE_MINUTE_RATE);
//                    MBeanMonitorInfo mBeanInfo = new MBeanMonitorInfo(mBean,objectDoubleValue(fifteenMinuteRate), objectDoubleValue(fiveMinuteRate), objectDoubleValue(meanRate), objectDoubleValue(oneMinuteRate));
//                    mBeanInfos.add(mBeanInfo);
//                }
//            } catch (InstanceNotFoundException e) {
//                log.error("当前连接[{}]主题[{}]未找到监控实例 [{}]",clusterName,topic,e.getMessage());
//            }
//        }
//
//        // 数据合并
//        Map<String, MBeanMonitorInfo> mergeMap = new HashMap<>();
//        for (MBeanMonitorInfo mBeanInfo : mBeanInfos) {
//            String mBean = mBeanInfo.getmBean();
//            MBeanMonitorInfo mergeMBeanInfo = mergeMap.get(mBean);
//            if(mergeMBeanInfo == null){
//                mergeMBeanInfo = mBeanInfo;
//                mergeMap.put(mBean,mergeMBeanInfo);
//                continue;
//            }
//            mergeMBeanInfo.addData(mBeanInfo);
//        }
//
//        return mergeMap.values();
//    }
//
//    private BigDecimal objectDoubleValue(Object value){
//        return new BigDecimal(value.toString());
//    }
//
//    private List<String> constansValues(Constants constants) {
//        List<String> mMbeans = new ArrayList<>();
//        try {
//            Method getFieldCache = Constants.class.getDeclaredMethod("getFieldCache");
//            getFieldCache.setAccessible(true);
//            Map<String, Object> invokeMethod = (Map<String, Object>) ReflectionUtils.invokeMethod(getFieldCache, constants);
//            Collection<Object> values = invokeMethod.values();
//
//            for (Object value : values) {
//                mMbeans.add(Objects.toString(value));
//            }
//        } catch (NoSuchMethodException e) {}
//        return mMbeans;
//    }
//
////    @PostConstruct
////    public void register(){
////        pluginManager.register(PluginDto.builder().module("monitor").name("kafkaGroup").author("sanri").logo("kafka.jpg").desc("消费组管理").envs("default").build());
////        pluginManager.register(PluginDto.builder().module("monitor").name("kafkaTopic").author("sanri").logo("kafka.jpg").desc("消费主题管理").envs("default").build());
////    }
//
//    @PreDestroy
//    public void destory(){
//        log.info("清除 {} 客户端列表:{}", MODULE,adminClientMap.keySet());
//        Iterator<AdminClient> iterator = adminClientMap.values().iterator();
//        while (iterator.hasNext()){
//            AdminClient next = iterator.next();
//            if(next != null){
//                try {
//                    next.close();
//                } catch (Exception e) {}
//            }
//        }
//    }
//
//    /**
//     * 创建 kafka 连接,强依赖于 zookeeper
//     * @param kafkaConnectParam
//     */
//    public void createConnect(KafkaConnectParam kafkaConnectParam) throws IOException {
//        KafkaProperties kafka = kafkaConnectParam.getKafka();
//        String connName = kafkaConnectParam.getConnectIdParam().getConnName();
//        String chroot = kafkaConnectParam.getChroot();
//
//        List<String> bootstrapServers = kafka.getBootstrapServers();
//        if (bootstrapServers.size() == 1){
//            String brokerOnlyOne = bootstrapServers.get(0);
//            if ("localhost:9092".equals(brokerOnlyOne)){
//                // 如果是默认的,检查 zookeeper 上的节点,如果不一致,则取 zookeeper 上的节点数据
//                List<BrokerInfo> brokers = readZookeeperBrokers(connName,chroot);
//                if (brokers.size() == 0){
//                    throw new ToolException("zookeeper "+connName+" 上的 kafka 节点为空");
//                }
//
//                List<String> bootstrapServersZookeeper = brokers.stream().map(broker -> broker.getHost() + ":" + broker.getPort()).collect(Collectors.toList());
//                String bootstrapServersString = StringUtils.join(bootstrapServersZookeeper, ',');
//                if (!bootstrapServersString.equals(brokerOnlyOne)){
//                    kafka.setBootstrapServers(bootstrapServersZookeeper);
//                }
//            }
//        }
//
//        // 一些默认参数配置
//        KafkaProperties.Consumer consumer = kafka.getConsumer();
//        consumer.setGroupId("console-sanritools-"+connName);
//        consumer.setAutoOffsetReset("earliest");
//        consumer.setEnableAutoCommit(true);
//
//        // 然后调用 连接服务,将配置序列化
////        connectService.createConnect(MODULE, JSON.toJSONString(kafkaConnectParam));
//    }

}
