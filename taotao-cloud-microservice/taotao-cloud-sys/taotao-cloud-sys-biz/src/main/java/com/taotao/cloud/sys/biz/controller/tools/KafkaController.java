package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.biz.tools.core.dtos.param.KafkaConnectParam;
import com.taotao.cloud.sys.api.dto.kafka.BrokerInfo;
import com.taotao.cloud.sys.api.dto.kafka.BrokerTopicMetrics;
import com.taotao.cloud.sys.api.dto.kafka.ConsumerGroupInfo;
import com.taotao.cloud.sys.api.dto.kafka.DataConsumerParam;
import com.taotao.cloud.sys.api.dto.kafka.KafkaData;
import com.taotao.cloud.sys.api.dto.kafka.MBeanMonitorInfo;
import com.taotao.cloud.sys.api.dto.kafka.NearbyDataConsumerParam;
import com.taotao.cloud.sys.api.dto.kafka.OffsetShow;
import com.taotao.cloud.sys.api.dto.kafka.PartitionKafkaData;
import com.taotao.cloud.sys.api.dto.kafka.SendJsonDataParam;
import com.taotao.cloud.sys.api.dto.kafka.SendObjectDataParam;
import com.taotao.cloud.sys.api.dto.kafka.TopicInfo;
import com.taotao.cloud.sys.api.dto.kafka.TopicLogSize;
import com.taotao.cloud.sys.api.dto.kafka.TopicOffset;
import com.taotao.cloud.sys.biz.service.KafkaDataService;
import com.taotao.cloud.sys.biz.service.KafkaService;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kafka")
@Validated
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private KafkaDataService kafkaDataService;

    YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();

    /**
     * kafka 连接的创建需要依赖于 zookeeper
     */
    @PostMapping(value = "/connect/create",consumes = "application/yaml")
    public void createConnect(@RequestBody String yamlConfig) throws IOException {
        ByteArrayResource byteArrayResource = new ByteArrayResource(yamlConfig.getBytes());
        List<PropertySource<?>> load = yamlPropertySourceLoader.load("a",byteArrayResource);
        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(load);
        Binder binder = new Binder(from);
        BindResult<KafkaConnectParam> bind = binder.bind("", KafkaConnectParam.class);
        KafkaConnectParam kafkaConnectParam = bind.get();
        kafkaService.createConnect(kafkaConnectParam);
    }

    @PostMapping("/topic/create")
    public void createTopic(@NotNull String clusterName, @NotNull String topic, @Positive int partitions,@Positive int replication) throws InterruptedException, ExecutionException, IOException {
        kafkaService.createTopic(clusterName,topic,partitions,replication);
    }

    @PostMapping("/topic/delete")
    public void deleteTopic(@NotNull String clusterName,@NotNull String topic) throws InterruptedException, ExecutionException, IOException {
        kafkaService.deleteTopic(clusterName,topic);
    }

    @GetMapping("/topics")
    public List<TopicInfo> listTopic(@NotNull String clusterName) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.topics(clusterName);
    }

    @GetMapping("/topic/partitions")
    public int topicPartitions(@NotNull String clusterName,@NotNull String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.partitions(clusterName,topic);
    }

    @GetMapping("/topic/logSize")
    public List<TopicLogSize> topicLogSize(@NotNull String clusterName, @NotNull String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.logSizes(clusterName,topic);
    }

    @GetMapping("/topic/data/last")
    public List<PartitionKafkaData> topicLastDatas(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
        return kafkaDataService.lastDatas(dataConsumerParam);
    }

    @GetMapping("/topic/data/consumerDataAndCreateIndex")
    public void consumerDataAndCreateIndex(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
        kafkaDataService.consumerDataAndCreateIndex(dataConsumerParam);
    }

    @GetMapping("/topic/data/search")
    public List<PartitionKafkaData> topicDataIndexQuery(String keyword) throws IOException, DecoderException, ClassNotFoundException {
        return kafkaDataService.topicDataIndexQuery(keyword);
    }

    @GetMapping("/topic/data/one")
    public KafkaData topicOnlyOneData(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
        return kafkaDataService.onlyOneData(dataConsumerParam);
    }

    @PostMapping("/topic/data/send/json")
    public void topicSendJsonData(@RequestBody SendJsonDataParam sendJsonDataParam) throws InterruptedException, ExecutionException, IOException {
        kafkaDataService.sendJsonData(sendJsonDataParam);
    }

    @PostMapping("/topic/data/send")
    public void topicSendData(@RequestBody SendObjectDataParam sendObjectDataParam) throws ClassNotFoundException, ExecutionException, InterruptedException, IOException {
        kafkaDataService.sendObjectData(sendObjectDataParam);
    }

    @GetMapping("/groups")
    public List<String> listGroups(String clusterName) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groups(clusterName);
    }

    @PostMapping("/group/delete")
    public void deleteGroup(String clusterName,String group) throws InterruptedException, ExecutionException, IOException {
        kafkaService.deleteGroup(clusterName,group);
    }

    @GetMapping("/group/topics")
    public Set<String> groupSubscribeTopics(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupSubscribeTopics(clusterName,group);
    }

    @GetMapping("/group/topic/data/nearby")
    public List<PartitionKafkaData> groupTopicNearbyData(
	    NearbyDataConsumerParam nearbyDataConsumerParam) throws IOException, ClassNotFoundException {
        return kafkaDataService.nearbyDatas(nearbyDataConsumerParam);
    }

    @GetMapping("/group/subscribes")
    public ConsumerGroupInfo consumerGroupInfo(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.consumerGroupInfo(clusterName,group);
    }

    @GetMapping("/group/topic/offset")
    public List<OffsetShow> groupTopicConsumerInfo(String clusterName, String group, String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupTopicConsumerInfo(clusterName,group,topic);
    }

    @GetMapping("/group/topics/offset")
    public List<TopicOffset> groupTopicConsumerInfos(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupTopicConsumerInfos(clusterName,group);
    }

    @GetMapping("/brokers")
    public List<String> brokers(@NotNull String clusterName) throws IOException {
        List<BrokerInfo> brokers = kafkaService.brokers(clusterName);
        List<String> collect = brokers.stream().map(BrokerInfo::hostAndPort).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/monitor/topic/{topic}")
    public Collection<MBeanMonitorInfo> topicMonitor(@NotNull String clusterName, @PathVariable("topic") String topic) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        Collection<MBeanMonitorInfo> monitor = kafkaService.monitor(clusterName, BrokerTopicMetrics.TopicMetrics.class, topic);
        return monitor;
    }

    @GetMapping("/monitor/broker")
    public Collection<MBeanMonitorInfo> brokerMonitor(@NotNull String clusterName) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        Collection<MBeanMonitorInfo> monitor = kafkaService.monitor(clusterName, BrokerTopicMetrics.BrokerMetrics.class,null);
        return monitor;
    }
}
