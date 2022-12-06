package com.taotao.cloud.tracing.skywalking;

public class SkyWalkingClusterTrace implements ClusterTrace {

	private final static ThreadLocal<String> TRACE_ID_STORAGE = new ThreadLocal<>();

	@Override
	public void setTraceId(String traceId) {
		TRACE_ID_STORAGE.set(traceId);
	}

	@Override
	public void removeTraceId() {
		TRACE_ID_STORAGE.remove();
	}

	@Override
	public String getTraceId() {
		return TRACE_ID_STORAGE.get();
	}
}
