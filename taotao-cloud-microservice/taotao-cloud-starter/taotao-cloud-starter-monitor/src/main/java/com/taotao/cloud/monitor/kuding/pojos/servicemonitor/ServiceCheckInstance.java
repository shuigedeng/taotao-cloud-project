package com.taotao.cloud.monitor.kuding.pojos.servicemonitor;

public class ServiceCheckInstance {

	private String instanceId;

	private String serviceId;

	private boolean noticed;

	private ServiceStatus serviceStatus;

	public ServiceCheckInstance(String instanceId, String serviceId, boolean noticed, ServiceStatus serviceStatus) {
		super();
		this.instanceId = instanceId;
		this.serviceId = serviceId;
		this.noticed = noticed;
		this.serviceStatus = serviceStatus;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isNoticed() {
		return noticed;
	}

	public void setNoticed(boolean noticed) {
		this.noticed = noticed;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceCheckInstance other = (ServiceCheckInstance) obj;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceCheckInstance [instanceId=" + instanceId + ", serviceId=" + serviceId + ", noticed=" + noticed
				+ ", serviceStatus=" + serviceStatus + "]";
	}

}
