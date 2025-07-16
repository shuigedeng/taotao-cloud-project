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

package com.taotao.cloud.monitor.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * alert hook
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class AlertMessage implements Serializable {

    /**
     * 版本号
     */
    private String version;

    /**
     * 由于 “max_alerts” 而截断了多少警报
     */
    private Integer truncatedAlerts;

    /**
     * 分组 key
     */
    private String groupKey;

    /**
     * 状态 resolved|firing
     */
    private String status;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 分组 labels
     */
    private Map<String, String> groupLabels;

    /**
     * 通用 label
     */
    private Map<String, String> commonLabels;

    /**
     * 通用注解
     */
    private Map<String, String> commonAnnotations;

    /**
     * 扩展 url 地址
     */
    private String externalURL;

    /**
     * alerts
     */
    private List<AlertInfo> alerts;

    public AlertMessage() {}

    public AlertMessage(
            String version,
            Integer truncatedAlerts,
            String groupKey,
            String status,
            String receiver,
            Map<String, String> groupLabels,
            Map<String, String> commonLabels,
            Map<String, String> commonAnnotations,
            String externalURL,
            List<AlertInfo> alerts) {
        this.version = version;
        this.truncatedAlerts = truncatedAlerts;
        this.groupKey = groupKey;
        this.status = status;
        this.receiver = receiver;
        this.groupLabels = groupLabels;
        this.commonLabels = commonLabels;
        this.commonAnnotations = commonAnnotations;
        this.externalURL = externalURL;
        this.alerts = alerts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTruncatedAlerts() {
        return truncatedAlerts;
    }

    public void setTruncatedAlerts(Integer truncatedAlerts) {
        this.truncatedAlerts = truncatedAlerts;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Map<String, String> getGroupLabels() {
        return groupLabels;
    }

    public void setGroupLabels(Map<String, String> groupLabels) {
        this.groupLabels = groupLabels;
    }

    public Map<String, String> getCommonLabels() {
        return commonLabels;
    }

    public void setCommonLabels(Map<String, String> commonLabels) {
        this.commonLabels = commonLabels;
    }

    public Map<String, String> getCommonAnnotations() {
        return commonAnnotations;
    }

    public void setCommonAnnotations(Map<String, String> commonAnnotations) {
        this.commonAnnotations = commonAnnotations;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    public List<AlertInfo> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertInfo> alerts) {
        this.alerts = alerts;
    }

    @Override
    public String toString() {
        return "AlertMessage{"
                + "version='"
                + version
                + '\''
                + ", truncatedAlerts="
                + truncatedAlerts
                + ", groupKey='"
                + groupKey
                + '\''
                + ", status='"
                + status
                + '\''
                + ", receiver='"
                + receiver
                + '\''
                + ", groupLabels="
                + groupLabels
                + ", commonLabels="
                + commonLabels
                + ", commonAnnotations="
                + commonAnnotations
                + ", externalURL='"
                + externalURL
                + '\''
                + ", alerts="
                + alerts
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
        AlertMessage that = (AlertMessage) o;
        return Objects.equals(version, that.version)
                && Objects.equals(truncatedAlerts, that.truncatedAlerts)
                && Objects.equals(groupKey, that.groupKey)
                && Objects.equals(status, that.status)
                && Objects.equals(receiver, that.receiver)
                && Objects.equals(groupLabels, that.groupLabels)
                && Objects.equals(commonLabels, that.commonLabels)
                && Objects.equals(commonAnnotations, that.commonAnnotations)
                && Objects.equals(externalURL, that.externalURL)
                && Objects.equals(alerts, that.alerts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                truncatedAlerts,
                groupKey,
                status,
                receiver,
                groupLabels,
                commonLabels,
                commonAnnotations,
                externalURL,
                alerts);
    }
}
