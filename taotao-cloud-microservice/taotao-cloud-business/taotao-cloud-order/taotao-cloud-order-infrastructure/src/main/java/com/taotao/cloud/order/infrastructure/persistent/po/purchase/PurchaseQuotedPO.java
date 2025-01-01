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

package com.taotao.cloud.order.infrastructure.persistent.po.purchase;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 报价单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:30
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = PurchaseQuotedPO.TABLE_NAME)
@TableName(PurchaseQuotedPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = PurchaseQuoted.TABLE_NAME, comment = "供求单报价表")
public class PurchaseQuotedPO extends BaseSuperEntity<PurchaseQuotedPO, Long> {

    public static final String TABLE_NAME = "tt_purchase_quoted";

    /** 采购单ID */
    @Column(name = "purchase_order_id", columnDefinition = "bigint not null comment '采购单ID'")
    private Long purchaseOrderId;
    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
    private String title;
    /** 报价说明 */
    @Column(name = "context", columnDefinition = "varchar(255) not null comment '报价说明'")
    private String context;
    /** 附件 */
    @Column(name = "annex", columnDefinition = "varchar(255) not null comment '附件'")
    private String annex;
    /** 公司名称 */
    @Column(name = "company_name", columnDefinition = "varchar(255) not null comment '公司名称'")
    private String companyName;
    /** 联系人 */
    @Column(name = "contacts", columnDefinition = "varchar(255) not null comment '联系人'")
    private String contacts;
    /** 联系电话 */
    @Column(name = "contact_number", columnDefinition = "varchar(255) not null comment '联系电话'")
    private String contactNumber;
    /** 报价人 */
    @Column(name = "member_id", columnDefinition = "varchar(255) not null comment '报价人'")
    private String memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        PurchaseQuotedPO purchaseQuotedPO = (PurchaseQuotedPO) o;
        return getId() != null && Objects.equals(getId(), purchaseQuotedPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
