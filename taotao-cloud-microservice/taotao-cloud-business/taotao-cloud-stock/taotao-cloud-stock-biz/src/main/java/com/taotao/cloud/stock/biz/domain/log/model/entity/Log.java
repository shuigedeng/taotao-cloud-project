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

package com.taotao.cloud.stock.biz.domain.log.model.entity;

import com.taotao.cloud.stock.biz.domain.log.model.vo.LogId;
import com.taotao.cloud.stock.biz.domain.model.log.LogId;

/**
 * 日志实体
 *
 * @author shuigedeng
 * @since 2021-02-02
 */
public class Log implements Entity<Log> {

    /** logId */
    private LogId logId;

    /** 用户名 */
    private UserName userName;

    /** 用户操作 */
    private String operation;

    /** 请求方法 */
    private String method;

    /** 请求参数 */
    private String params;

    /** 执行时长(毫秒) */
    private Long time;

    /** IP地址 */
    private String ip;

    /** 租户ID */
    private TenantId tenantId;

    public Log(LogId logId, UserName userName, String operation, String method, String params, Long time, String ip) {
        this.logId = logId;
        this.userName = userName;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.time = time;
        this.ip = ip;
    }

    @Override
    public boolean sameIdentityAs(Log other) {
        return other != null && logId.sameValueAs(other.logId);
    }

    public LogId getLogId() {
        return logId;
    }

    public String getOperation() {
        return operation;
    }

    public String getMethod() {
        return method;
    }

    public String getParams() {
        return params;
    }

    public Long getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public UserName getUserName() {
        return userName;
    }

    public TenantId getTenantId() {
        return tenantId;
    }
}
