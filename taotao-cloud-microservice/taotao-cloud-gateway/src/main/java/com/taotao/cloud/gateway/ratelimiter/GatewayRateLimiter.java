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

package com.taotao.cloud.gateway.ratelimiter;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import java.util.Objects;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import reactor.core.publisher.Mono;

/**
 * CustomRateLimiter
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/01/06 16:42
 */
// @Component
public class GatewayRateLimiter extends AbstractRateLimiter<GatewayRateLimiter.Config> {

    private final RateLimiter rateLimiter = RateLimiter.create(2.0);

    public static final String CONFIG_PROPERTY_NAME = "taotao-cloud-gateway-rate-limiter";

    protected GatewayRateLimiter( ConfigurationService configurationService ) {
        super(Config.class, CONFIG_PROPERTY_NAME, configurationService);
    }

    @Override
    public Mono<Response> isAllowed( String routeId, String id ) {
        Config config = getConfig().get(routeId);
        if (Objects.nonNull(config)) {
            return Mono.fromSupplier(
                    () -> {
                        boolean acquire = rateLimiter.tryAcquire(config.requestedToken);
                        if (acquire) {
                            return new Response(true, Maps.newHashMap());
                        }

                        return new Response(false, Maps.newHashMap());
                    });
        } else {
            return Mono.fromSupplier(() -> new Response(true, Maps.newHashMap()));
        }
    }

    /**
     * Config
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static class Config {

        // 每次请求多少个token
        private Integer requestedToken = 100;

        public Integer getRequestedToken() {
            return requestedToken;
        }

        public void setRequestedToken( Integer requestedToken ) {
            this.requestedToken = requestedToken;
        }
    }
}
