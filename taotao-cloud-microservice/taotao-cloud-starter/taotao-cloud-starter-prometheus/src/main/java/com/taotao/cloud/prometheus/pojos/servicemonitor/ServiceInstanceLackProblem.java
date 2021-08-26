package com.taotao.cloud.prometheus.pojos.servicemonitor;

import java.util.Collection;
import java.util.Set;

public class ServiceInstanceLackProblem extends ServiceProblem {

	private Collection<String> instanceIds;

	private int lackCount;

	/**
	 * @param instanceIds
	 * @param lackCount
	 */
	public ServiceInstanceLackProblem(String serviceName, Set<String> instanceIds, int lackCount) {
		this.serviceName = serviceName;
		this.instanceIds = instanceIds;
		this.lackCount = lackCount;
	}

	public ServiceInstanceLackProblem(ServiceDiscorveryInstance instance) {
		this.serviceName = instance.getServiceId();
		this.instanceIds = instance.getInstanceIds();
		this.lackCount = instance.getServiceCount() - instanceIds.size();
	}

	public Collection<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(Collection<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

	public int getLackCount() {
		return lackCount;
	}

	public void setLackCount(int lackCount) {
		this.lackCount = lackCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanceIds == null) ? 0 : instanceIds.hashCode());
		result = prime * result + lackCount;
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
		ServiceInstanceLackProblem other = (ServiceInstanceLackProblem) obj;
		if (instanceIds == null) {
			if (other.instanceIds != null)
				return false;
		} else if (!instanceIds.equals(other.instanceIds))
			return false;
		if (lackCount != other.lackCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceInstanceLackProblem [instanceIds=" + instanceIds + ", lackCount=" + lackCount + ", serviceName="
				+ serviceName + "]";
	}

}
