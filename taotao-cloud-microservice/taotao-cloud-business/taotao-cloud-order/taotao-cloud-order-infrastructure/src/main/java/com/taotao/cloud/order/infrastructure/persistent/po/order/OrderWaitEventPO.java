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
 * 订单定时任务处理表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:59
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderWaitEventPO.TABLE_NAME)
@Table(name = OrderWaitEventPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderWaitEventPO.TABLE_NAME, comment = "订单定时任务处理表")
public class OrderWaitEventPO extends BaseSuperEntity<OrderWaitEventPO, Long> {

    public static final String TABLE_NAME = "order_wait_event";

    /** 事件类型 */
    @Column(name = "event_type", columnDefinition = "int not null default 0 comment '事件类型'")
    private Integer eventType = 0;

    /** 事件状态；1--已处理；0--待处理 */
    @Column(name = "event_status", columnDefinition = "int not null default 0 comment '事件状态；1--已处理；0--待处理'")
    private Integer eventStatus = 0;

    /** 触发时间 */
    @Column(name = "trigger_time", columnDefinition = "datetime comment '触发时间'")
    private LocalDateTime triggerTime;

    /** 事件处理结果 */
    @Column(name = "event_result", columnDefinition = "varchar(256) not null comment '事件处理结果'")
    private String eventResult;

    @Column(name = "refund_code", columnDefinition = "varchar(256) not null comment 'refundCode'")
    private String refundCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderWaitEventPO that = (OrderWaitEventPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
