package com.taotao.cloud.prometheus.pojos.servicemonitor;

public class ServiceHealth {

	private String instanceId;

	private ServiceStatus status;

	public ServiceHealth(String instanceId, ServiceStatus status) {
		this.instanceId = instanceId;
		this.status = status;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

}
