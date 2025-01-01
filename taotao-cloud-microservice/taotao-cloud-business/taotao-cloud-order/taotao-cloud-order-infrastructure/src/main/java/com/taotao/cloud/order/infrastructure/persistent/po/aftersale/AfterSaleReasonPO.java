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

package com.taotao.cloud.order.infrastructure.persistent.po.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 售后原因
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSaleReasonPO.TABLE_NAME)
@TableName(AfterSaleReasonPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = AfterSaleReason.TABLE_NAME, comment = "售后原因")
public class AfterSaleReasonPO extends BaseSuperEntity<AfterSaleReasonPO, Long> {

    public static final String TABLE_NAME = "tt_after_sale_reason";

    /** 售后原因 */
    @Column(name = "reason", columnDefinition = "varchar(1024) not null comment '售后原因'")
    private String reason;

    /**
     * 售后类型
     *
     * @see AfterSaleTypeEnum
     */
    @Column(name = "service_type", columnDefinition = "varchar(64) not null comment '售后类型'")
    private String serviceType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AfterSaleReasonPO that = (AfterSaleReasonPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
