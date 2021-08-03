package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import com.taotao.cloud.order.api.constant.OrderConstant;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单信息表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:37
 */
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
	@Column(name = "amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '订单金额'")
	private BigDecimal amount = BigDecimal.ZERO;

	/**
	 * 优惠金额
	 */
	@Column(name = "discount_amount", columnDefinition = "decimal(10,2) default 0 comment '优惠金额'")
	private BigDecimal discountAmount = BigDecimal.ZERO;

	/**
	 * 实际支付金额
	 */
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
	@Column(name = "refund_main_status", columnDefinition = "int not null default 0 comment '售后主状态'")
	private Integer refundMainStatus = 0;

	/**
	 * 售后子状态
	 */
	@Column(name = "refund_child_status", columnDefinition = "int not null default 0 comment '售后子状态'")
	private Integer refundChildStatus = 0;

	/**
	 * 是否可评价 <br/>不可评价 --0 <br/>可评价 --1 <br/>可追评 --2
	 */
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
	 * 收货地址:json的形式存储 {"province":"省","city":"市","zone":"区","detail":"详细地址"}
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
	@Column(name = "has_settlement", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否结算 0-未结算，1-已结算'")
	private Boolean hasSettlement = false;

	/**
	 * 订单类型
	 */
	@Column(name = "type", columnDefinition = "int not null default 0 comment '订单类型 0-普通订单 1-秒杀订单'")
	private Integer type = 0;

	/**
	 * 条形码
	 */
	@Column(name = "bar_code", columnDefinition = "varchar(32) comment '条形码'")
	private String barCode;


	public Order() {
	}

	public Order(Long memberId, Long couponId, Long seckillId, String code, BigDecimal amount,
		BigDecimal discountAmount, BigDecimal actualAmount, LocalDateTime paySuccessTime,
		Integer mainStatus, Integer childStatus, Integer refundMainStatus,
		Integer refundChildStatus, Integer evaluateStatus, String refundCode,
		Boolean hasCancel, LocalDateTime shipTime, LocalDateTime receiptTime,
		LocalDateTime tradeEndTime, String receiverName, String receiverPhone,
		String receiverAddressJson, String receiverAddress, String memberMsg,
		String cancelMsg, String expressCode, String expressName, String expressNumber,
		String memberIp, Boolean hasSettlement, Integer type, String barCode) {
		this.memberId = memberId;
		this.couponId = couponId;
		this.seckillId = seckillId;
		this.code = code;
		this.amount = amount;
		this.discountAmount = discountAmount;
		this.actualAmount = actualAmount;
		this.paySuccessTime = paySuccessTime;
		this.mainStatus = mainStatus;
		this.childStatus = childStatus;
		this.refundMainStatus = refundMainStatus;
		this.refundChildStatus = refundChildStatus;
		this.evaluateStatus = evaluateStatus;
		this.refundCode = refundCode;
		this.hasCancel = hasCancel;
		this.shipTime = shipTime;
		this.receiptTime = receiptTime;
		this.tradeEndTime = tradeEndTime;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverAddressJson = receiverAddressJson;
		this.receiverAddress = receiverAddress;
		this.memberMsg = memberMsg;
		this.cancelMsg = cancelMsg;
		this.expressCode = expressCode;
		this.expressName = expressName;
		this.expressNumber = expressNumber;
		this.memberIp = memberIp;
		this.hasSettlement = hasSettlement;
		this.type = type;
		this.barCode = barCode;
	}

	@Override
	public String toString() {
		return "Order{" +
			"memberId=" + memberId +
			", couponId=" + couponId +
			", seckillId=" + seckillId +
			", code='" + code + '\'' +
			", amount=" + amount +
			", discountAmount=" + discountAmount +
			", actualAmount=" + actualAmount +
			", paySuccessTime=" + paySuccessTime +
			", mainStatus=" + mainStatus +
			", childStatus=" + childStatus +
			", refundMainStatus=" + refundMainStatus +
			", refundChildStatus=" + refundChildStatus +
			", evaluateStatus=" + evaluateStatus +
			", refundCode='" + refundCode + '\'' +
			", hasCancel=" + hasCancel +
			", shipTime=" + shipTime +
			", receiptTime=" + receiptTime +
			", tradeEndTime=" + tradeEndTime +
			", receiverName='" + receiverName + '\'' +
			", receiverPhone='" + receiverPhone + '\'' +
			", receiverAddressJson='" + receiverAddressJson + '\'' +
			", receiverAddress='" + receiverAddress + '\'' +
			", memberMsg='" + memberMsg + '\'' +
			", cancelMsg='" + cancelMsg + '\'' +
			", expressCode='" + expressCode + '\'' +
			", expressName='" + expressName + '\'' +
			", expressNumber='" + expressNumber + '\'' +
			", memberIp='" + memberIp + '\'' +
			", hasSettlement=" + hasSettlement +
			", type=" + type +
			", barCode='" + barCode + '\'' +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Order order = (Order) o;
		return Objects.equals(memberId, order.memberId) && Objects.equals(couponId,
			order.couponId) && Objects.equals(seckillId, order.seckillId)
			&& Objects.equals(code, order.code) && Objects.equals(amount,
			order.amount) && Objects.equals(discountAmount, order.discountAmount)
			&& Objects.equals(actualAmount, order.actualAmount)
			&& Objects.equals(paySuccessTime, order.paySuccessTime)
			&& Objects.equals(mainStatus, order.mainStatus) && Objects.equals(
			childStatus, order.childStatus) && Objects.equals(refundMainStatus,
			order.refundMainStatus) && Objects.equals(refundChildStatus,
			order.refundChildStatus) && Objects.equals(evaluateStatus,
			order.evaluateStatus) && Objects.equals(refundCode, order.refundCode)
			&& Objects.equals(hasCancel, order.hasCancel) && Objects.equals(
			shipTime, order.shipTime) && Objects.equals(receiptTime, order.receiptTime)
			&& Objects.equals(tradeEndTime, order.tradeEndTime)
			&& Objects.equals(receiverName, order.receiverName)
			&& Objects.equals(receiverPhone, order.receiverPhone)
			&& Objects.equals(receiverAddressJson, order.receiverAddressJson)
			&& Objects.equals(receiverAddress, order.receiverAddress)
			&& Objects.equals(memberMsg, order.memberMsg) && Objects.equals(
			cancelMsg, order.cancelMsg) && Objects.equals(expressCode, order.expressCode)
			&& Objects.equals(expressName, order.expressName) && Objects.equals(
			expressNumber, order.expressNumber) && Objects.equals(memberIp,
			order.memberIp) && Objects.equals(hasSettlement, order.hasSettlement)
			&& Objects.equals(type, order.type) && Objects.equals(barCode,
			order.barCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), memberId, couponId, seckillId, code, amount,
			discountAmount, actualAmount, paySuccessTime, mainStatus, childStatus, refundMainStatus,
			refundChildStatus, evaluateStatus, refundCode, hasCancel, shipTime, receiptTime,
			tradeEndTime, receiverName, receiverPhone, receiverAddressJson, receiverAddress,
			memberMsg, cancelMsg, expressCode, expressName, expressNumber, memberIp, hasSettlement,
			type, barCode);
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public LocalDateTime getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(LocalDateTime paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public Integer getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(Integer mainStatus) {
		this.mainStatus = mainStatus;
	}

	public Integer getChildStatus() {
		return childStatus;
	}

	public void setChildStatus(Integer childStatus) {
		this.childStatus = childStatus;
	}

	public Integer getRefundMainStatus() {
		return refundMainStatus;
	}

	public void setRefundMainStatus(Integer refundMainStatus) {
		this.refundMainStatus = refundMainStatus;
	}

	public Integer getRefundChildStatus() {
		return refundChildStatus;
	}

	public void setRefundChildStatus(Integer refundChildStatus) {
		this.refundChildStatus = refundChildStatus;
	}

	public Integer getEvaluateStatus() {
		return evaluateStatus;
	}

	public void setEvaluateStatus(Integer evaluateStatus) {
		this.evaluateStatus = evaluateStatus;
	}

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public Boolean getHasCancel() {
		return hasCancel;
	}

	public void setHasCancel(Boolean hasCancel) {
		this.hasCancel = hasCancel;
	}

	public LocalDateTime getShipTime() {
		return shipTime;
	}

	public void setShipTime(LocalDateTime shipTime) {
		this.shipTime = shipTime;
	}

	public LocalDateTime getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(LocalDateTime receiptTime) {
		this.receiptTime = receiptTime;
	}

	public LocalDateTime getTradeEndTime() {
		return tradeEndTime;
	}

	public void setTradeEndTime(LocalDateTime tradeEndTime) {
		this.tradeEndTime = tradeEndTime;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddressJson() {
		return receiverAddressJson;
	}

	public void setReceiverAddressJson(String receiverAddressJson) {
		this.receiverAddressJson = receiverAddressJson;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getMemberMsg() {
		return memberMsg;
	}

	public void setMemberMsg(String memberMsg) {
		this.memberMsg = memberMsg;
	}

	public String getCancelMsg() {
		return cancelMsg;
	}

	public void setCancelMsg(String cancelMsg) {
		this.cancelMsg = cancelMsg;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public String getMemberIp() {
		return memberIp;
	}

	public void setMemberIp(String memberIp) {
		this.memberIp = memberIp;
	}

	public Boolean getHasSettlement() {
		return hasSettlement;
	}

	public void setHasSettlement(Boolean hasSettlement) {
		this.hasSettlement = hasSettlement;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public static OrderBuilder builder() {
		return new OrderBuilder();
	}

	public static final class OrderBuilder {

		private Long memberId;
		private Long couponId;
		private Long seckillId;
		private String code;
		private BigDecimal amount = BigDecimal.ZERO;
		private BigDecimal discountAmount = BigDecimal.ZERO;
		private BigDecimal actualAmount = BigDecimal.ZERO;
		private LocalDateTime paySuccessTime;
		private Integer mainStatus;
		private Integer childStatus;
		private Integer refundMainStatus = 0;
		private Integer refundChildStatus = 0;
		private Integer evaluateStatus = 0;
		private String refundCode;
		private Boolean hasCancel = false;
		private LocalDateTime shipTime;
		private LocalDateTime receiptTime;
		private LocalDateTime tradeEndTime;
		private String receiverName;
		private String receiverPhone;
		private String receiverAddressJson;
		private String receiverAddress;
		private String memberMsg;
		private String cancelMsg;
		private String expressCode;
		private String expressName;
		private String expressNumber;
		private String memberIp;
		private Boolean hasSettlement = false;
		private Integer type = 0;
		private String barCode;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderBuilder() {
		}

		public static OrderBuilder anOrder() {
			return new OrderBuilder();
		}

		public OrderBuilder memberId(Long memberId) {
			this.memberId = memberId;
			return this;
		}

		public OrderBuilder couponId(Long couponId) {
			this.couponId = couponId;
			return this;
		}

		public OrderBuilder seckillId(Long seckillId) {
			this.seckillId = seckillId;
			return this;
		}

		public OrderBuilder code(String code) {
			this.code = code;
			return this;
		}

		public OrderBuilder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public OrderBuilder discountAmount(BigDecimal discountAmount) {
			this.discountAmount = discountAmount;
			return this;
		}

		public OrderBuilder actualAmount(BigDecimal actualAmount) {
			this.actualAmount = actualAmount;
			return this;
		}

		public OrderBuilder paySuccessTime(LocalDateTime paySuccessTime) {
			this.paySuccessTime = paySuccessTime;
			return this;
		}

		public OrderBuilder mainStatus(Integer mainStatus) {
			this.mainStatus = mainStatus;
			return this;
		}

		public OrderBuilder childStatus(Integer childStatus) {
			this.childStatus = childStatus;
			return this;
		}

		public OrderBuilder refundMainStatus(Integer refundMainStatus) {
			this.refundMainStatus = refundMainStatus;
			return this;
		}

		public OrderBuilder refundChildStatus(Integer refundChildStatus) {
			this.refundChildStatus = refundChildStatus;
			return this;
		}

		public OrderBuilder evaluateStatus(Integer evaluateStatus) {
			this.evaluateStatus = evaluateStatus;
			return this;
		}

		public OrderBuilder refundCode(String refundCode) {
			this.refundCode = refundCode;
			return this;
		}

		public OrderBuilder hasCancel(Boolean hasCancel) {
			this.hasCancel = hasCancel;
			return this;
		}

		public OrderBuilder shipTime(LocalDateTime shipTime) {
			this.shipTime = shipTime;
			return this;
		}

		public OrderBuilder receiptTime(LocalDateTime receiptTime) {
			this.receiptTime = receiptTime;
			return this;
		}

		public OrderBuilder tradeEndTime(LocalDateTime tradeEndTime) {
			this.tradeEndTime = tradeEndTime;
			return this;
		}

		public OrderBuilder receiverName(String receiverName) {
			this.receiverName = receiverName;
			return this;
		}

		public OrderBuilder receiverPhone(String receiverPhone) {
			this.receiverPhone = receiverPhone;
			return this;
		}

		public OrderBuilder receiverAddressJson(String receiverAddressJson) {
			this.receiverAddressJson = receiverAddressJson;
			return this;
		}

		public OrderBuilder receiverAddress(String receiverAddress) {
			this.receiverAddress = receiverAddress;
			return this;
		}

		public OrderBuilder memberMsg(String memberMsg) {
			this.memberMsg = memberMsg;
			return this;
		}

		public OrderBuilder cancelMsg(String cancelMsg) {
			this.cancelMsg = cancelMsg;
			return this;
		}

		public OrderBuilder expressCode(String expressCode) {
			this.expressCode = expressCode;
			return this;
		}

		public OrderBuilder expressName(String expressName) {
			this.expressName = expressName;
			return this;
		}

		public OrderBuilder expressNumber(String expressNumber) {
			this.expressNumber = expressNumber;
			return this;
		}

		public OrderBuilder memberIp(String memberIp) {
			this.memberIp = memberIp;
			return this;
		}

		public OrderBuilder hasSettlement(Boolean hasSettlement) {
			this.hasSettlement = hasSettlement;
			return this;
		}

		public OrderBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public OrderBuilder barCode(String barCode) {
			this.barCode = barCode;
			return this;
		}

		public OrderBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public Order build() {
			Order order = new Order();
			order.setMemberId(memberId);
			order.setCouponId(couponId);
			order.setSeckillId(seckillId);
			order.setCode(code);
			order.setAmount(amount);
			order.setDiscountAmount(discountAmount);
			order.setActualAmount(actualAmount);
			order.setPaySuccessTime(paySuccessTime);
			order.setMainStatus(mainStatus);
			order.setChildStatus(childStatus);
			order.setRefundMainStatus(refundMainStatus);
			order.setRefundChildStatus(refundChildStatus);
			order.setEvaluateStatus(evaluateStatus);
			order.setRefundCode(refundCode);
			order.setHasCancel(hasCancel);
			order.setShipTime(shipTime);
			order.setReceiptTime(receiptTime);
			order.setTradeEndTime(tradeEndTime);
			order.setReceiverName(receiverName);
			order.setReceiverPhone(receiverPhone);
			order.setReceiverAddressJson(receiverAddressJson);
			order.setReceiverAddress(receiverAddress);
			order.setMemberMsg(memberMsg);
			order.setCancelMsg(cancelMsg);
			order.setExpressCode(expressCode);
			order.setExpressName(expressName);
			order.setExpressNumber(expressNumber);
			order.setMemberIp(memberIp);
			order.setHasSettlement(hasSettlement);
			order.setType(type);
			order.setBarCode(barCode);
			order.setId(id);
			order.setCreateBy(createBy);
			order.setLastModifiedBy(lastModifiedBy);
			order.setCreateTime(createTime);
			order.setLastModifiedTime(lastModifiedTime);
			order.setVersion(version);
			order.setDelFlag(delFlag);
			return order;
		}
	}
}
