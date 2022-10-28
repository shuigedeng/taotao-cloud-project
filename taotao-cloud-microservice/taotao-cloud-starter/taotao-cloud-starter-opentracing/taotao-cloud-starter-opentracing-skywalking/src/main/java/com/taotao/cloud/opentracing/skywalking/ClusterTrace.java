package com.taotao.cloud.opentracing.skywalking;

public interface ClusterTrace {
	default String getTraceId() {
		return "";
	}

	void setTraceId(String traceId);

	void removeTraceId();
}
