package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.model.MicroServiceReport;
import com.taotao.cloud.prometheus.model.ServiceHealthProblem;
import com.taotao.cloud.prometheus.model.ServiceInstanceLackProblem;
import java.util.Set;


public interface ServiceNoticeRepository {

	void addServiceLackProblem(ServiceInstanceLackProblem serviceInstanceLackProblem);

	void addServiceHealthProblem(ServiceHealthProblem serviceHealthProblem);

	void addLackServices(Set<String> serviceName);

	void addLackServices(String... serviceName);

	MicroServiceReport report();
}
