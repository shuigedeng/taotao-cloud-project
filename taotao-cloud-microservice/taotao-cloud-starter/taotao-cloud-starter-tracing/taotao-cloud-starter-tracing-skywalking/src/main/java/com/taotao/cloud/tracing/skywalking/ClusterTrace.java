package com.taotao.cloud.tracing.skywalking;

public interface ClusterTrace {

	default String getTraceId() {
		return "";
	}

	void setTraceId(String traceId);

	void removeTraceId();
}
