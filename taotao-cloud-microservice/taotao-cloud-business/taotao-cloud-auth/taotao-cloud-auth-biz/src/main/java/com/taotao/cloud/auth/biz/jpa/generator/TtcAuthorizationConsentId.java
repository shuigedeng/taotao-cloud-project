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

package com.taotao.cloud.auth.biz.jpa.generator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;

/**
 * <p>OAuth2AuthorizationConsent 联合主键 </p>
 * <p>
 * 复合主键类必须满足： 1. 实现Serializable接口; 2. 有默认的public无参数的构造方法; 3.
 * 重写equals和hashCode方法。equals方法用于判断两个对象是否相同，
 */
public class TtcAuthorizationConsentId implements Serializable {

    /**
     * 这里一定要注意：
     * <p>
     * 属性名：这里的属性名，一定要与中间表中，ManyToOne的属性名称一致 类型：虽然名称一致，但是类型可以不一致，此处的String类型，说明属性对应数据库字段的类型为String
     */
    private String registeredClientId;

    private String principalName;

    public TtcAuthorizationConsentId() {}

    public TtcAuthorizationConsentId(String registeredClientId, String principalName) {
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
    }

    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TtcAuthorizationConsentId that = (TtcAuthorizationConsentId) o;
        return Objects.equal(registeredClientId, that.registeredClientId)
                && Objects.equal(principalName, that.principalName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(registeredClientId, principalName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("registeredClientId", registeredClientId)
                .add("principalName", principalName)
                .toString();
    }
}
