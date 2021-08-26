package com.taotao.cloud.prometheus.microservice.components;//package com.kuding.microservice.components;
//
//import static java.util.stream.Collectors.toList;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import com.kuding.microservice.interfaces.ReportedFilterHandler;
//import com.kuding.pojos.servicemonitor.MicroServiceReport;
//import com.kuding.pojos.servicemonitor.ServiceInstanceLackProblem;
//
//public class DefaultReportFilterHandler implements ReportedFilterHandler {
//
//	private MicroServiceReport lastMergedMicroServiceReport;
//
//	@Override
//	public MicroServiceReport filter(MicroServiceReport serviceCheckNotice) {
//		if (lastMergedMicroServiceReport == null)
//			return lastMergedMicroServiceReport = serviceCheckNotice;
//
////		// handle recovered
////		Map<String, List<String>> currentRecoveredServices = serviceCheckNotice.getRecoveredServicesInstances();
////		lastMergedMicroServiceReport.getLackServices().removeIf(x -> currentRecoveredServices.containsKey(x));
////		lastMergedMicroServiceReport.getHealthProblems().forEach((x, y) -> y.getUnhealthyInstances()
////				.removeIf(z -> currentRecoveredServices.getOrDefault(x, Collections.emptyList()).contains(z)));
////		Map<String, ServiceInstanceLackProblem> instanceLackMap = lastMergedMicroServiceReport
////				.getInstanceLackProblems();
////		List<String> needDeletedLackServices = currentRecoveredServices.keySet().stream()
////				.filter(x -> instanceLackMap.keySet().contains(x)).collect(toList());
////		needDeletedLackServices.forEach(x -> instanceLackMap.remove(x));
//
//		// handle reported
////		serviceCheckNotice.getLackServices().removeIf(lastMergedMicroServiceReport.getLackServices()::contains);
////		instanceLackMap.keySet().forEach(x -> serviceCheckNotice.getInstanceLackProblems().remove(x));
////		lastMergedMicroServiceReport.getHealthProblems().keySet()
////				.forEach(x -> serviceCheckNotice.getHealthProblems().remove(x));
////
////		// merge
////		serviceCheckNotice.getHealthProblems()
////				.forEach((x, y) -> lastMergedMicroServiceReport.getHealthProblems().merge(x, y, (old, neww) -> {
////					old.getUnhealthyInstances().addAll(neww.getUnhealthyInstances());
////					return old;
////				}));
////		lastMergedMicroServiceReport.getInstanceLackProblems().putAll(serviceCheckNotice.getInstanceLackProblems());
////
//		return serviceCheckNotice;
//	}
//}
