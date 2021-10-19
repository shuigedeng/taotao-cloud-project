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
package com.taotao.cloud.order.api;


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
public class OrderQueryDO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 买家ID
	 */
	private Long memberId;
	/**
	 * 优惠券id
	 */
	private Long couponId;
	/**
	 * 秒杀活动id
	 */
	private Long seckillId;
	/**
	 * 订单编码
	 */
	private String code;
	/**
	 * 订单金额
	 */
	private BigDecimal amount;
	/**
	 * 优惠金额
	 */
	private BigDecimal discountAmount;
	/**
	 * 实际支付金额
	 */
	private BigDecimal actualAmount;
	/**
	 * 支付时间
	 */
	private LocalDateTime paySuccessTime;
	/**
	 * 订单主状态
	 */
	private Integer mainStatus;
	/**
	 * 订单子状态
	 */
	private Integer childStatus;
	/**
	 * 售后主状态
	 */
	private Integer refundMainStatus;
	/**
	 * 售后子状态
	 */
	private Integer refundChildStatus;
	/**
	 * 是否可评价 0-不可评价 1-可评价 2-可追评
	 */
	private Integer evaluateStatus;
	/**
	 * 申请售后code
	 */
	private String refundCode;
	/**
	 * 申请售后是否撤销
	 */
	private Boolean hasCancel;
	/**
	 * 发货时间
	 */
	private LocalDateTime shipTime;
	/**
	 * 收货时间
	 */
	private LocalDateTime receiptTime;
	/**
	 * 交易结束时间
	 */
	private LocalDateTime tradeEndTime;
	/**
	 * 交易结束时间
	 */
	private String receiverName;
	/**
	 * 收货人电话
	 */
	private String receiverPhone;

	public OrderQueryDO() {
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
}
