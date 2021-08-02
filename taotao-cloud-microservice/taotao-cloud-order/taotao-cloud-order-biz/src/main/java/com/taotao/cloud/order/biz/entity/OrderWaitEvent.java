package com.taotao.cloud.order.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单定时任务处理表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:53
 */
@Entity
@Table(name = "tt_order_wait_event")
@org.hibernate.annotations.Table(appliesTo = "tt_order_wait_event", comment = "订单定时任务处理表")
public class OrderWaitEvent extends BaseEntity {

    /**
     * 事件类型
     */
    private Short eventType;

    /**
     * 事件状态；1--已处理；0--待处理
     */
    private Short eventStatus = 0;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 事件处理结果
     */
    private String eventResult;

    private String refundCode;
}
