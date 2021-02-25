package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import com.taotao.cloud.order.api.constant.OrderConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息表
 *
 * @author dengtao
 * @date 2020/4/30 15:37
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_order")
@org.hibernate.annotations.Table(appliesTo = "tt_order", comment = "订单信息表")
public class Order extends BaseEntity {
	/**
	 * 买家ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "bigint not null comment '买家ID'")
	private Long memberId;

	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id", columnDefinition = "bigint comment '优惠券id'")
	private Long couponId;

	/**
	 * 秒杀活动id
	 */
	@Column(name = "seckill_id", columnDefinition = "bigint comment '秒杀活动id'")
	private Long seckillId;

	/**
	 * 订单编码
	 */
	@Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '订单编码'")
	private String code;

	/**
	 * 订单金额
	 */
	@Builder.Default
	@Column(name = "amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '订单金额'")
	private BigDecimal amount = BigDecimal.ZERO;

	/**
	 * 优惠金额
	 */
	@Builder.Default
	@Column(name = "discount_amount", columnDefinition = "decimal(10,2) default 0 comment '优惠金额'")
	private BigDecimal discountAmount = BigDecimal.ZERO;

	/**
	 * 实际支付金额
	 */
	@Builder.Default
	@Column(name = "actual_mount", columnDefinition = "decimal(10,2) default 0 comment '实际支付金额'")
	private BigDecimal actualAmount = BigDecimal.ZERO;

	/**
	 * 支付时间--支付成功后的时间
	 */
	@Column(name = "pay_success_time", columnDefinition = "TIMESTAMP comment '支付时间--支付成功后的时间'")
	private LocalDateTime paySuccessTime;

	/**
	 * 订单主状态
	 *
	 * @see OrderConstant
	 */
	@Column(name = "main_status", columnDefinition = "int not null comment '订单主状态'")
	private Integer mainStatus;

	/**
	 * 订单子状态
	 *
	 * @see OrderConstant
	 */
	@Column(name = "child_status", columnDefinition = "int not null comment '订单子状态'")
	private Integer childStatus;

	/**
	 * 售后主状态
	 */
	@Builder.Default
	@Column(name = "refund_main_status", columnDefinition = "int not null default 0 comment '售后主状态'")
	private Integer refundMainStatus = 0;

	/**
	 * 售后子状态
	 */
	@Builder.Default
	@Column(name = "refund_child_status", columnDefinition = "int not null default 0 comment '售后子状态'")
	private Integer refundChildStatus = 0;

	/**
	 * 是否可评价
	 * <br/>不可评价 --0
	 * <br/>可评价 --1
	 * <br/>可追评 --2
	 */
	@Builder.Default
	@Column(name = "evaluate_status", columnDefinition = "int not null default 0 comment '评价状态 0-不可评价 1-可评价 2-可追评'")
	private Integer evaluateStatus = 0;

	/**
	 * 申请售后code
	 */
	@Column(name = "refund_code", unique = true, columnDefinition = "varchar(32) comment '申请售后code'")
	private String refundCode;

	/**
	 * 申请售后是否撤销 1--已撤销 0--未撤销
	 */
	@Builder.Default
	@Column(name = "has_cancel", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '申请售后是否撤销 1-已撤销 0-未撤销'")
	private Boolean hasCancel = false;

	/**
	 * 发货时间
	 */
	@Column(name = "ship_time", columnDefinition = "TIMESTAMP comment '发货时间'")
	private LocalDateTime shipTime;

	/**
	 * 收货时间
	 */
	@Column(name = "receipt_time", columnDefinition = "TIMESTAMP comment '收货时间'")
	private LocalDateTime receiptTime;

	/**
	 * 交易结束时间--(1.每天00：15定时任务订单自动取消时间 2.用户收货后收货时间)
	 */
	@Column(name = "trade_end_time", columnDefinition = "TIMESTAMP comment '交易结束时间'")
	private LocalDateTime tradeEndTime;

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
	 * 收货地址:json的形式存储
	 * {"province":"省","city":"市","zone":"区","detail":"详细地址"}
	 */
	@Column(name = "receiver_address_json", columnDefinition = "varchar(2550) not null comment '收货地址:json的形式存储'")
	private String receiverAddressJson;

	/**
	 * 冗余收货地址字符串
	 */
	@Column(name = "receiver_address", columnDefinition = "varchar(2550) comment '冗余收货地址字符串'")
	private String receiverAddress;

	/**
	 * 买家留言
	 */
	@Column(name = "member_msg", columnDefinition = "varchar(255) comment '买家留言'")
	private String memberMsg;

	/**
	 * 取消订单说明
	 */
	@Column(name = "cancel_msg", columnDefinition = "varchar(255) comment '取消订单说明'")
	private String cancelMsg;

	/**
	 * 物流公司code
	 */
	@Column(name = "express_code", columnDefinition = "varchar(32) comment '物流公司code'")
	private String expressCode;

	/**
	 * 物流公司名称
	 */
	@Column(name = "express_name", columnDefinition = "varchar(32) comment '物流公司名称'")
	private String expressName;

	/**
	 * 物流单号
	 */
	@Column(name = "express_number", columnDefinition = "varchar(32) comment '物流单号'")
	private String expressNumber;

	/**
	 * 买家IP
	 */
	@Column(name = "member_ip", columnDefinition = "varchar(32) comment '买家IP'")
	private String memberIp;

	/**
	 * 是否结算 0-未结算，1-已结算
	 */
	@Builder.Default
	@Column(name = "has_settlement", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否结算 0-未结算，1-已结算'")
	private Boolean hasSettlement = false;

	/**
	 * 订单类型
	 */
	@Builder.Default
	@Column(name = "type", columnDefinition = "int not null default 0 comment '订单类型 0-普通订单 1-秒杀订单'")
	private Integer type = 0;

	/**
	 * 条形码
	 */
	@Column(name = "bar_code", columnDefinition = "varchar(32) comment '条形码'")
	private String barCode;

}
