package com.taotao.cloud.order.api.vo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
//@ApiModel(value = "订单VO", description = "订单VO")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

//    @ApiModelProperty(value = "id")
    private Long id;

//    @ApiModelProperty(value = "买家ID")
    private Long memberId;

	// @ApiModelProperty(value = "优惠券id")
    private Long couponId;

	//@ApiModelProperty(value = "秒杀活动id")
    private Long seckillId;

	// @ApiModelProperty(value = "订单编码")
    private String code;

	//@ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

	// @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

	//  @ApiModelProperty(value = "实际支付金额")
    private BigDecimal actualAmount;

	// @ApiModelProperty(value = "支付时间")
    private LocalDateTime paySuccessTime;

	// @ApiModelProperty(value = "订单主状态")
    private Integer mainStatus;

	// @ApiModelProperty(value = "订单子状态")
    private Integer childStatus;

	//  @ApiModelProperty(value = "售后主状态")
    private Integer refundMainStatus;

	//  @ApiModelProperty(value = "售后子状态")
    private Integer refundChildStatus;

    /**
     * 是否可评价
     * <br/>不可评价 --0
     * <br/>可评价 --1
     * <br/>可追评 --2
     */
    //@ApiModelProperty(value = "是否可评价")
    private Integer evaluateStatus;

	// @ApiModelProperty(value = "申请售后code")
    private String refundCode;

	// @ApiModelProperty(value = "申请售后是否撤销")
    private Boolean hasCancel;

	// @ApiModelProperty(value = "发货时间")
    private LocalDateTime shipTime;

	// @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

	// @ApiModelProperty(value = "交易结束时间")
    private LocalDateTime tradeEndTime;

	// @ApiModelProperty(value = "交易结束时间")
    private String receiverName;

	// @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

	// @ApiModelProperty(value = "收货地址:json的形式存储")
    private String receiverAddressJson;

	//@ApiModelProperty(value = "冗余收货地址字符串")
    private String receiverAddress;

	// @ApiModelProperty(value = "买家留言")
    private String memberMsg;

	// @ApiModelProperty(value = "取消订单说明")
    private String cancelMsg;

	// @ApiModelProperty(value = "物流公司code")
    private String expressCode;

	// @ApiModelProperty(value = "物流公司名称")
    private String expressName;

	// @ApiModelProperty(value = "物流单号")
    private String expressNumber;

	// @ApiModelProperty(value = "买家IP")
    private String memberIp;

	// @ApiModelProperty(value = "是否结算")
    private Boolean hasSettlement;

	//@ApiModelProperty(value = "订单类型")
    private Integer type;

	// @ApiModelProperty(value = "条形码")
    private String barCode;

	//@ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

	// @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifiedTime;

    public OrderVO(){}

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
