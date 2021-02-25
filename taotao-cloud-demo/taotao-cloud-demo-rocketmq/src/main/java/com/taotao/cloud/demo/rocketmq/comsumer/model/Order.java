package com.taotao.cloud.demo.rocketmq.comsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Order implements Serializable {
	private static final long serialVersionUID = 2801814838883246461L;

	private Long orderId;
	private String orderNo;
}
