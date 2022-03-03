package com.taotao.cloud.sys.biz.controller.tools;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KafkaController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:52:39
 */
@Validated
@RestController
@Tag(name = "工具管理端-kafka管理API", description = "工具管理端-kafka管理API")
@RequestMapping("/sys/tools/kafka")
public class KafkaController {

	//@Autowired
	//private IKafkaService kafkaService;
	//
	//YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
	//
	//@PostMapping("/topic/create")
	//public void createTopic(@NotNull String clusterName, @NotNull String topic, @Positive int partitions,@Positive int replication) throws InterruptedException, ExecutionException, IOException {
	//    kafkaService.createTopic(clusterName,topic,partitions,replication);
	//}
	//
	//@PostMapping("/topic/delete")
	//public void deleteTopic(@NotNull String clusterName,@NotNull String topic) throws InterruptedException, ExecutionException, IOException {
	//    kafkaService.deleteTopic(clusterName,topic);
	//}
	//
	//@GetMapping("/topics")
	//public List<TopicInfo> listTopic(@NotNull String clusterName) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.topics(clusterName);
	//}
	//
	//@GetMapping("/topic/partitions")
	//public int topicPartitions(@NotNull String clusterName,@NotNull String topic) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.partitions(clusterName,topic);
	//}
	//
	//@GetMapping("/topic/logSize")
	//public List<TopicLogSize> topicLogSize(@NotNull String clusterName, @NotNull String topic) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.logSizes(clusterName,topic);
	//}
	//
	//@GetMapping("/topic/data/last")
	//public List<PartitionKafkaData> topicLastDatas(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
	//    return kafkaDataService.lastDatas(dataConsumerParam);
	//}
	//
	//@GetMapping("/topic/data/consumerDataAndCreateIndex")
	//public void consumerDataAndCreateIndex(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
	//    kafkaDataService.consumerDataAndCreateIndex(dataConsumerParam);
	//}
	//
	//@GetMapping("/topic/data/search")
	//public List<PartitionKafkaData> topicDataIndexQuery(String keyword) throws IOException, DecoderException, ClassNotFoundException {
	//    return kafkaDataService.topicDataIndexQuery(keyword);
	//}
	//
	//@GetMapping("/topic/data/one")
	//public KafkaData topicOnlyOneData(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
	//    return kafkaDataService.onlyOneData(dataConsumerParam);
	//}
	//
	//@PostMapping("/topic/data/send/json")
	//public void topicSendJsonData(@RequestBody SendJsonDataParam sendJsonDataParam) throws InterruptedException, ExecutionException, IOException {
	//    kafkaDataService.sendJsonData(sendJsonDataParam);
	//}
	//
	//@PostMapping("/topic/data/send")
	//public void topicSendData(@RequestBody SendObjectDataParam sendObjectDataParam) throws ClassNotFoundException, ExecutionException, InterruptedException, IOException {
	//    kafkaDataService.sendObjectData(sendObjectDataParam);
	//}
	//
	//@GetMapping("/groups")
	//public List<String> listGroups(String clusterName) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.groups(clusterName);
	//}
	//
	//@PostMapping("/group/delete")
	//public void deleteGroup(String clusterName,String group) throws InterruptedException, ExecutionException, IOException {
	//    kafkaService.deleteGroup(clusterName,group);
	//}
	//
	//@GetMapping("/group/topics")
	//public Set<String> groupSubscribeTopics(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.groupSubscribeTopics(clusterName,group);
	//}
	//
	//@GetMapping("/group/topic/data/nearby")
	//public List<PartitionKafkaData> groupTopicNearbyData(
	//    NearbyDataConsumerParam nearbyDataConsumerParam) throws IOException, ClassNotFoundException {
	//    return kafkaDataService.nearbyDatas(nearbyDataConsumerParam);
	//}
	//
	//@GetMapping("/group/subscribes")
	//public ConsumerGroupInfo consumerGroupInfo(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.consumerGroupInfo(clusterName,group);
	//}
	//
	//@GetMapping("/group/topic/offset")
	//public List<OffsetShow> groupTopicConsumerInfo(String clusterName, String group, String topic) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.groupTopicConsumerInfo(clusterName,group,topic);
	//}
	//
	//@GetMapping("/group/topics/offset")
	//public List<TopicOffset> groupTopicConsumerInfos(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
	//    return kafkaService.groupTopicConsumerInfos(clusterName,group);
	//}
	//
	//@GetMapping("/brokers")
	//public List<String> brokers(@NotNull String clusterName) throws IOException {
	//    List<BrokerInfo> brokers = kafkaService.brokers(clusterName);
	//    List<String> collect = brokers.stream().map(BrokerInfo::hostAndPort).collect(Collectors.toList());
	//    return collect;
	//}
	//
	//@GetMapping("/monitor/topic/{topic}")
	//public Collection<MBeanMonitorInfo> topicMonitor(@NotNull String clusterName, @PathVariable("topic") String topic) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
	//    Collection<MBeanMonitorInfo> monitor = kafkaService.monitor(clusterName, BrokerTopicMetrics.TopicMetrics.class, topic);
	//    return monitor;
	//}
	//
	//@GetMapping("/monitor/broker")
	//public Collection<MBeanMonitorInfo> brokerMonitor(@NotNull String clusterName) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
	//    Collection<MBeanMonitorInfo> monitor = kafkaService.monitor(clusterName, BrokerTopicMetrics.BrokerMetrics.class,null);
	//    return monitor;
	//}
}
