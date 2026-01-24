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

package com.taotao.cloud.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 网关配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:15
 */
@RefreshScope
@ConfigurationProperties(prefix = ApiProperties.PREFIX)
public class ApiProperties {

    public static final String PREFIX = "taotao.cloud.gateway.api";

    /**
     * 网关基础路由前缀
     */
    private String prefix = "/api";

    /**
     * 网关基础路由版本
     */
    private String version = "/v1.0";

    /**
     * 网关基础路由uri
     */
    private String baseUri = prefix + version;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
