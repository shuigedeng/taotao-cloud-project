/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.order.api.vo.order_info;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:29:13
 */
@Schema(name = "OrderVO", description = "订单VO")
public class OrderVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	private Long memberId;
	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;
	/**
	 * 秒杀活动id
	 */
	@Schema(description = "秒杀活动id")
	private Long seckillId;
	/**
	 * 订单编码
	 */
	@Schema(description = "订单编码")
	private String code;
	/**
	 * 订单金额
	 */
	@Schema(description = "订单金额")
	private BigDecimal amount;
	/**
	 * 优惠金额
	 */
	@Schema(description = "优惠金额")
	private BigDecimal discountAmount;
	/**
	 * 实际支付金额
	 */
	@Schema(description = "实际支付金额")
	private BigDecimal actualAmount;
	/**
	 * 支付时间
	 */
	@Schema(description = "支付时间")
	private LocalDateTime paySuccessTime;
	/**
	 * 订单主状态
	 */
	@Schema(description = "订单主状态")
	private Integer mainStatus;
	/**
	 * 订单子状态
	 */
	@Schema(description = "订单子状态")
	private Integer childStatus;
	/**
	 * 售后主状态
	 */
	@Schema(description = "售后主状态")
	private Integer refundMainStatus;
	/**
	 * 售后子状态
	 */
	@Schema(description = "售后子状态")
	private Integer refundChildStatus;
	/**
	 * 是否可评价 0-不可评价 1-可评价 2-可追评
	 */
	@Schema(description = "是否可评价")
	private Integer evaluateStatus;
	/**
	 * 申请售后code
	 */
	@Schema(description = "申请售后code")
	private String refundCode;
	/**
	 * 申请售后是否撤销
	 */
	@Schema(description = "申请售后是否撤销")
	private Boolean hasCancel;
	/**
	 * 发货时间
	 */
	@Schema(description = "发货时间")
	private LocalDateTime shipTime;
	/**
	 * 收货时间
	 */
	@Schema(description = "收货时间")
	private LocalDateTime receiptTime;
	/**
	 * 交易结束时间
	 */
	@Schema(description = "交易结束时间")
	private LocalDateTime tradeEndTime;
	/**
	 * 交易结束时间
	 */
	@Schema(description = "交易结束时间")
	private String receiverName;
	/**
	 * 收货人电话
	 */
	@Schema(description = "收货人电话")
	private String receiverPhone;
	/**
	 * 收货地址:json的形式存储
	 */
	@Schema(description = "收货地址:json的形式存储")
	private String receiverAddressJson;
	/**
	 * 冗余收货地址字符串
	 */
	@Schema(description = "冗余收货地址字符串")
	private String receiverAddress;
	/**
	 * 买家留言
	 */
	@Schema(description = "买家留言")
	private String memberMsg;
	/**
	 * 取消订单说明
	 */
	@Schema(description = "取消订单说明")
	private String cancelMsg;
	/**
	 * 物流公司code
	 */
	@Schema(description = "物流公司code")
	private String expressCode;
	/**
	 * 物流公司名称
	 */
	@Schema(description = "物流公司名称")
	private String expressName;
	/**
	 * 物流单号
	 */
	@Schema(description = "物流单号")
	private String expressNumber;
	/**
	 * 买家IP
	 */
	@Schema(description = "买家IP")
	private String memberIp;
	/**
	 * 是否结算
	 */
	@Schema(description = "是否结算")
	private Boolean hasSettlement;
	/**
	 * 订单类型
	 */
	@Schema(description = "订单类型")
	private Integer type;
	/**
	 * 条形码
	 */
	@Schema(description = "条形码")
	private String barCode;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public OrderVO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
