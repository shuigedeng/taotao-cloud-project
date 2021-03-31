package com.taotao.cloud.rocketmq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 演示订单表
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
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
