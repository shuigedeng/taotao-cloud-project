package com.taotao.cloud.monitor.kuding.microservice.interfaces;

import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.MicroServiceReport;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.ServiceHealthProblem;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.ServiceInstanceLackProblem;

import java.util.Set;


public interface ServiceNoticeRepository {

	void addServiceLackProblem(ServiceInstanceLackProblem serviceInstanceLackProblem);

	void addServiceHealthProblem(ServiceHealthProblem serviceHealthProblem);

	void addLackServices(Set<String> serviceName);

	void addLackServices(String... serviceName);

	MicroServiceReport report();
}
