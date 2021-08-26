package com.taotao.cloud.prometheus.microservice.interfaces;

import com.taotao.cloud.prometheus.pojos.servicemonitor.MicroServiceReport;
import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceHealthProblem;
import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceInstanceLackProblem;
import java.util.Set;


public interface ServiceNoticeRepository {

	void addServiceLackProblem(ServiceInstanceLackProblem serviceInstanceLackProblem);

	void addServiceHealthProblem(ServiceHealthProblem serviceHealthProblem);

	void addLackServices(Set<String> serviceName);

	void addLackServices(String... serviceName);

	MicroServiceReport report();
}
