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

package com.taotao.cloud.auth.biz.demo.core.definition.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Set;

/**
 * Description: Security Metadata 传输数据实体
 *
 * @author : gengwei.zheng
 * @date : 2021/8/8 15:51
 */
public class SecurityAttribute implements Serializable {

    private String attributeId;

    private String attributeCode;

    private String attributeName;

    private String expression;

    private String manualSetting;

    private String ipAddress;

    private String url;

    private String requestMethod;

    private String serviceId;

    private Set<HerodotusGrantedAuthority> roles;

    public SecurityAttribute() {}

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getManualSetting() {
        return manualSetting;
    }

    public void setManualSetting(String manualSetting) {
        this.manualSetting = manualSetting;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Set<HerodotusGrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Set<HerodotusGrantedAuthority> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityAttribute that = (SecurityAttribute) o;
        return Objects.equal(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributeId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("attributeId", attributeId)
                .add("attributeCode", attributeCode)
                .add("attributeName", attributeName)
                .add("expression", expression)
                .add("manualSetting", manualSetting)
                .add("ipAddress", ipAddress)
                .add("url", url)
                .add("requestMethod", requestMethod)
                .add("serviceId", serviceId)
                .toString();
    }
}
