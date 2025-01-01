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

package com.taotao.cloud.order.infrastructure.persistent.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
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
 * 发票表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:01
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ReceiptPO.TABLE_NAME)
@TableName(ReceiptPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = ReceiptPO.TABLE_NAME, comment = "发票表")
public class ReceiptPO extends BaseSuperEntity<ReceiptPO, Long> {

    public static final String TABLE_NAME = "tt_receipt";

    @Serial
    private static final long serialVersionUID = -8210927482915675995L;

    /** 订单编号 */
    @Column(name = "order_sn", columnDefinition = "varchar(64) not null comment '订单编号'")
    private String orderSn;
    /** 发票抬头 */
    @Column(name = "receipt_title", columnDefinition = "varchar(64) not null comment '发票抬头'")
    private String receiptTitle;
    /** 纳税人识别号 */
    @Column(name = "taxpayer_id", columnDefinition = "varchar(64) not null comment '纳税人识别号'")
    private String taxpayerId;
    /** 发票内容 */
    @Column(name = "receipt_content", columnDefinition = "varchar(64) not null comment '发票内容'")
    private String receiptContent;
    /** 发票金额 */
    @Column(name = "receipt_price", columnDefinition = "varchar(64) not null comment '发票金额'")
    private BigDecimal receiptPrice;
    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    private Long memberId;
    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(64) not null comment '会员名称'")
    private String memberName;
    /** 商家ID */
    @Column(name = "store_id", columnDefinition = "varchar(64) not null comment '商家ID'")
    private Long storeId;
    /** 商家名称 */
    @Column(name = "store_name", columnDefinition = "varchar(64) not null comment '商家名称'")
    private String storeName;
    /** 发票状态 0未开 1已开 */
    @Column(name = "receipt_status", columnDefinition = "varchar(64) not null comment '发票状态 0未开 1已开'")
    private Integer receiptStatus;
    /** 发票详情 */
    @Column(name = "receipt_detail", columnDefinition = "varchar(64) not null comment '发票详情'")
    private String receiptDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ReceiptPO receiptPO = (ReceiptPO) o;
        return getId() != null && Objects.equals(getId(), receiptPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
