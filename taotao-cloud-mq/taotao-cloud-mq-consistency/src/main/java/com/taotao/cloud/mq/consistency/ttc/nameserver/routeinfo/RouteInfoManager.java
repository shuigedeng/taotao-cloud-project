package com.taotao.cloud.mq.consistency.ttc.nameserver.routeinfo;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.RegisterBrokerResult;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.remoting.common.RemotingHelper;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 路由信息管理器
 * 负责管理Broker路由信息、Topic路由数据、Broker存活状态等
 * <p>
 * 核心数据结构：
 * 1. topicQueueTable - Topic队列信息（Topic -> List<QueueData>）
 * 2. brokerAddrTable - Broker地址信息（BrokerName -> BrokerData）
 * 3. clusterAddrTable - 集群信息（ClusterName -> Set<BrokerName>）
 * 4. brokerLiveTable - Broker存活信息（BrokerAddr -> BrokerLiveInfo）
 */
public class RouteInfoManager {

    private static final Logger log = LoggerFactory.getLogger(RouteInfoManager.class);

    // Broker超时时间（毫秒）- 2分钟
    private static final long BROKER_CHANNEL_EXPIRED_TIME = 1000 * 60 * 2;

    // 读写锁，用于并发控制
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Topic队列信息：Topic -> List<QueueData>
    private final HashMap<String, List<QueueData>> topicQueueTable = new HashMap<>();

    // Broker地址信息：BrokerName -> BrokerData
    private final HashMap<String, BrokerData> brokerAddrTable = new HashMap<>();

    // 集群信息：ClusterName -> Set<BrokerName>
    private final HashMap<String, Set<String>> clusterAddrTable = new HashMap<>();

    // Broker存活信息：BrokerAddr -> BrokerLiveInfo
    private final ConcurrentHashMap<String, BrokerLiveInfo> brokerLiveTable = new ConcurrentHashMap<>();

    /**
     * 扫描并移除不活跃的Broker
     */
    public void scanNotActiveBroker() {
        Iterator<Map.Entry<String, BrokerLiveInfo>> it = this.brokerLiveTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, BrokerLiveInfo> entry = it.next();
            long lastUpdateTimestamp = entry.getValue().getLastUpdateTimestamp();
            long channelExpiredTime = entry.getValue().getChannelExpiredTime();

            if ((lastUpdateTimestamp + channelExpiredTime) < System.currentTimeMillis()) {
                // 关闭通道
                RemotingHelper.closeChannel(entry.getValue().getChannel());
                it.remove();
                log.warn("The broker channel expired, {} ,{}ms", entry.getKey(), channelExpiredTime);
                // 从路由表中删除Broker
                this.onChannelDestroy(entry.getKey(), entry.getValue().getChannel());
            }
        }
    }

    /**
     * Broker下线处理
     */
    public void onChannelDestroy(String remoteAddr, Channel channel) {
        String brokerNameFound = null;
        String brokerAddrFound = null;

        this.lock.writeLock().lock();
        try {
            // 查找对应的Broker
            Iterator<Map.Entry<String, BrokerLiveInfo>> brokerLiveTableIt = this.brokerLiveTable.entrySet().iterator();
            while (brokerLiveTableIt.hasNext()) {
                Map.Entry<String, BrokerLiveInfo> entry = brokerLiveTableIt.next();
                if (entry.getValue().getChannel() == channel) {
                    brokerAddrFound = entry.getKey();
                    break;
                }
            }

            if (brokerAddrFound != null) {
                // 从BrokerData中移除
                Iterator<Map.Entry<String, BrokerData>> brokerAddrTableIt = this.brokerAddrTable.entrySet().iterator();
                while (brokerAddrTableIt.hasNext()) {
                    BrokerData brokerData = brokerAddrTableIt.next().getValue();
                    brokerNameFound = brokerData.getBrokerName();
                    brokerData.getBrokerAddrs().values().remove(brokerAddrFound);

                    if (brokerData.getBrokerAddrs().isEmpty()) {
                        brokerAddrTableIt.remove();
                        // 从clusterAddrTable中移除
                        // 使用迭代器避免lambda中的非final变量问题
                        Iterator<Map.Entry<String, Set<String>>> clusterIt = this.clusterAddrTable.entrySet().iterator();
                        while (clusterIt.hasNext()) {
                            Set<String> brokerNames = clusterIt.next().getValue();
                            brokerNames.remove(brokerNameFound);
                            if (brokerNames.isEmpty()) {
                                clusterIt.remove();
                            }
                        }
                    }
                    break;
                }

                // 从brokerLiveTable中移除
                this.brokerLiveTable.remove(brokerAddrFound);

                // 从topicQueueTable中移除
                // 从topicQueueTable中移除
                Iterator<Map.Entry<String, List<QueueData>>> topicQueueTableIt = this.topicQueueTable.entrySet().iterator();
                while (topicQueueTableIt.hasNext()) {
                    List<QueueData> queueDataList = topicQueueTableIt.next().getValue();
                    // 使用显式循环避免lambda中的非final变量问题
                    Iterator<QueueData> queueDataIt = queueDataList.iterator();
                    while (queueDataIt.hasNext()) {
                        QueueData queueData = queueDataIt.next();
                        if (queueData.getBrokerName().equals(brokerNameFound)) {
                            queueDataIt.remove();
                        }
                    }
                    if (queueDataList.isEmpty()) {
                        topicQueueTableIt.remove();
                    }
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }

        if (brokerNameFound != null) {
            log.info("onChannelDestroy: remove broker[{}][{}] from NameServer", brokerNameFound, brokerAddrFound);
        }
    }

    /**
     * 注册Broker
     */
    public RegisterBrokerResult registerBroker(
            final String clusterName,
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final String haServerAddr,
            final Channel channel) {

        RegisterBrokerResult result = new RegisterBrokerResult();

        this.lock.writeLock().lock();
        try {
            // 更新集群信息
            Set<String> brokerNames = this.clusterAddrTable.computeIfAbsent(clusterName, k -> new HashSet<>());
            brokerNames.add(brokerName);

            // 更新Broker地址信息
            BrokerData brokerData = this.brokerAddrTable.get(brokerName);
            if (brokerData == null) {
                brokerData = new BrokerData(clusterName, brokerName, new HashMap<>());
                this.brokerAddrTable.put(brokerName, brokerData);
            }

            // 如果是主Broker，记录HA地址
            if (brokerId == 0) {
                result.setHaServerAddr(haServerAddr);
            }

            // 更新Broker地址
            String oldAddr = brokerData.getBrokerAddrs().put(brokerId, brokerAddr);
            result.setMasterAddr(oldAddr);

            // 更新Broker存活信息
            BrokerLiveInfo prevBrokerLiveInfo = this.brokerLiveTable.put(brokerAddr,
                    new BrokerLiveInfo(System.currentTimeMillis(), BROKER_CHANNEL_EXPIRED_TIME, channel));

            if (prevBrokerLiveInfo != null) {
                log.warn("Broker[{}] Channel has been changed! OldChannel:{}, NewChannel:{}",
                        brokerAddr, prevBrokerLiveInfo.getChannel(), channel);
            }

        } finally {
            this.lock.writeLock().unlock();
        }

        log.info("registerBroker: clusterName={}, brokerAddr={}, brokerName={}, brokerId={}",
                clusterName, brokerAddr, brokerName, brokerId);

        return result;
    }

    /**
     * 注销Broker
     */
    public void unregisterBroker(
            final String clusterName,
            final String brokerAddr,
            final String brokerName,
            final long brokerId) {

        this.lock.writeLock().lock();
        try {
            // 从Broker存活表中移除
            BrokerLiveInfo removed = this.brokerLiveTable.remove(brokerAddr);
            if (removed == null) {
                log.warn("unregisterBroker: brokerAddr[{}] not found in brokerLiveTable", brokerAddr);
                return;
            }

            // 从Broker地址表中移除
            BrokerData brokerData = this.brokerAddrTable.get(brokerName);
            if (brokerData != null) {
                String removedAddr = brokerData.getBrokerAddrs().remove(brokerId);
                if (brokerData.getBrokerAddrs().isEmpty()) {
                    this.brokerAddrTable.remove(brokerName);
                    // 从集群表中移除
                    Set<String> brokerNames = this.clusterAddrTable.get(clusterName);
                    if (brokerNames != null) {
                        brokerNames.remove(brokerName);
                        if (brokerNames.isEmpty()) {
                            this.clusterAddrTable.remove(clusterName);
                        }
                    }
                }
                log.info("unregisterBroker: remove broker[{}][{}] from brokerAddrTable", brokerName, removedAddr);
            }

        } finally {
            this.lock.writeLock().unlock();
        }

        log.info("unregisterBroker: clusterName={}, brokerAddr={}, brokerName={}, brokerId={}",
                clusterName, brokerAddr, brokerName, brokerId);
    }

    /**
     * 注册 / 覆盖某 Broker 的 Topic 配置。
     * <p>
     * 把 topic + readQueueNums / writeQueueNums / perm 注册到 topicQueueTable，
     * 之后 {@link #pickupTopicRouteData(String)} 就能看到。
     */
    public void registerTopic(final String brokerName, final TopicConfig topicConfig) {
        if (brokerName == null || topicConfig == null || topicConfig.getTopicName() == null) {
            return;
        }
        this.lock.writeLock().lock();
        try {
            QueueData qd = new QueueData();
            qd.setBrokerName(brokerName);
            qd.setReadQueueNums(topicConfig.getReadQueueNums());
            qd.setWriteQueueNums(topicConfig.getWriteQueueNums());
            qd.setPerm(topicConfig.getPerm());
            qd.setTopicSysFlag(0);
            List<QueueData> list = this.topicQueueTable.computeIfAbsent(topicConfig.getTopicName(),
                    k -> new ArrayList<>());
            // 替换同 broker 的旧 entry，避免重复
            list.removeIf(q -> brokerName.equals(q.getBrokerName()));
            list.add(qd);
            log.info("registerTopic: broker={} topic={} readQ={} writeQ={}",
                    brokerName, topicConfig.getTopicName(), qd.getReadQueueNums(), qd.getWriteQueueNums());
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * 清掉某个 Broker 名下的全部 Topic 队列项，注册前调用。
     * <p>
     * 注册只增不减：broker 上把 topic 删掉后再上报，{@link #registerTopic} 一条都不会进来，
     * 旧的那条却一直留在表里。于是 Topic 删了、路由表里还在，控制台列出来的是一条
     * 没有队列数的幽灵 topic，客户端也会一直往一个已经不存在的 topic 拉消息。
     * <p>
     * Broker 的注册报文是**全量快照**，所以先整体清掉再按新快照重填就是对的；
     * 只动属于该 broker 的 QueueData，别家 broker 服务同一个 topic 时不受影响。
     */
    public void unregisterTopicsOfBroker(final String brokerName) {
        if (brokerName == null) {
            return;
        }
        this.lock.writeLock().lock();
        try {
            Iterator<Map.Entry<String, List<QueueData>>> it = this.topicQueueTable.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<QueueData>> entry = it.next();
                List<QueueData> queues = entry.getValue();
                if (queues == null) {
                    it.remove();
                    continue;
                }
                queues.removeIf(q -> brokerName.equals(q.getBrokerName()));
                if (queues.isEmpty()) {
                    it.remove();
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * 获取 Topic 队列表 (Topic -> List&lt;QueueData&gt;) 的快照。
     */
    public Map<String, List<QueueData>> getTopicQueueTable() {
        this.lock.readLock().lock();
        try {
            return new HashMap<>(this.topicQueueTable);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * 获取 Broker 地址表 (BrokerName -> BrokerData) 的快照。
     */
    public Map<String, BrokerData> getBrokerAddrTable() {
        this.lock.readLock().lock();
        try {
            return new HashMap<>(this.brokerAddrTable);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * 获取 Cluster 表 (ClusterName -> Set&lt;BrokerName&gt;) 的快照。
     */
    public Map<String, Set<String>> getClusterAddrTable() {
        this.lock.readLock().lock();
        try {
            return new HashMap<>(this.clusterAddrTable);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * 获取Topic路由信息
     */
    public TopicRouteData pickupTopicRouteData(final String topic) {
        TopicRouteData topicRouteData = new TopicRouteData();

        this.lock.readLock().lock();
        try {
            // 获取队列信息
            List<QueueData> queueDataList = this.topicQueueTable.get(topic);
            if (queueDataList == null || queueDataList.isEmpty()) {
                return null;
            }
            topicRouteData.setQueueDatas(queueDataList);

            // 获取Broker信息
            Set<String> brokerNameSet = new HashSet<>();
            for (QueueData queueData : queueDataList) {
                brokerNameSet.add(queueData.getBrokerName());
            }

            List<BrokerData> brokerDataList = new ArrayList<>();
            for (String brokerName : brokerNameSet) {
                BrokerData brokerData = this.brokerAddrTable.get(brokerName);
                if (brokerData != null) {
                    brokerDataList.add(brokerData);
                }
            }
            topicRouteData.setBrokerDatas(brokerDataList);

        } finally {
            this.lock.readLock().unlock();
        }

        return topicRouteData;
    }

    /**
     * 获取所有集群信息
     */
    public Map<String, Set<String>> getClusterInfo() {
        this.lock.readLock().lock();
        try {
            return new HashMap<>(this.clusterAddrTable);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * 根据集群名称获取Broker名称列表
     */
    public Set<String> getBrokerNamesByCluster(String clusterName) {
        this.lock.readLock().lock();
        try {
            Set<String> brokerNames = this.clusterAddrTable.get(clusterName);
            if (brokerNames != null) {
                return new HashSet<>(brokerNames);
            }
            return null;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * 获取系统Topic列表
     */
    public Set<String> getSystemTopicList() {
        this.lock.readLock().lock();
        try {
            Set<String> topicList = new HashSet<>();
            // 添加所有Topic
            topicList.addAll(this.topicQueueTable.keySet());
            // 添加集群名称
            topicList.addAll(this.clusterAddrTable.keySet());
            return topicList;
        } finally {
            this.lock.readLock().unlock();
        }
    }
}

/**
 * Broker存活信息
 */
class BrokerLiveInfo {
    private long lastUpdateTimestamp;
    private long channelExpiredTime;
    private Channel channel;

    public BrokerLiveInfo(long lastUpdateTimestamp, long channelExpiredTime, Channel channel) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.channelExpiredTime = channelExpiredTime;
        this.channel = channel;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public long getChannelExpiredTime() {
        return channelExpiredTime;
    }

    public void setChannelExpiredTime(long channelExpiredTime) {
        this.channelExpiredTime = channelExpiredTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
