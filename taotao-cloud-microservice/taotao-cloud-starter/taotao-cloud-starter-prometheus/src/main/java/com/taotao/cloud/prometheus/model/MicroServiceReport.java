package com.taotao.cloud.prometheus.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MicroServiceReport {

	/**
	 * 缺失的服务
	 */
	private Set<String> lackServices = new HashSet<>();

	/**
	 * 缺少实例的服务
	 */
	private Map<String, ServiceInstanceLackProblem> instanceLackProblems = new HashMap<>();

	/**
	 * 不健康的服务
	 */
	private Map<String, ServiceHealthProblem> healthProblems = new HashMap<>();

	/**
	 * 是否需要通知
	 */
	private boolean needReport = false;

	public Set<String> getLackServices() {
		return lackServices;
	}

	public void setLackServices(Set<String> lackServices) {
		this.lackServices = lackServices;
	}

	public Map<String, ServiceInstanceLackProblem> getInstanceLackProblems() {
		return instanceLackProblems;
	}

	public void setInstanceLackProblems(Map<String, ServiceInstanceLackProblem> instanceLackProblems) {
		this.instanceLackProblems = instanceLackProblems;
	}

	public Map<String, ServiceHealthProblem> getHealthProblems() {
		return healthProblems;
	}

	public void setHealthProblems(Map<String, ServiceHealthProblem> healthProblems) {
		this.healthProblems = healthProblems;
	}

	public void putLackService(String service) {
		lackServices.add(service);
		needReport = true;
	}

	public void putUnHealthyService(String serviceName, ServiceHealthProblem serviceHealthProblem) {
		healthProblems.put(serviceName, serviceHealthProblem);
		needReport = true;
	}

	public void putLackInstanceService(String serviceName, ServiceInstanceLackProblem instanceLackProblem) {
		instanceLackProblems.put(serviceName, instanceLackProblem);
		needReport = true;
	}

	@Override
	public String toString() {
		return "MicroServiceReport [lackServices=" + lackServices + ", instanceLackProblems=" + instanceLackProblems
				+ ", healthProblems=" + healthProblems + "]";
	}

	public int totalProblemCount() {
		return healthProblems.size() + instanceLackProblems.size() + lackServices.size();
	}

	public void putLackServices(Set<String> serviceIds) {
		this.lackServices.addAll(serviceIds);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((healthProblems == null) ? 0 : healthProblems.hashCode());
		result = prime * result + ((instanceLackProblems == null) ? 0 : instanceLackProblems.hashCode());
		result = prime * result + ((lackServices == null) ? 0 : lackServices.hashCode());
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
		MicroServiceReport other = (MicroServiceReport) obj;
		if (healthProblems == null) {
			if (other.healthProblems != null)
				return false;
		} else if (!healthProblems.equals(other.healthProblems))
			return false;
		if (instanceLackProblems == null) {
			if (other.instanceLackProblems != null)
				return false;
		} else if (!instanceLackProblems.equals(other.instanceLackProblems))
			return false;
		if (lackServices == null) {
			if (other.lackServices != null)
				return false;
		} else if (!lackServices.equals(other.lackServices))
			return false;
		return true;
	}

	public boolean isNeedReport() {
		return needReport;
	}

	public void setNeedReport(boolean needReport) {
		this.needReport = needReport;
	}

}
