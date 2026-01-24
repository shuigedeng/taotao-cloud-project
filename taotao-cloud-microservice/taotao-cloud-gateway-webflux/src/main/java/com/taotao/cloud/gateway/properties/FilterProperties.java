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
 * 过滤器配置
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-08 09:25:49
 */
@RefreshScope
@ConfigurationProperties(prefix = FilterProperties.PREFIX)
public class FilterProperties {

    public static final String PREFIX = "taotao.cloud.gateway.filter";

    /**
     * 是否开启日志链路追踪
     */
    private Boolean trace = true;

    /**
     * 是否开启日志打印
     */
    private Boolean globalLog = false;

    /**
     * 是否开启日志打印
     */
    private Boolean requestLog = false;

    /**
     * 是否开启日志打印
     */
    private Boolean log = true;

    /**
     * 是否启用灰度发布
     */
    private Boolean gray = true;

    /**
     * 是否启用黑名单
     */
    private Boolean blacklist = true;

    /**
     * 是否启用黑名单
     */
    private Boolean sign = true;

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public Boolean getLog() {
        return log;
    }

    public void setLog(Boolean log) {
        this.log = log;
    }

    public Boolean getGray() {
        return gray;
    }

    public void setGray(Boolean gray) {
        this.gray = gray;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public Boolean getGlobalLog() {
        return globalLog;
    }

    public void setGlobalLog(Boolean globalLog) {
        this.globalLog = globalLog;
    }

    public Boolean getRequestLog() {
        return requestLog;
    }

    public void setRequestLog(Boolean requestLog) {
        this.requestLog = requestLog;
    }
}
