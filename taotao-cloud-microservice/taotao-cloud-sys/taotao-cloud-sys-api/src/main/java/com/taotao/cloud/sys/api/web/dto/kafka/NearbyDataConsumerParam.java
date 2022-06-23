package com.taotao.cloud.sys.api.web.dto.kafka;

public class NearbyDataConsumerParam extends DataConsumerParam{
    private long offset;

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}
}
