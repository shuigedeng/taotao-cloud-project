/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.job.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * XxlProperties
 *
 * @author dengtao
 * @since 2020/6/16 11:36
 * @version 1.0.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.xxl.job")
public class XxlProperties {
    /**
     * job开关,默认为false，非必填
     */
    private boolean enabled = false;

    /**
     * springAppName
     */
    @Value("${spring.application.name:}")
    private String springAppName;

    /**
     * xxljob地址，必填
     */
    private String adminAddresses;

    /**
     * xxljob地址，必填
     */
    private String address;

    /**
     * 执行器名称，非必填，默认等于spring.application.name
     */
    @Value("${spring.application.name:}")
    private String appName;

    /**
     * 执行器ip，必填，默认本机ip，如果存在多网卡，则必填，否在可能会导致调度中心无法连接到此执行器）
     */
    private String ip;

    /**
     * 匹配网段
     */
    private String ipRegx;

    /**
     * 排除网段
     */
    private String ipExgx;

    /**
     * 执行器端口，非必填，默认9999，如果单机部署多个执行器，则分别指定
     */
    private int port = 9999;

    /**
     * 非必填，如果调度中心配置，此处需要配置
     */
    private String accessToken;

    /**
     * 非必填，任务日志目录，默认job-logs/）
     */
    private String logPath;

    /**
     * 非必填，默认为7，任务日志保存天数）
     */
    private int logRetentionDays = 7;
}
