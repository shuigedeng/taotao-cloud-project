package com.taotao.cloud.sys.api.model.dto.kafka;


import java.util.ArrayList;
import java.util.List;

/**
 * 对应 kafka 数据的 TopicDescription
 */
public class TopicInfo {
    private String name;
    private boolean internal;
    private List<TopicPartitionInfo> partitions = new ArrayList<>();

    public TopicInfo(String name, boolean internal) {
        this.name = name;
        this.internal = internal;
    }

    /**
     * 添加分区信息
     * @param partitionInfo
     */
    public void addPartitionInfo(TopicPartitionInfo partitionInfo){
        partitions.add(partitionInfo);
    }

    public static class TopicPartitionInfo {
        private int partition;
        private BrokerInfo leader;
        private List<BrokerInfo> replicas;
        private List<BrokerInfo> isr;

        public TopicPartitionInfo(int partition, BrokerInfo leader, List<BrokerInfo> replicas, List<BrokerInfo> isr) {
            this.partition = partition;
            this.leader = leader;
            this.replicas = replicas;
            this.isr = isr;
        }

	    public int getPartition() {
		    return partition;
	    }

	    public void setPartition(int partition) {
		    this.partition = partition;
	    }

	    public BrokerInfo getLeader() {
		    return leader;
	    }

	    public void setLeader(BrokerInfo leader) {
		    this.leader = leader;
	    }

	    public List<BrokerInfo> getReplicas() {
		    return replicas;
	    }

	    public void setReplicas(List<BrokerInfo> replicas) {
		    this.replicas = replicas;
	    }

	    public List<BrokerInfo> getIsr() {
		    return isr;
	    }

	    public void setIsr(List<BrokerInfo> isr) {
		    this.isr = isr;
	    }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public List<TopicPartitionInfo> getPartitions() {
		return partitions;
	}

	public void setPartitions(
		List<TopicPartitionInfo> partitions) {
		this.partitions = partitions;
	}
}
