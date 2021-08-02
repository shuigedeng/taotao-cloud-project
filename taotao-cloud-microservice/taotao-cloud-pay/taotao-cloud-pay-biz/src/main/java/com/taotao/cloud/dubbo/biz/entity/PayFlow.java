package com.taotao.cloud.dubbo.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 支付流水表
 *
 * @author shuigedeng
 * @since 2020/11/13 09:46
 * @version 1.0.0
 */
@Entity
@Table(name = "tt_pay_flow")
@org.hibernate.annotations.Table(appliesTo = "tt_pay_flow", comment = "支付流水表")
public class PayFlow extends BaseEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 支付流水号
	 */
	@Column(name = "code", nullable = false, unique = true, updatable = false, columnDefinition = "varchar(32) not null COMMENT '支付流水号'")
	private String code;

	/**
	 * 订单号
	 */
	@Column(name = "order_code", nullable = false, updatable = false, columnDefinition = "varchar(32) not null COMMENT '订单号'")
	private String orderCode;

	/**
	 * 商品id
	 */
	@Column(name = "product_id", nullable = false, columnDefinition = "bigint not null comment '商品id'")
	private Long productId;

	/**
	 * 支付金额
	 */
	@Column(name = "paid_amount", nullable = false, columnDefinition = "decimal(10,2) not null comment '支付金额'")
	private BigDecimal paidAmount;

	/**
	 * 支付方式
	 */
	@Column(name = "paid_method", nullable = false, columnDefinition = "int not null comment '支付方式 1-微信 2-支付宝'")
	private Integer paidMethod;

	/**
	 * 购买个数
	 */
	@Column(name = "buy_count", nullable = false, columnDefinition = "int not null comment '购买个数'")
	private Integer buyCount;

	/**
	 * 支付时间
	 */
	@Column(name = "pay_time", columnDefinition = "TIMESTAMP  comment '支付时间'")
	private LocalDateTime payTime;

}
