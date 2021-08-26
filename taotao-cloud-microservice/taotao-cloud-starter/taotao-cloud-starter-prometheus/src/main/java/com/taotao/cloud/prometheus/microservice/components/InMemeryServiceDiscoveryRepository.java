package com.taotao.cloud.prometheus.microservice.components;//package com.kuding.microservice.components;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import com.kuding.microservice.interfaces.ServiceDiscoveryRepository;
//import com.kuding.pojos.servicemonitor.ServiceDiscorveryInstance;
//
//public class InMemeryServiceDiscoveryRepository implements ServiceDiscoveryRepository {
//
//	private final Set<ServiceDiscorveryInstance> discorveryInstances = new HashSet<>();
//
//	@Override
//	public void add(ServiceDiscorveryInstance discorveryInstance) {
//		discorveryInstances.add(discorveryInstance);
//	}
//
//	@Override
//	public void del(ServiceDiscorveryInstance discorveryInstance) {
//		discorveryInstances.removeIf(x -> x.getServiceId().equals(discorveryInstance.getServiceId()));
//	}
//
//	@Override
//	public void modify(ServiceDiscorveryInstance discorveryInstance) {
//		discorveryInstances.add(discorveryInstance);
//
//	}
//
//	@Override
//	public List<ServiceDiscorveryInstance> list() {
//		return discorveryInstances.stream().collect(Collectors.toList());
//	}
//}
