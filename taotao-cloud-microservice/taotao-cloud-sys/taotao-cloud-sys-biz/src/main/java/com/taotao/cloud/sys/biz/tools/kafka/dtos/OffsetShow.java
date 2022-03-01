package com.taotao.cloud.sys.biz.tools.kafka.dtos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OffsetShow implements Comparable<OffsetShow>{
	private String topic;
	private int partition;
	private long offset;
	private long logSize;
	private long lag;
	private String owner;
	private long modified;
	private long minOffset;

	public OffsetShow(String topic, int partition, long offset) {
		super();
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
	}

	public OffsetShow(String topic, int partition, long offset, long logSize) {
		super();
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.logSize = logSize;
		this.lag = (logSize - offset);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getTopic() {
		return topic;
	}

	public int getPartition() {
		return partition;
	}

	public long getOffset() {
		return offset;
	}

	public long getLogSize() {
		return logSize;
	}

	public long getLag() {
		return lag;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public long getMinOffset() {
		return minOffset;
	}

	public void setMinOffset(long minOffset) {
		this.minOffset = minOffset;
	}


	@Override
	public int compareTo(OffsetShow o) {
		return this.partition - o.partition;
	}
}
