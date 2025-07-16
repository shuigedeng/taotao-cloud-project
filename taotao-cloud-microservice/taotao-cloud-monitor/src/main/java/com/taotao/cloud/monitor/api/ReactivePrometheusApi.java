/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.monitor.api;

import com.taotao.cloud.monitor.model.AlertMessage;
import com.taotao.cloud.monitor.model.TargetGroup;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * prometheus http sd
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@RestController
@RequestMapping("/actuator/prometheus/reactive")
public class ReactivePrometheusApi {

    private final ReactiveDiscoveryClient discoveryClient;
    private final ApplicationEventPublisher eventPublisher;

    public ReactivePrometheusApi(
            ReactiveDiscoveryClient discoveryClient, ApplicationEventPublisher eventPublisher) {
        this.discoveryClient = discoveryClient;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/sd")
    public Flux<TargetGroup> getList() {
        return discoveryClient
                .getServices()
                .flatMap(discoveryClient::getInstances)
                .groupBy(
                        ServiceInstance::getServiceId,
                        (instance) ->
                                String.format("%s:%d", instance.getHost(), instance.getPort()))
                .flatMap(
                        instanceGrouped -> {
                            Map<String, String> labels = new HashMap<>(2);
                            String serviceId = instanceGrouped.key();
                            labels.put("__taotao_prometheus_job", serviceId);

                            return instanceGrouped
                                    .collectList()
                                    .map(targets -> new TargetGroup(targets, labels));
                        });
    }

    @PostMapping("/alerts")
    public ResponseEntity<Object> postAlerts(@RequestBody AlertMessage message) {
        eventPublisher.publishEvent(message);
        return ResponseEntity.ok().build();
    }
}
