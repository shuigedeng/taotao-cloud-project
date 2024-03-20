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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties;

import com.xkcoding.http.config.HttpConfig;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;

/**
 * JustAuth 代理配置
 * @author YongWu zheng
 * @version V2.0  Created by 2021-05-10 10:14
 */
@Getter
@Setter
public class HttpConfigProperties {

    /**
     * 当 enable = true 时, 返回 HttpConfig 对象, 否则返回为 null.
     * @return  当 enable = true 时, 返回 HttpConfig 对象, 否则返回为 null.
     */
    public HttpConfig getHttpConfig() {
        if (!enable) {
            return HttpConfig.builder().timeout((int) timeout.toMillis()).build();
        }
        return HttpConfig.builder()
                .proxy(new Proxy(proxy, new InetSocketAddress(hostname, port)))
                .timeout((int) timeout.toMillis())
                .build();
    }

    /**
     * 是否支持代理, 默认为: false. <br>
     */
    private Boolean enable = false;

    /**
     * 针对国外服务可以单独设置代理类型, 默认 Proxy.Type.HTTP, enable = true 时生效.
     */
    private Proxy.Type proxy = Proxy.Type.HTTP;

    /**
     * 代理 host, enable = true 时生效.
     */
    private String hostname;

    /**
     * 代理端口, enable = true 时生效.
     */
    private Integer port;

    /**
     * 代理超时, 默认 PT3S
     */
    private Duration timeout = Duration.ofSeconds(3);
    /**
     * 用于国外网站代理超时, 默认 PT15S
     */
    private Duration foreignTimeout = Duration.ofSeconds(15);
}
