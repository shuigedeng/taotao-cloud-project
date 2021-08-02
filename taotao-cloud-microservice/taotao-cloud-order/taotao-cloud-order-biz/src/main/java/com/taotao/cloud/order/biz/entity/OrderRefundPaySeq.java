package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Table;

/**
 * 退款流水表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:46
 */
//@Entity
@Table(name = "tt_order_refund_pay_seq")
@org.hibernate.annotations.Table(appliesTo = "tt_order_refund_pay_seq", comment = "退款流水表")
public class OrderRefundPaySeq extends BaseEntity {

	/**
	 * 售后申请ID
	 */
	private String refundCode;

	/**
	 * 管家审核日期
	 */
	private LocalDateTime stewardAuditDate;

	/**
	 * 管家id
	 */
	private Long stewardId;

	/**
	 * 退款金额
	 */
	private BigDecimal amount = new BigDecimal(0);

	/**
	 * 微信退款ID
	 */
	private String wxRefundId;

	/**
	 * 微信退款渠道 需要通过微信 “查询退款”接口设置
	 */
	private String wxRefundChanel;

	/**
	 * 微信退款状态 需要通过微信 “查询退款”接口设置
	 */
	private String wxRefundStatus;

	/**
	 * 微信退款收款账户 需要通过微信 “查询退款”接口设置
	 */
	private String wxRefundTarget;

	/**
	 * 退款时间
	 */
	private LocalDateTime refundDate;

	/**
	 * 创建日期
	 */
	private LocalDateTime createDate;

}
