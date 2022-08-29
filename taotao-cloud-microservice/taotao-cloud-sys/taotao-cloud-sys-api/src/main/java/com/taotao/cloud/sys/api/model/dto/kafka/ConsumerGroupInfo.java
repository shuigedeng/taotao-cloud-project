package com.taotao.cloud.sys.api.model.dto.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 对应 kafka 数据的 ConsumerGroupDescription
 */
public class ConsumerGroupInfo {
    // 组协调器
    private BrokerInfo coordinator;
    // 分区分配策略 默认 range
    private String partitionAssignor;
    private List<MemberInfo> memberInfos = new ArrayList<>();

    public ConsumerGroupInfo() {
    }

    public ConsumerGroupInfo(BrokerInfo coordinator) {
        this.coordinator = coordinator;
    }

    public ConsumerGroupInfo(BrokerInfo coordinator, String partitionAssignor) {
        this.coordinator = coordinator;
        this.partitionAssignor = partitionAssignor;
    }

    /**
     * 对应 kafka 数据的 MemberDescription
     */
    public static class MemberInfo{
        // 主机消费的主题分区
        private String host;
        private Set<SimpleTopicPartition> topicPartitions;

        public MemberInfo() {
        }

        public MemberInfo(String host, Set<SimpleTopicPartition> topicPartitions) {
            this.host = host;
            this.topicPartitions = topicPartitions;
        }

	    public String getHost() {
		    return host;
	    }

	    public void setHost(String host) {
		    this.host = host;
	    }

	    public Set<SimpleTopicPartition> getTopicPartitions() {
		    return topicPartitions;
	    }

	    public void setTopicPartitions(
		    Set<SimpleTopicPartition> topicPartitions) {
		    this.topicPartitions = topicPartitions;
	    }
    }

    /**
     * 添加一个主机消费的主题分区
     * @param memberInfo
     */
    public void addMember(MemberInfo memberInfo){
        memberInfos.add(memberInfo);
    }

	public BrokerInfo getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(BrokerInfo coordinator) {
		this.coordinator = coordinator;
	}

	public String getPartitionAssignor() {
		return partitionAssignor;
	}

	public void setPartitionAssignor(String partitionAssignor) {
		this.partitionAssignor = partitionAssignor;
	}

	public List<MemberInfo> getMemberInfos() {
		return memberInfos;
	}

	public void setMemberInfos(
		List<MemberInfo> memberInfos) {
		this.memberInfos = memberInfos;
	}
}
