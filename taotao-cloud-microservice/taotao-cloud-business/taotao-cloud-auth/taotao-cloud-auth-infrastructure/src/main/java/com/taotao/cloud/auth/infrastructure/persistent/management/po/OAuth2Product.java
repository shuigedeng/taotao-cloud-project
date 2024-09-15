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

package com.taotao.cloud.auth.infrastructure.persistent.management.po;

import com.google.common.base.MoreObjects;
import com.taotao.boot.data.jpa.tenant.BaseEntity;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>物联网产品 </p>
 *
 *
 * @since : 2023/5/15 14:26
 */
@Schema(name = "物联网产品")
@Entity
@Table(
        name = "oauth2_product",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_key"})},
        indexes = {
            @Index(name = "oauth2_product_pid_idx", columnList = "product_id"),
            @Index(name = "oauth2_product_ipk_idx", columnList = "product_key")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_IOT_PRODUCT)
public class OAuth2Product extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "product_id", length = 64)
    private String productId;

    @Column(name = "product_key", length = 32, unique = true)
    private String productKey;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("productId", productId)
                .add("productKey", productKey)
                .toString();
    }
}
