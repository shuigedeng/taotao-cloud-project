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

import cn.hutool.core.collection.CollUtil;
import com.taotao.boot.common.utils.context.ContextUtils;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证权限配置
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-12 11:11:45
 */
@RefreshScope
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
@AutoConfigureBefore(ApiProperties.class)
public class SecurityProperties {

    public static final String PREFIX = "taotao.cloud.gateway.security";

    public static final String[] ENDPOINTS = {
        "/actuator/**",
        "/v3/**",
        "/*/v3/**",
        "/fallback",
        "/favicon.ico",
        "/startup-report",
        "/swagger-resources/**",
        "/webjars/**",
        "/druid/**",
        "/*/*.html",
        "/*/*.css",
        "/*/*.js",
        "/*.js",
        "/*.css",
        "/*.html",
        "/*/favicon.ico",
        "/*/api-docs",
        "/health/**",
        "/css/**",
        "/js/**",
        "/k8s/**",
        "/k8s",
        "/doc.html",
        "/tt/**",
        "/images/**"
    };

    /**
     * 是否启用网关鉴权模式
     */
    private Boolean enabled = true;

    /**
     * 忽略URL，List列表形式
     */
    private List<String> ignoreUrl = new ArrayList<>();

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreUrl() {
        ApiProperties apiProperties = ContextUtils.getBean(ApiProperties.class, true);
        if (Objects.nonNull(apiProperties)) {
            String baseUri = apiProperties.getBaseUri();
            ignoreUrl =
                    (List<String>)
                            CollUtil.addAll(
                                    new ArrayList<>(
                                            ignoreUrl.stream().map(url -> baseUri + url).toList()),
                                    ENDPOINTS);
        }
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(List<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }
}
