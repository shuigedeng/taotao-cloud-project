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

package com.taotao.cloud.auth.biz.demo.server.entity;

import cn.herodotus.engine.data.core.entity.BaseSysEntity;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.server.authentication.generator.OAuth2AuthorityUuid;
import com.google.common.base.MoreObjects;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Description: 客户端权限
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 13:39
 */
@Entity
@Table(
        name = "oauth2_authority",
        indexes = {@Index(name = "oauth2_authority_id_idx", columnList = "authority_id")})
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_AUTHORITY)
public class OAuth2Authority extends BaseSysEntity {

    @Id
    @OAuth2AuthorityUuid
    @Column(name = "authority_id", length = 64)
    private String authorityId;

    @Column(name = "authority_code", length = 128)
    private String authorityCode;

    @Column(name = "service_id", length = 128)
    private String serviceId;

    @Column(name = "request_method", length = 20)
    private String requestMethod;

    @Column(name = "url", length = 2048)
    private String url;

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authorityId", authorityId)
                .add("authorityCode", authorityCode)
                .add("serviceId", serviceId)
                .add("requestMethod", requestMethod)
                .add("url", url)
                .toString();
    }
}
