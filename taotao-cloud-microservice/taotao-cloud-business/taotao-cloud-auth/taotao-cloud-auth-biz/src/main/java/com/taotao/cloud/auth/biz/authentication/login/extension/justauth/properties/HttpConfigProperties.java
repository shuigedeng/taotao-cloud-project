/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties;

import com.xkcoding.http.config.HttpConfig;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

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
        if (!enable)
        {
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
