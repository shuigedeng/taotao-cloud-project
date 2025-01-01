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
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 供求单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:23
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = PurchaseOrderPO.TABLE_NAME)
@TableName(PurchaseOrderPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = PurchaseOrder.TABLE_NAME, comment = "供求单表")
public class PurchaseOrderPO extends BaseSuperEntity<PurchaseOrderPO, Long> {

    public static final String TABLE_NAME = "tt_purchase_order";
    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
    private String title;
    /** 截止时间 */
    @Column(name = "deadline", columnDefinition = "datetime not null comment '截止时间'")
    private LocalDateTime deadline;
    /** 收货时间 */
    @Column(name = "receipt_time", columnDefinition = "datetime not null comment '收货时间'")
    private LocalDateTime receiptTime;
    /** 价格类型 */
    @Column(name = "price_method", columnDefinition = "varchar(255) not null comment '价格类型 可议价、不可议价、面议'")
    private String priceMethod;
    /** 地址名称 逗号分割 */
    @Column(name = "consignee_address_path", columnDefinition = "varchar(255) not null comment '地址名称 逗号分割'")
    private String consigneeAddressPath;
    /** 地址id 逗号分割 */
    @Column(name = "consignee_address_id_path", columnDefinition = "varchar(255) not null comment '地址id 逗号分割'")
    private String consigneeAddressIdPath;
    /** 是否需要发票 */
    @Column(name = "need_receipt", columnDefinition = "varchar(255) not null comment '是否需要发票'")
    private Boolean needReceipt;
    /** 补充说明 */
    @Column(name = "supplement", columnDefinition = "varchar(255) not null comment '补充说明'")
    private String supplement;
    /** 联系类型 */
    @Column(name = "contact_type", columnDefinition = "varchar(255) not null comment '联系方式什么时候可见 公开后、公开'")
    private String contactType;

    /** 联系人 */
    @Column(name = "contacts", columnDefinition = "varchar(255) not null comment '联系人'")
    private String contacts;
    /** 联系电话 */
    @Column(name = "contact_mumber", columnDefinition = "varchar(255) not null comment '联系电话'")
    private String contactNumber;

    /** 供求人 */
    @Column(name = "member_id", columnDefinition = "varchar(255) not null comment '供求人'")
    private String memberId;

    /** 状态，开启：OPEN，关闭：CLOSE */
    @Column(name = "status", columnDefinition = "varchar(255) not null comment '状态，开启：OPEN，关闭：CLOSE'")
    private String status;
    /** 分类ID */
    @Column(name = "category_id", columnDefinition = "varchar(255) not null comment '分类ID'")
    private String categoryId;
    /** 分类名称 */
    @Column(name = "category_name", columnDefinition = "varchar(255) not null comment '分类名称'")
    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        PurchaseOrderPO purchaseOrderPO = (PurchaseOrderPO) o;
        return getId() != null && Objects.equals(getId(), purchaseOrderPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
