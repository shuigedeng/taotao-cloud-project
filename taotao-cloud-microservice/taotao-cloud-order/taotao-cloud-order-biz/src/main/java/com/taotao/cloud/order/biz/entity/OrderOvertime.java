package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 订单超时信息表
 *
 * @author dengtao
 * @date 2020/4/30 15:44
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_order_overtime")
@org.hibernate.annotations.Table(appliesTo = "tt_order_overtime", comment = "订单超时信息表")
public class OrderOvertime extends BaseEntity {

    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name", columnDefinition = "varchar(32) not null comment '收货人姓名'")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone", columnDefinition = "varchar(32) not null comment '收货人电话'")
    private String receiverPhone;

    /**
     * 支付时间--支付成功后的时间
     */
    @Column(name = "pay_success_time", columnDefinition = "TIMESTAMP comment '支付时间--支付成功后的时间'")
    private LocalDateTime paySuccessTime;

    /**
     * 超时类型
     */
    @Column(name = "type", columnDefinition = "int not null default 0 comment '超时类型 0-未支付超时 1-未处理售后超时'")
    private Integer type;

    /**
     * 超时时间
     */
    @Column(name = "over_time", columnDefinition = "TIMESTAMP not null comment '超时时间'")
    private LocalDateTime overTime;

}
