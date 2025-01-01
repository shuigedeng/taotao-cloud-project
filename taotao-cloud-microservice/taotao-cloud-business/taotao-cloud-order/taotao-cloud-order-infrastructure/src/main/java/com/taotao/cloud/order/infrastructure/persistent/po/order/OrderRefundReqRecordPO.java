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
 * 售后退款操作记录表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:53
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderRefundReqRecordPO.TABLE_NAME)
@Table(name = OrderRefundReqRecordPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderRefundReqRecordPO.TABLE_NAME, comment = "售后退款操作记录表")
public class OrderRefundReqRecordPO extends BaseSuperEntity<OrderRefundReqRecordPO, Long> {

    public static final String TABLE_NAME = "order_refund_req_record";

    @Column(name = "order_code", columnDefinition = "varchar(32) not null comment '订单编码'")
    private String orderCode;

    @Column(name = "item_code", columnDefinition = "varchar(32) not null comment '订单编码'")
    private String itemCode;

    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(32) not null comment '标题'")
    private String title;

    /** 备注 */
    @Column(name = "remark", columnDefinition = "varchar(3200) null comment '备注'")
    private String remark;

    /** 操作人名称 */
    @Column(name = "create_name", columnDefinition = "varchar(32) not null comment '操作人名称'")
    private String createName;

    /** 操作人id */
    @Column(name = "create_id", columnDefinition = "varchar(32) not null comment '操作人id'")
    private String createId;

    /** 操作人昵称 */
    @Column(name = "create_nick", columnDefinition = "varchar(32) not null comment '操作人昵称'")
    private String createNick;

    /** 扩展信息 */
    @Column(name = "ext", columnDefinition = "text null comment '扩展信息'")
    private String ext;

    @Column(name = "req_record_type", columnDefinition = "int not null default 0 comment '记录类型'")
    private Integer reqRecordType;

    /** 创建时间 */
    @Column(name = "create_Date", columnDefinition = "TIMESTAMP comment '创建时间'")
    private LocalDateTime createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderRefundReqRecordPO that = (OrderRefundReqRecordPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
