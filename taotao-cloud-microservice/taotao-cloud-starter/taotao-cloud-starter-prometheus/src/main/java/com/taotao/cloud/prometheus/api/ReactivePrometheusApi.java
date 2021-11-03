/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.prometheus.api;

import com.taotao.cloud.prometheus.pojo.AlertMessage;
import com.taotao.cloud.prometheus.pojo.TargetGroup;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * prometheus http sd
 *
 * @author L.cm
 */
@RestController
@RequestMapping("actuator/prometheus")
public class ReactivePrometheusApi {

	private final ReactiveDiscoveryClient discoveryClient;
	private final ApplicationEventPublisher eventPublisher;

	public ReactivePrometheusApi(
		ReactiveDiscoveryClient discoveryClient,
		ApplicationEventPublisher eventPublisher) {
		this.discoveryClient = discoveryClient;
		this.eventPublisher = eventPublisher;
	}

	@GetMapping("sd")
	public Flux<TargetGroup> getList() {
		return discoveryClient.getServices()
			.flatMap(discoveryClient::getInstances)
			.groupBy(ServiceInstance::getServiceId, (instance) ->
				String.format("%s:%d", instance.getHost(), instance.getPort())
			).flatMap(instanceGrouped -> {
				Map<String, String> labels = new HashMap<>(2);
				String serviceId = instanceGrouped.key();
				labels.put("__meta_prometheus_job", serviceId);
				return instanceGrouped.collect(Collectors.toList())
					.map(targets -> new TargetGroup(targets, labels));
			});
	}

	@PostMapping("alerts")
	public ResponseEntity<Object> postAlerts(@RequestBody AlertMessage message) {
		eventPublisher.publishEvent(message);
		return ResponseEntity.ok().build();
	}

}
