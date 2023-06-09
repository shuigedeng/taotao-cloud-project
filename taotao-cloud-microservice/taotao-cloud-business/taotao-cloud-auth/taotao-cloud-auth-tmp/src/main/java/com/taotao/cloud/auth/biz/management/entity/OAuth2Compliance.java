/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.taotao.cloud.data.jpa.tenant.BaseEntity;
import com.taotao.cloud.security.springsecurity.core.constants.OAuth2Constants;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>Description: 用户操作审计 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/7 18:55
 */
@Entity
@Table(name = "oauth2_compliance", indexes = {
        @Index(name = "oauth2_compliance_id_idx", columnList = "compliance_id")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = OAuth2Constants.REGION_OAUTH2_COMPLIANCE)
public class OAuth2Compliance extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "compliance_id", length = 64)
    private String complianceId;

    @Column(name = "principal_name", length = 128)
    private String principalName;

    @Column(name = "client_id", length = 100)
    private String clientId;

    @Column(name = "ip_address", length = 20)
    private String ip;

    @Column(name = "is_mobile")
    private Boolean mobile = false;

    @Column(name = "os_name", length = 200)
    private String osName;

    @Column(name = "browser_name", length = 50)
    private String browserName;
    @Column(name = "is_mobile_browser")
    private Boolean mobileBrowser = false;

    @Column(name = "engine_name", length = 50)
    private String engineName;

    @Column(name = "is_mobile_platform")
    private Boolean mobilePlatform = false;

    @Column(name = "is_iphone_or_ipod")
    private Boolean iphoneOrIpod = false;

    @Column(name = "is_ipad")
    private Boolean ipad = false;

    @Column(name = "is_ios")
    private Boolean ios = false;

    @Column(name = "is_android")
    private Boolean android = false;

    @Column(name = "operation")
    private String operation;

    public String getComplianceId() {
        return complianceId;
    }

    public void setComplianceId(String complianceId) {
        this.complianceId = complianceId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public Boolean getMobileBrowser() {
        return mobileBrowser;
    }

    public void setMobileBrowser(Boolean mobileBrowser) {
        this.mobileBrowser = mobileBrowser;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public Boolean getMobilePlatform() {
        return mobilePlatform;
    }

    public void setMobilePlatform(Boolean mobilePlatform) {
        this.mobilePlatform = mobilePlatform;
    }

    public Boolean getIphoneOrIpod() {
        return iphoneOrIpod;
    }

    public void setIphoneOrIpod(Boolean iphoneOrIpod) {
        this.iphoneOrIpod = iphoneOrIpod;
    }

    public Boolean getIpad() {
        return ipad;
    }

    public void setIpad(Boolean ipad) {
        this.ipad = ipad;
    }

    public Boolean getIos() {
        return ios;
    }

    public void setIos(Boolean ios) {
        this.ios = ios;
    }

    public Boolean getAndroid() {
        return android;
    }

    public void setAndroid(Boolean android) {
        this.android = android;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuth2Compliance that = (OAuth2Compliance) o;
        return Objects.equal(complianceId, that.complianceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(complianceId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("complianceId", complianceId)
                .add("principalName", principalName)
                .add("clientId", clientId)
                .add("ip", ip)
                .add("mobile", mobile)
                .add("osName", osName)
                .add("browserName", browserName)
                .add("mobileBrowser", mobileBrowser)
                .add("engineName", engineName)
                .add("mobilePlatform", mobilePlatform)
                .add("iphoneOrIpod", iphoneOrIpod)
                .add("ipad", ipad)
                .add("ios", ios)
                .add("android", android)
                .add("operation", operation)
                .toString();
    }
}
