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

package com.taotao.cloud.monitor.configuration;

import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.dingtalk.entity.DingerRequest;
import com.taotao.boot.dingtalk.enums.MessageSubType;
import com.taotao.boot.dingtalk.model.DingerSender;
import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * NotifierConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/01 10:01
 */
@Configuration
@AutoConfigureAfter(AdminServerAutoConfiguration.class)
public class NotifierConfiguration {

    @Bean
    public DingDingNotifier dingDingNotifier(InstanceRepository repository) {
        return new DingDingNotifier(repository);
    }

    @Bean
    public HttpHeadersProvider customHttpHeadersProvider() {
        return (instance) -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("X-CUSTOM", "My Custom Value");
            return httpHeaders;
        };
    }

    @Bean
    public InstanceExchangeFilterFunction auditLog() {
        return (instance, request, next) ->
                next.exchange(request)
                        .doOnSubscribe(
                                (s) -> {
                                    if (HttpMethod.DELETE.equals(request.method())
                                            || HttpMethod.POST.equals(request.method())) {
                                        LogUtils.info(
                                                "{} for {} on {}",
                                                request.method(),
                                                instance.getId(),
                                                request.url());
                                    }
                                });
    }

    public static class DingDingNotifier extends AbstractStatusChangeNotifier {

        private final String[] ignoreChanges = new String[] {"UNKNOWN:UP", "DOWN:UP", "OFFLINE:UP"};

        @Autowired private DingerSender sender;

        public DingDingNotifier(InstanceRepository repository) {
            super(repository);
        }

        @Override
        protected boolean shouldNotify(InstanceEvent event, Instance instance) {
            LogUtils.info("微服务监控回调数据 shouldNotify event: {}, instance: {}", event, instance);

            if (!(event instanceof InstanceStatusChangedEvent statusChange)) {
                return false;
            } else {
                String from = this.getLastStatus(event.getInstance());
                String to = statusChange.getStatusInfo().getStatus();
                return Arrays.binarySearch(this.ignoreChanges, from + ":" + to) < 0
                        && Arrays.binarySearch(this.ignoreChanges, "*:" + to) < 0
                        && Arrays.binarySearch(this.ignoreChanges, from + ":*") < 0;
            }
        }

        @Override
        protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
            String serviceName = instance.getRegistration().getName();
            String serviceUrl = instance.getRegistration().getServiceUrl();

            StringBuilder str = new StringBuilder();
            str.append("taotaocloud微服务监控 \n");
            str.append("[时间戳]: ")
                    .append(
                            DateUtils.format(
                                    LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT))
                    .append("\n");
            str.append("[服务名] : ").append(serviceName).append("\n");
            str.append("[服务ip]: ").append(serviceUrl).append("\n");

            return Mono.fromRunnable(
                    () -> {
                        if (event instanceof InstanceStatusChangedEvent) {
                            String status =
                                    ((InstanceStatusChangedEvent) event)
                                            .getStatusInfo()
                                            .getStatus();
                            switch (status) {
                                // 健康检查没通过
                                case "DOWN" ->
                                        str.append("[服务状态]: ")
                                                .append(status)
                                                .append("(")
                                                .append("健康检未通过")
                                                .append(")")
                                                .append("\n");

                                // 服务离线
                                case "OFFLINE" ->
                                        str.append("[服务状态]: ")
                                                .append(status)
                                                .append("(")
                                                .append("服务离线")
                                                .append(")")
                                                .append("\n");

                                // 服务上线
                                case "UP" ->
                                        str.append("[服务状态]: ")
                                                .append(status)
                                                .append("(")
                                                .append("服务上线")
                                                .append(")")
                                                .append("\n");

                                // 服务未知异常
                                case "UNKNOWN" ->
                                        str.append("[服务状态]: ")
                                                .append(status)
                                                .append("(")
                                                .append("服务未知异常")
                                                .append(")")
                                                .append("\n");

                                default ->
                                        str.append("[服务状态]: ")
                                                .append(status)
                                                .append("(")
                                                .append("服务未知异常")
                                                .append(")")
                                                .append("\n");
                            }

                            Map<String, Object> details =
                                    ((InstanceStatusChangedEvent) event)
                                            .getStatusInfo()
                                            .getDetails();
                            str.append("[服务详情]: ").append(JsonUtils.toJSONString(details));

                            LogUtils.info(
                                    "微服务监控回调数据 event: {}, instance: {}, message: {}",
                                    event,
                                    instance,
                                    str);

                            sender.send(MessageSubType.TEXT, DingerRequest.request(str.toString()));
                        }
                    });
        }
    }
}
