package com.taotao.cloud.rocketmq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 演示订单表
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 2011242218927120008L;

	private Long id;

	private Long tradeId;

	private Long goodsId;

	private BigDecimal goodsPrice;

	private Integer number;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
