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
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 订单交易投诉通信表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:29
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderComplaintCommunicationPO.TABLE_NAME)
@TableName(OrderComplaintCommunicationPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderComplaintCommunicationPO.TABLE_NAME, comment = "订单交易投诉通信表")
public class OrderComplaintCommunicationPO extends BaseSuperEntity<OrderComplaintCommunicationPO, Long> {

    public static final String TABLE_NAME = "tt_order_complaint_communication";

    @Serial
    private static final long serialVersionUID = -2384351827382795547L;

    /** 投诉id */
    @Column(name = "complain_id", columnDefinition = "varchar(64) not null comment '投诉id'")
    private Long complainId;
    /** 对话内容 */
    @Column(name = "content", columnDefinition = "varchar(64) not null comment '对话内容'")
    private String content;
    /** 所属，买家/卖家 */
    @Column(name = "owner", columnDefinition = "varchar(64) not null comment '所属，买家/卖家'")
    private String owner;
    /** 对话所属名称 */
    @Column(name = "owner_name", columnDefinition = "varchar(64) not null comment '对话所属名称'")
    private String ownerName;
    /** 对话所属id,卖家id/买家id */
    @Column(name = "owner_id", columnDefinition = "varchar(64) not null comment '对话所属id,卖家id/买家id'")
    private Long ownerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderComplaintCommunicationPO that = (OrderComplaintCommunicationPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
