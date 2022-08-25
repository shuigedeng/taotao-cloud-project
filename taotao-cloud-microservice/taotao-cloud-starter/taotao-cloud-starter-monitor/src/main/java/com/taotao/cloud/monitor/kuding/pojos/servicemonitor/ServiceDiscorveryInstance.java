package com.taotao.cloud.monitor.kuding.pojos.servicemonitor;

import java.util.Set;

public class ServiceDiscorveryInstance {

	private String serviceId;

	private int serviceCount;

	private Set<String> instanceIds;

	public ServiceDiscorveryInstance(String serviceId, int serviceCount, Set<String> instanceIds) {
		super();
		this.serviceId = serviceId;
		this.serviceCount = serviceCount;
		this.instanceIds = instanceIds;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(int serviceCount) {
		this.serviceCount = serviceCount;
	}

	public Set<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(Set<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ServiceDiscorveryInstance other = (ServiceDiscorveryInstance) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceDiscorveryInstance [serviceId=" + serviceId + ", serviceCount=" + serviceCount + ", instanceIds="
				+ instanceIds + "]";
	}

}
