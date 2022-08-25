package com.taotao.cloud.monitor.kuding.pojos.servicemonitor;

import com.taotao.cloud.monitor.kuding.microservice.events.ServiceInstanceUnhealthyEvent;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


public class ServiceHealthProblem extends ServiceProblem {

	private Set<String> healthyInstances = Collections.emptySet();

	private Set<String> unhealthyInstances = Collections.emptySet();

	public ServiceHealthProblem(String serviceName, Set<String> healthyInstances, Set<String> unhealthyInstances) {
		this.serviceName = serviceName;
		this.healthyInstances = healthyInstances;
		this.unhealthyInstances = unhealthyInstances;
	}

	public ServiceHealthProblem(ServiceInstanceUnhealthyEvent event) {
		this.serviceName = event.getServiceName();
		Map<ServiceStatus, Set<String>> map = event.getServiceHealths().stream()
				.collect(groupingBy(ServiceHealth::getStatus, mapping(ServiceHealth::getInstanceId, toSet())));
		healthyInstances = map.getOrDefault(ServiceStatus.UP, Collections.emptySet());
		unhealthyInstances = map.getOrDefault(ServiceStatus.DOWN, Collections.emptySet());
	}

	public Set<String> getHealthyInstances() {
		return healthyInstances;
	}

	public void setHealthyInstances(Set<String> healthyInstances) {
		this.healthyInstances = healthyInstances;
	}

	public Set<String> getUnhealthyInstances() {
		return unhealthyInstances;
	}

	public void setUnhealthyInstances(Set<String> unhealthyInstances) {
		this.unhealthyInstances = unhealthyInstances;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((healthyInstances == null) ? 0 : healthyInstances.hashCode());
		result = prime * result + ((unhealthyInstances == null) ? 0 : unhealthyInstances.hashCode());
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
		ServiceHealthProblem other = (ServiceHealthProblem) obj;
		if (healthyInstances == null) {
			if (other.healthyInstances != null)
				return false;
		} else if (!healthyInstances.equals(other.healthyInstances))
			return false;
		if (unhealthyInstances == null) {
			if (other.unhealthyInstances != null)
				return false;
		} else if (!unhealthyInstances.equals(other.unhealthyInstances))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceHealthProblem [healthyInstances=" + healthyInstances + ", unhealthyInstances="
				+ unhealthyInstances + ", serviceName=" + serviceName + "]";
	}

}
