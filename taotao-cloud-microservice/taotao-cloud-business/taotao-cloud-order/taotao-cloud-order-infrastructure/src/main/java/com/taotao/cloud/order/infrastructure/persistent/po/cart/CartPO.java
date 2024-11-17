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

package com.taotao.cloud.order.infrastructure.persistent.po.cart;

import com.taotao.boot.data.jpa.base.entity.JpaSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 购物车表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:11
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tt_cart")
@org.springframework.data.relational.core.mapping.Table(name = "tt_cart", comment = "购物车表")
public class CartPO extends JpaSuperEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6887296988458221221L;

    /** 申请单号 */
    @Column(name = "code", unique = true, columnDefinition = "varchar(32) not null comment '申请单号'")
    private String code;

    /** 公司ID */
    @Column(name = "company_id", columnDefinition = "bigint not null comment '公司ID'")
    private Long companyId;

    /** 商城ID */
    @Column(name = "mall_id", columnDefinition = "bigint not null comment '商城ID'")
    private Long mallId;

    /** 提现金额 */
    @Column(name = "amount", columnDefinition = "decimal(10,2) not null default 0 comment '提现金额'")
    private BigDecimal amount;

    /** 钱包余额 */
    @Column(name = "balance_amount", columnDefinition = "decimal(10,2) not null default 0 comment '钱包余额'")
    private BigDecimal balanceAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CartPO cartPO = (CartPO) o;
        return getId() != null && Objects.equals(getId(), cartPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
