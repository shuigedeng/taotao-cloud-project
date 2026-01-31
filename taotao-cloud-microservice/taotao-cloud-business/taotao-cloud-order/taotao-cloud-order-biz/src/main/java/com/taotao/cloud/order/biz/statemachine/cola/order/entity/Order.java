package com.taotao.cloud.order.biz.statemachine.cola.order.entity;

import com.taotao.cloud.order.biz.statemachine.cola.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * Order
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_order")
public class Order {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "serial_no", length = 30, nullable = false)
    private String serialNo;

    /**
     * 订单状态
     */
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 创建人
     */
    @Column(name = "create_by", length = 30, nullable = false)
    private String createBy;


}
