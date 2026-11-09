package com.taotao.cloud.mq.consistency.ttc.admin.service;

import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.Dtos.BrokerInfoDto;
import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.Dtos.ClusterOverviewDto;
import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.Dtos.ConsumerGroupInfoDto;
import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.Dtos.MessageItemDto;
import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.Dtos.TopicInfoDto;
import com.taotao.cloud.mq.consistency.ttc.admin.web.dto.MessageQueryRequest;
import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.tools.admin.ClusterAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制台业务层：把 ClusterAdmin 的 RPC 能力整形成前端要的视图对象.
 * <p>
 * <b>口径纪律</b>：拿不到的指标一律给 0 或空列表，绝不编造。
 * 仪表盘的 TPS/存储量若由后端凭空造数，看板就退化成纯装饰，真出故障时反而误导排查 ——
 * 所以这里宁可显示 0（前端呈现为「未采集」），也不给一个看起来像真的假数。
 */
@Service
public class AdminService implements InitializingBean, DisposableBean {

    @Value("${zmq.admin.namesrv-addr:127.0.0.1:9876}")
    private String namesrvAddr;

    /**
     * 写操作要连的 broker 地址；留空则用路由表里的第一个。
     * <p>
     * 路由表里存的是 broker **自己公布的**地址，容器部署时是集群内 DNS，
     * 集群外的控制台（如本机调试、边缘节点上的实例）根本解析不了那个名字 ——
     * 这时显式指一个可达地址，读列表照旧走 NameServer。
     */
    @Value("${zmq.admin.broker-addr:}")
    private String brokerAddrOverride;

    private ClusterAdmin clusterAdmin;

    @Override
    public void afterPropertiesSet() {
        clusterAdmin = new ClusterAdmin(namesrvAddr);
        clusterAdmin.start();
    }

    @Override
    public void destroy() {
        if (clusterAdmin != null) {
            clusterAdmin.shutdown();
        }
    }

    // ===== 集群 =====

    public ClusterOverviewDto clusterOverview() {
        List<BrokerInfoDto> brokerDtos = listBrokers();

        ClusterOverviewDto dto = new ClusterOverviewDto();
        dto.setNamesrvCount(countNamesrvs());
        dto.setBrokerCount(brokerDtos.size());
        dto.setTopicCount(clusterAdmin.listTopics().size());
        dto.setConsumerGroupCount(0);
        // broker 未暴露 TPS / 存储量统计命令，这四项没有采集通道
        dto.setTotalMessages(0);
        dto.setMessagesPerMinute(0);
        dto.setMessagesPerSecond(0);
        dto.setStorageBytes(0);
        return dto;
    }

    private int countNamesrvs() {
        // 只报"真连上了"的数量：配了地址但连不上时报 1 会在控制台上把
        // "集群挂了" 显示成 "1 个 NameServer 在线"，恰好在故障时误导排查
        if (!clusterAdmin.namesrvReachable()) {
            return 0;
        }
        if (namesrvAddr == null || namesrvAddr.trim().isEmpty()) {
            return 0;
        }
        int n = 0;
        for (String s : namesrvAddr.split(";")) {
            if (!s.trim().isEmpty()) {
                n++;
            }
        }
        return n;
    }

    public List<BrokerInfoDto> listBrokers() {
        Map<String, List<BrokerData>> clusterInfo = clusterAdmin.listBrokers();
        List<BrokerInfoDto> list = new ArrayList<>();
        if (clusterInfo == null) {
            return list;
        }
        for (Map.Entry<String, List<BrokerData>> entry : clusterInfo.entrySet()) {
            String cluster = entry.getKey();
            if (entry.getValue() == null) {
                continue;
            }
            for (BrokerData data : entry.getValue()) {
                BrokerInfoDto dto = new BrokerInfoDto();
                dto.setCluster(data.getCluster() != null ? data.getCluster() : cluster);
                dto.setBrokerName(data.getBrokerName());
                dto.setAddr(data.selectBrokerAddr());
                dto.setBrokerId((int) BrokerData.MASTER_ID);
                // broker 未暴露版本/连接数/TPS 的查询命令
                dto.setVersion("");
                dto.setProducerCount(0);
                dto.setConsumerCount(0);
                dto.setQueues(0);
                dto.setTopics(0);
                dto.setInTps(0);
                dto.setOutTps(0);
                dto.setLastUpdateMs(System.currentTimeMillis());
                list.add(dto);
            }
        }
        return list;
    }

    // ===== Topic =====

    public List<TopicInfoDto> listTopics() {
        List<TopicInfoDto> list = new ArrayList<>();
        String brokerAddr = firstBrokerAddr();
        String brokerName = firstBrokerName();
        for (String topic : clusterAdmin.listTopics()) {
            TopicInfoDto dto = new TopicInfoDto();
            dto.setTopic(topic);
            dto.setBroker(brokerName);
            TopicConfig config = brokerAddr == null ? null : clusterAdmin.topicConfig(brokerAddr, topic);
            if (config != null) {
                dto.setReadQueueNums(config.getReadQueueNums());
                dto.setWriteQueueNums(config.getWriteQueueNums());
                dto.setPerm(config.getPerm());
            }
            list.add(dto);
        }
        return list;
    }

    public Map<String, Object> createTopic(String topic, int readQueueNums, int writeQueueNums) {
        Map<String, Object> result = new LinkedHashMap<>();
        String brokerAddr = firstBrokerAddr();
        if (brokerAddr == null) {
            result.put("success", false);
            result.put("message", "no broker available in cluster");
            return result;
        }
        boolean ok = clusterAdmin.createTopic(brokerAddr, topic, readQueueNums, writeQueueNums);
        result.put("success", ok);
        if (ok) {
            result.put("topic", topic);
        } else {
            result.put("message", "create topic failed on broker " + brokerAddr);
        }
        return result;
    }

    public Map<String, Object> deleteTopic(String topic) {
        Map<String, Object> result = new LinkedHashMap<>();
        String brokerAddr = firstBrokerAddr();
        if (brokerAddr == null) {
            result.put("success", false);
            result.put("message", "no broker available in cluster");
            return result;
        }
        boolean ok = clusterAdmin.deleteTopic(brokerAddr, topic);
        result.put("success", ok);
        if (ok) {
            result.put("topic", topic);
        } else {
            result.put("message", "delete topic failed on broker " + brokerAddr);
        }
        return result;
    }

    // ===== 消息 =====

    public List<MessageItemDto> queryMessages(MessageQueryRequest req) {
        if (req == null || req.getTopic() == null || req.getTopic().isEmpty()) {
            return Collections.emptyList();
        }
        String brokerAddr = req.getBrokerAddr() != null && !req.getBrokerAddr().isEmpty()
                ? req.getBrokerAddr() : firstBrokerAddr();
        if (brokerAddr == null) {
            return Collections.emptyList();
        }
        List<MessageExt> messages = clusterAdmin.viewMessage(
                brokerAddr, req.getTopic(), req.getQueueId(), req.getOffset(), req.getCount());

        List<MessageItemDto> list = new ArrayList<>(messages.size());
        for (MessageExt msg : messages) {
            MessageItemDto dto = new MessageItemDto();
            dto.setMsgId(msg.getMsgId());
            dto.setTopic(msg.getTopic());
            dto.setQueueId(msg.getQueueId());
            dto.setQueueOffset(msg.getQueueOffset());
            dto.setBornTimestamp(msg.getBornTimestamp());
            dto.setStoreTimestamp(msg.getStoreTimestamp());
            byte[] body = msg.getBody();
            dto.setBody(body == null ? "" : new String(body, StandardCharsets.UTF_8));
            dto.setKeys(msg.getKeys());
            Map<String, String> props = msg.getProperties();
            dto.setTags(props == null ? null : props.get("TAGS"));
            list.add(dto);
        }
        return list;
    }

    // ===== 消费组 =====

    public List<ConsumerGroupInfoDto> listConsumerGroups() {
        String brokerAddr = firstBrokerAddr();
        if (brokerAddr == null) {
            return Collections.emptyList();
        }
        Map<String, Object> raw = clusterAdmin.listConsumerGroups(brokerAddr);
        if (raw == null || raw.isEmpty()) {
            // broker 侧 GET_ALL_SUBSCRIPTION_GROUP 当前返回空表（MVP 未实现订阅组存储），
            // 如实返回空列表，不造占位数据
            return Collections.emptyList();
        }
        List<ConsumerGroupInfoDto> list = new ArrayList<>();
        Object groups = raw.get("subscriptionGroupTable");
        if (groups instanceof List) {
            for (Object item : (List<?>) groups) {
                if (!(item instanceof Map)) {
                    continue;
                }
                Map<?, ?> m = (Map<?, ?>) item;
                ConsumerGroupInfoDto dto = new ConsumerGroupInfoDto();
                dto.setGroup(String.valueOf(m.get("groupName")));
                dto.setState("UNKNOWN");
                dto.setConsumers(0);
                dto.setTopics(Collections.emptyList());
                dto.setTotalLag(0);
                dto.setTps(0);
                list.add(dto);
            }
        }
        return list;
    }

    // ===== 内部 =====

    /** 集群里第一个 master broker 地址；没有则返回 null。 */
    private String firstBrokerAddr() {
        if (brokerAddrOverride != null && !brokerAddrOverride.trim().isEmpty()) {
            return brokerAddrOverride.trim();
        }
        for (BrokerData data : allBrokers()) {
            String addr = data.selectBrokerAddr();
            if (addr != null && !addr.isEmpty()) {
                return addr;
            }
        }
        return null;
    }

    private String firstBrokerName() {
        for (BrokerData data : allBrokers()) {
            if (data.getBrokerName() != null) {
                return data.getBrokerName();
            }
        }
        return null;
    }

    private List<BrokerData> allBrokers() {
        Map<String, List<BrokerData>> clusterInfo = clusterAdmin.listBrokers();
        List<BrokerData> all = new ArrayList<>();
        if (clusterInfo == null) {
            return all;
        }
        for (List<BrokerData> brokers : clusterInfo.values()) {
            if (brokers != null) {
                all.addAll(brokers);
            }
        }
        return all;
    }
}
