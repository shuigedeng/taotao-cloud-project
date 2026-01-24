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

package com.taotao.cloud.gateway.model;

import java.util.Objects;

/**
 * 黑名单工具类
 */
public class BlackList {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 截止时间
     */
    private String endTime;

    /**
     * 黑名单状态：1:开启　0:关闭
     */
    private String status;

    /**
     * 创建时间
     */
    private String createTime;

    public BlackList() {}

    public BlackList(
            Long id,
            String ip,
            String requestUri,
            String requestMethod,
            String startTime,
            String endTime,
            String status,
            String createTime) {
        this.id = id;
        this.ip = ip;
        this.requestUri = requestUri;
        this.requestMethod = requestMethod;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlackList{"
                + "id="
                + id
                + ", ip='"
                + ip
                + '\''
                + ", requestUri='"
                + requestUri
                + '\''
                + ", requestMethod='"
                + requestMethod
                + '\''
                + ", startTime='"
                + startTime
                + '\''
                + ", endTime='"
                + endTime
                + '\''
                + ", status='"
                + status
                + '\''
                + ", createTime='"
                + createTime
                + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlackList blackList = (BlackList) o;
        return Objects.equals(id, blackList.id)
                && Objects.equals(ip, blackList.ip)
                && Objects.equals(requestUri, blackList.requestUri)
                && Objects.equals(requestMethod, blackList.requestMethod)
                && Objects.equals(startTime, blackList.startTime)
                && Objects.equals(endTime, blackList.endTime)
                && Objects.equals(status, blackList.status)
                && Objects.equals(createTime, blackList.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, ip, requestUri, requestMethod, startTime, endTime, status, createTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
