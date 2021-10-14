package com.taotao.cloud.order.biz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 售后申请表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:51
 */
@Entity
@TableName(OrderRefundRequest.TABLE_NAME)
@Table(name = OrderRefundRequest.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderRefundRequest.TABLE_NAME, comment = "售后申请表")
public class OrderRefundRequest extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "order_refund_request";

	/**
	 * 退款编号
	 */
	@Column(name = "refund_code", columnDefinition = "varchar(32) not null comment '退款编号'")
	private String refundCode;

	/**
	 * 售后类型 1, 仅退款;0, 退货退款
	 */
	@Column(name = "refund_type", columnDefinition = "int not null default 0 comment ' 售后类型 1, 仅退款;0, 退货退款'")
	private Integer refundType;

	/**
	 * 用户物流收货类型：未收到货--3;已收到货--4
	 */
	@Column(name = "receive_status", columnDefinition = "int not null default 0 comment ' 用户物流收货类型：未收到货--3;已收到货--4'")
	private Integer receiveStatus;

	/**
	 * 主状态 10, 关闭;20, 处理中;30, 退款成功
	 */
	@Column(name = "main_status", columnDefinition = "int not null default 0 comment ' 主状态 10, 关闭;20, 处理中;30, 退款成功'")
	private Integer mainStatus;

	/**
	 * 子状态
	 */
	@Column(name = "child_status", columnDefinition = "int not null default 0 comment ' 子状态'")
	private Integer childStatus;

	/**
	 * 用户退货状态 0, 无需退货,10, 未退货，20, 已退货
	 */
	@Column(name = "user_return_goods_status", columnDefinition = "int not null default 0 comment '  用户退货状态 0, 无需退货,10, 未退货，20, 已退货'")
	private Integer userReturnGoodsStatus;

	/**
	 * 申请原因
	 */
	@Column(name = "refund_cause", columnDefinition = "varchar(256) null comment '申请原因'")
	private String refundCause;

	/**
	 * 退款说明
	 */
	@Column(name = "refund_remark", columnDefinition = "varchar(2560) not null comment '退款说明'")
	private String refundRemark;

	/**
	 * 订单总金额
	 */
	@Column(name = "total_amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '订单总金额'")
	private BigDecimal totalAmount = BigDecimal.ZERO;

	/**
	 * 申请退款金额
	 */
	@Column(name = "req_refund_amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '申请退款金额'")
	private BigDecimal reqRefundAmount = BigDecimal.ZERO;

	/**
	 * 实际退款金额--定时任务设置
	 */
	@Column(name = "act_refund_amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '实际退款金额--定时任务设置'")
	private BigDecimal actRefundAmount = BigDecimal.ZERO;

	/**
	 * 买家会员ID
	 */

	@Column(name = "customer_id", columnDefinition = "varchar(256) not null comment '申请原因'")
	private String customerId;

	/**
	 * 买家姓名
	 */
	@Column(name = "customer_name", columnDefinition = "varchar(256) not null comment '买家姓名'")
	private String customerName;

	/**
	 * 买家电话
	 */
	@Column(name = "customer_phone", columnDefinition = "varchar(256) not null comment '买家电话'")
	private String customerPhone;

	/**
	 * 收货人名称
	 */
	@Column(name = "receiver_name", columnDefinition = "varchar(256) not null comment '收货人名称'")
	private String receiverName;

	/**
	 * 收货人电话
	 */
	@Column(name = "receiver_phone", columnDefinition = "varchar(256) not null comment '收货人电话'")
	private String receiverPhone;

	/**
	 * 收货人地区
	 */
	@Column(name = "receiver_address", columnDefinition = "varchar(256) not null comment '收货人地区'")
	private String receiverAddress;

	/**
	 * 申请时间
	 */
	@Column(name = "create_Date", columnDefinition = "TIMESTAMP comment '创建时间'")
	private LocalDateTime subDate;

	/**
	 * 结束关闭时间
	 */
	@Column(name = "end_date", columnDefinition = "TIMESTAMP comment '结束关闭时间'")
	private LocalDateTime endDate;

	/**
	 * 管家处理时间
	 */
	@Column(name = "steward_date", columnDefinition = "TIMESTAMP comment '管家处理时间'")
	private LocalDateTime stewardDate;

	/**
	 * 用户退货时间
	 */
	@Column(name = "return_goods_date", columnDefinition = "TIMESTAMP comment '用户退货时间'")
	private LocalDateTime returnGoodsDate;

	/**
	 * 退款确认时间
	 */
	@Column(name = "mch_handle_date", columnDefinition = "TIMESTAMP comment '退款确认时间'")
	private LocalDateTime mchHandleDate;

	/**
	 * 退款成功时间
	 */
	@Column(name = "return_money_date", columnDefinition = "TIMESTAMP comment '退款成功时间'")
	private LocalDateTime returnMoneyDate;

	/**
	 * 商品SPU ID
	 */
	@Column(name = "product_spu_id", columnDefinition = "varchar(256) not null comment '商品SPU ID'")
	private String productSpuId;

	/**
	 * 商品SPU名称
	 */
	@Column(name = "product_spu_name", columnDefinition = "varchar(256) not null comment '商品SPU名称'")
	private String productSpuName;

	/**
	 * 商品SKU ID
	 */
	@Column(name = "product_sku_id", columnDefinition = "varchar(256) not null comment '商品SKU ID'")
	private String productSkuId;

	/**
	 * 商品SKU 规格名称
	 */
	@Column(name = "product_sku_name", columnDefinition = "varchar(256) not null comment ' 商品SKU 规格名称'")
	private String productSkuName;

	/**
	 * 商品单价
	 */
	@Column(name = "product_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
	private BigDecimal productPrice = BigDecimal.ZERO;

	/**
	 * 购买数量
	 */
	@Column(name = "buy_num", columnDefinition = "int not null default 0 comment '购买数量'")
	private Integer buyNum;

	/**
	 * 管家编码
	 */
	@Column(name = "steward_id", columnDefinition = "varchar(256) not null comment '管家编码'")
	private String stewardId;

	/**
	 * 管家名称
	 */
	@Column(name = "steward_name", columnDefinition = "varchar(256) not null comment '管家名称'")
	private String stewardName;

	/**
	 * 管家昵称
	 */
	@Column(name = "steward_nick", columnDefinition = "varchar(256) not null comment '管家昵称'")
	private String stewardNick;

	/**
	 * 关闭理由
	 */
	@Column(name = "close_cause", columnDefinition = "varchar(256) not null comment '关闭理由'")
	private String closeCause;

	/**
	 * 关闭说明
	 */
	@Column(name = "close_remark", columnDefinition = "varchar(256) not null comment '关闭说明'")
	private String closeRemark;

	/**
	 * 物流公司编码
	 */
	@Column(name = "express_code", columnDefinition = "varchar(256) not null comment '物流公司编码'")
	private String expressCode;

	/**
	 * 物流快递单号
	 */
	@Column(name = "post_id", columnDefinition = "varchar(256) not null comment '物流快递单号'")
	private String postId;

	/**
	 * 物流说明
	 */
	@Column(name = "express_remark", columnDefinition = "varchar(256) not null comment '物流说明'")
	private String expressRemark;

	/**
	 * 此售后是否存在撤销 1 -存在撤销 0 -不存在撤销
	 */
	@Column(name = "has_cancel", columnDefinition = "int not null default 0 comment '此售后是否存在撤销 1 -存在撤销 0 -不存在撤销'")
	private Integer hasCancel;

	@Column(name = "order_code", columnDefinition = "varchar(256) not null comment 'orderCode'")
	private String orderCode;

	/**
	 * 订单支付流水号
	 */
	@Column(name = "pay_seq_code", columnDefinition = "varchar(256) not null comment '订单支付流水号'")
	private String paySeqCode;

	/**
	 * 微信订单号.
	 */
	@Column(name = "transaction_id", columnDefinition = "varchar(256) not null comment '微信订单号'")
	private String transactionId;

	/**
	 * 商城id
	 */
	@Column(name = "mall_id", columnDefinition = "int not null default 0 comment '商城id'")
	private Integer mallId;

	/**
	 * 供应商id
	 */
	@Column(name = "supplier_id", columnDefinition = "int not null default 0 comment '供应商id'")
	private Integer supplierId;

	/**
	 * 场景方id
	 */
	@Column(name = "scene_id", columnDefinition = "int not null default 0 comment '场景方id'")
	private Integer sceneId;

	@Column(name = "collect_scene_confirm_date", columnDefinition = "TIMESTAMP comment 'collectSceneConfirmDate'")
	private LocalDateTime collectSceneConfirmDate;

	@Column(name = "org_id", columnDefinition = "int not null default 0 comment 'orgId'")
	private Integer orgId;

	@Column(name = "order_type", columnDefinition = "int not null default 0 comment 'orderType'")
	private Integer orderType;

	@Column(name = "item_code", columnDefinition = "varchar(256) not null comment 'itemCode'")
	private String itemCode;

	@Column(name = "main_code", columnDefinition = "varchar(256) not null comment 'mainCode'")
	private String mainCode;

	@Column(name = "has_apply", columnDefinition = "int not null default 0 comment 'hasApply'")
	private Integer hasApply = 0;

	public OrderRefundRequest() {
	}

	public OrderRefundRequest(String refundCode, Integer refundType, Integer receiveStatus,
		Integer mainStatus, Integer childStatus, Integer userReturnGoodsStatus,
		String refundCause, String refundRemark, BigDecimal totalAmount,
		BigDecimal reqRefundAmount, BigDecimal actRefundAmount, String customerId,
		String customerName, String customerPhone, String receiverName,
		String receiverPhone, String receiverAddress, LocalDateTime subDate,
		LocalDateTime endDate, LocalDateTime stewardDate, LocalDateTime returnGoodsDate,
		LocalDateTime mchHandleDate, LocalDateTime returnMoneyDate, String productSpuId,
		String productSpuName, String productSkuId, String productSkuName,
		BigDecimal productPrice, Integer buyNum, String stewardId, String stewardName,
		String stewardNick, String closeCause, String closeRemark, String expressCode,
		String postId, String expressRemark, Integer hasCancel, String orderCode,
		String paySeqCode, String transactionId, Integer mallId, Integer supplierId,
		Integer sceneId, LocalDateTime collectSceneConfirmDate, Integer orgId,
		Integer orderType, String itemCode, String mainCode, Integer hasApply) {
		this.refundCode = refundCode;
		this.refundType = refundType;
		this.receiveStatus = receiveStatus;
		this.mainStatus = mainStatus;
		this.childStatus = childStatus;
		this.userReturnGoodsStatus = userReturnGoodsStatus;
		this.refundCause = refundCause;
		this.refundRemark = refundRemark;
		this.totalAmount = totalAmount;
		this.reqRefundAmount = reqRefundAmount;
		this.actRefundAmount = actRefundAmount;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverAddress = receiverAddress;
		this.subDate = subDate;
		this.endDate = endDate;
		this.stewardDate = stewardDate;
		this.returnGoodsDate = returnGoodsDate;
		this.mchHandleDate = mchHandleDate;
		this.returnMoneyDate = returnMoneyDate;
		this.productSpuId = productSpuId;
		this.productSpuName = productSpuName;
		this.productSkuId = productSkuId;
		this.productSkuName = productSkuName;
		this.productPrice = productPrice;
		this.buyNum = buyNum;
		this.stewardId = stewardId;
		this.stewardName = stewardName;
		this.stewardNick = stewardNick;
		this.closeCause = closeCause;
		this.closeRemark = closeRemark;
		this.expressCode = expressCode;
		this.postId = postId;
		this.expressRemark = expressRemark;
		this.hasCancel = hasCancel;
		this.orderCode = orderCode;
		this.paySeqCode = paySeqCode;
		this.transactionId = transactionId;
		this.mallId = mallId;
		this.supplierId = supplierId;
		this.sceneId = sceneId;
		this.collectSceneConfirmDate = collectSceneConfirmDate;
		this.orgId = orgId;
		this.orderType = orderType;
		this.itemCode = itemCode;
		this.mainCode = mainCode;
		this.hasApply = hasApply;
	}

	@Override
	public String
	toString() {
		return "OrderRefundRequest{" +
			"refundCode='" + refundCode + '\'' +
			", refundType=" + refundType +
			", receiveStatus=" + receiveStatus +
			", mainStatus=" + mainStatus +
			", childStatus=" + childStatus +
			", userReturnGoodsStatus=" + userReturnGoodsStatus +
			", refundCause='" + refundCause + '\'' +
			", refundRemark='" + refundRemark + '\'' +
			", totalAmount=" + totalAmount +
			", reqRefundAmount=" + reqRefundAmount +
			", actRefundAmount=" + actRefundAmount +
			", customerId='" + customerId + '\'' +
			", customerName='" + customerName + '\'' +
			", customerPhone='" + customerPhone + '\'' +
			", receiverName='" + receiverName + '\'' +
			", receiverPhone='" + receiverPhone + '\'' +
			", receiverAddress='" + receiverAddress + '\'' +
			", subDate=" + subDate +
			", endDate=" + endDate +
			", stewardDate=" + stewardDate +
			", returnGoodsDate=" + returnGoodsDate +
			", mchHandleDate=" + mchHandleDate +
			", returnMoneyDate=" + returnMoneyDate +
			", productSpuId='" + productSpuId + '\'' +
			", productSpuName='" + productSpuName + '\'' +
			", productSkuId='" + productSkuId + '\'' +
			", productSkuName='" + productSkuName + '\'' +
			", productPrice=" + productPrice +
			", buyNum=" + buyNum +
			", stewardId='" + stewardId + '\'' +
			", stewardName='" + stewardName + '\'' +
			", stewardNick='" + stewardNick + '\'' +
			", closeCause='" + closeCause + '\'' +
			", closeRemark='" + closeRemark + '\'' +
			", expressCode='" + expressCode + '\'' +
			", postId='" + postId + '\'' +
			", expressRemark='" + expressRemark + '\'' +
			", hasCancel=" + hasCancel +
			", orderCode='" + orderCode + '\'' +
			", paySeqCode='" + paySeqCode + '\'' +
			", transactionId='" + transactionId + '\'' +
			", mallId=" + mallId +
			", supplierId=" + supplierId +
			", sceneId=" + sceneId +
			", collectSceneConfirmDate=" + collectSceneConfirmDate +
			", orgId=" + orgId +
			", orderType=" + orderType +
			", itemCode='" + itemCode + '\'' +
			", mainCode='" + mainCode + '\'' +
			", hasApply=" + hasApply +
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
		OrderRefundRequest that = (OrderRefundRequest) o;
		return Objects.equals(refundCode, that.refundCode) && Objects.equals(
			refundType, that.refundType) && Objects.equals(receiveStatus,
			that.receiveStatus) && Objects.equals(mainStatus, that.mainStatus)
			&& Objects.equals(childStatus, that.childStatus) && Objects.equals(
			userReturnGoodsStatus, that.userReturnGoodsStatus) && Objects.equals(
			refundCause, that.refundCause) && Objects.equals(refundRemark,
			that.refundRemark) && Objects.equals(totalAmount, that.totalAmount)
			&& Objects.equals(reqRefundAmount, that.reqRefundAmount)
			&& Objects.equals(actRefundAmount, that.actRefundAmount)
			&& Objects.equals(customerId, that.customerId) && Objects.equals(
			customerName, that.customerName) && Objects.equals(customerPhone,
			that.customerPhone) && Objects.equals(receiverName, that.receiverName)
			&& Objects.equals(receiverPhone, that.receiverPhone)
			&& Objects.equals(receiverAddress, that.receiverAddress)
			&& Objects.equals(subDate, that.subDate) && Objects.equals(endDate,
			that.endDate) && Objects.equals(stewardDate, that.stewardDate)
			&& Objects.equals(returnGoodsDate, that.returnGoodsDate)
			&& Objects.equals(mchHandleDate, that.mchHandleDate)
			&& Objects.equals(returnMoneyDate, that.returnMoneyDate)
			&& Objects.equals(productSpuId, that.productSpuId)
			&& Objects.equals(productSpuName, that.productSpuName)
			&& Objects.equals(productSkuId, that.productSkuId)
			&& Objects.equals(productSkuName, that.productSkuName)
			&& Objects.equals(productPrice, that.productPrice)
			&& Objects.equals(buyNum, that.buyNum) && Objects.equals(stewardId,
			that.stewardId) && Objects.equals(stewardName, that.stewardName)
			&& Objects.equals(stewardNick, that.stewardNick) && Objects.equals(
			closeCause, that.closeCause) && Objects.equals(closeRemark, that.closeRemark)
			&& Objects.equals(expressCode, that.expressCode) && Objects.equals(
			postId, that.postId) && Objects.equals(expressRemark, that.expressRemark)
			&& Objects.equals(hasCancel, that.hasCancel) && Objects.equals(
			orderCode, that.orderCode) && Objects.equals(paySeqCode, that.paySeqCode)
			&& Objects.equals(transactionId, that.transactionId)
			&& Objects.equals(mallId, that.mallId) && Objects.equals(supplierId,
			that.supplierId) && Objects.equals(sceneId, that.sceneId)
			&& Objects.equals(collectSceneConfirmDate, that.collectSceneConfirmDate)
			&& Objects.equals(orgId, that.orgId) && Objects.equals(orderType,
			that.orderType) && Objects.equals(itemCode, that.itemCode)
			&& Objects.equals(mainCode, that.mainCode) && Objects.equals(
			hasApply, that.hasApply);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), refundCode, refundType, receiveStatus, mainStatus,
			childStatus, userReturnGoodsStatus, refundCause, refundRemark, totalAmount,
			reqRefundAmount, actRefundAmount, customerId, customerName, customerPhone, receiverName,
			receiverPhone, receiverAddress, subDate, endDate, stewardDate, returnGoodsDate,
			mchHandleDate, returnMoneyDate, productSpuId, productSpuName, productSkuId,
			productSkuName, productPrice, buyNum, stewardId, stewardName, stewardNick, closeCause,
			closeRemark, expressCode, postId, expressRemark, hasCancel, orderCode, paySeqCode,
			transactionId, mallId, supplierId, sceneId, collectSceneConfirmDate, orgId, orderType,
			itemCode, mainCode, hasApply);
	}

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
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

	public Integer getUserReturnGoodsStatus() {
		return userReturnGoodsStatus;
	}

	public void setUserReturnGoodsStatus(Integer userReturnGoodsStatus) {
		this.userReturnGoodsStatus = userReturnGoodsStatus;
	}

	public String getRefundCause() {
		return refundCause;
	}

	public void setRefundCause(String refundCause) {
		this.refundCause = refundCause;
	}

	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getReqRefundAmount() {
		return reqRefundAmount;
	}

	public void setReqRefundAmount(BigDecimal reqRefundAmount) {
		this.reqRefundAmount = reqRefundAmount;
	}

	public BigDecimal getActRefundAmount() {
		return actRefundAmount;
	}

	public void setActRefundAmount(BigDecimal actRefundAmount) {
		this.actRefundAmount = actRefundAmount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
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

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public LocalDateTime getSubDate() {
		return subDate;
	}

	public void setSubDate(LocalDateTime subDate) {
		this.subDate = subDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getStewardDate() {
		return stewardDate;
	}

	public void setStewardDate(LocalDateTime stewardDate) {
		this.stewardDate = stewardDate;
	}

	public LocalDateTime getReturnGoodsDate() {
		return returnGoodsDate;
	}

	public void setReturnGoodsDate(LocalDateTime returnGoodsDate) {
		this.returnGoodsDate = returnGoodsDate;
	}

	public LocalDateTime getMchHandleDate() {
		return mchHandleDate;
	}

	public void setMchHandleDate(LocalDateTime mchHandleDate) {
		this.mchHandleDate = mchHandleDate;
	}

	public LocalDateTime getReturnMoneyDate() {
		return returnMoneyDate;
	}

	public void setReturnMoneyDate(LocalDateTime returnMoneyDate) {
		this.returnMoneyDate = returnMoneyDate;
	}

	public String getProductSpuId() {
		return productSpuId;
	}

	public void setProductSpuId(String productSpuId) {
		this.productSpuId = productSpuId;
	}

	public String getProductSpuName() {
		return productSpuName;
	}

	public void setProductSpuName(String productSpuName) {
		this.productSpuName = productSpuName;
	}

	public String getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(String productSkuId) {
		this.productSkuId = productSkuId;
	}

	public String getProductSkuName() {
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName) {
		this.productSkuName = productSkuName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	public String getStewardId() {
		return stewardId;
	}

	public void setStewardId(String stewardId) {
		this.stewardId = stewardId;
	}

	public String getStewardName() {
		return stewardName;
	}

	public void setStewardName(String stewardName) {
		this.stewardName = stewardName;
	}

	public String getStewardNick() {
		return stewardNick;
	}

	public void setStewardNick(String stewardNick) {
		this.stewardNick = stewardNick;
	}

	public String getCloseCause() {
		return closeCause;
	}

	public void setCloseCause(String closeCause) {
		this.closeCause = closeCause;
	}

	public String getCloseRemark() {
		return closeRemark;
	}

	public void setCloseRemark(String closeRemark) {
		this.closeRemark = closeRemark;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getExpressRemark() {
		return expressRemark;
	}

	public void setExpressRemark(String expressRemark) {
		this.expressRemark = expressRemark;
	}

	public Integer getHasCancel() {
		return hasCancel;
	}

	public void setHasCancel(Integer hasCancel) {
		this.hasCancel = hasCancel;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPaySeqCode() {
		return paySeqCode;
	}

	public void setPaySeqCode(String paySeqCode) {
		this.paySeqCode = paySeqCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getMallId() {
		return mallId;
	}

	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getSceneId() {
		return sceneId;
	}

	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	public LocalDateTime getCollectSceneConfirmDate() {
		return collectSceneConfirmDate;
	}

	public void setCollectSceneConfirmDate(LocalDateTime collectSceneConfirmDate) {
		this.collectSceneConfirmDate = collectSceneConfirmDate;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}

	public Integer getHasApply() {
		return hasApply;
	}

	public void setHasApply(Integer hasApply) {
		this.hasApply = hasApply;
	}


	public static OrderRefundRequestBuilder builder() {
		return new OrderRefundRequestBuilder();
	}


	public static final class OrderRefundRequestBuilder {

		private String refundCode;
		private Integer refundType;
		private Integer receiveStatus;
		private Integer mainStatus;
		private Integer childStatus;
		private Integer userReturnGoodsStatus;
		private String refundCause;
		private String refundRemark;
		private BigDecimal totalAmount = BigDecimal.ZERO;
		private BigDecimal reqRefundAmount = BigDecimal.ZERO;
		private BigDecimal actRefundAmount = BigDecimal.ZERO;

		private String customerId;
		private String customerName;
		private String customerPhone;
		private String receiverName;
		private String receiverPhone;
		private String receiverAddress;
		private LocalDateTime subDate;
		private LocalDateTime endDate;
		private LocalDateTime stewardDate;
		private LocalDateTime returnGoodsDate;
		private LocalDateTime mchHandleDate;
		private LocalDateTime returnMoneyDate;
		private String productSpuId;
		private String productSpuName;
		private String productSkuId;
		private String productSkuName;
		private BigDecimal productPrice = BigDecimal.ZERO;
		private Integer buyNum;
		private String stewardId;
		private String stewardName;
		private String stewardNick;
		private String closeCause;
		private String closeRemark;
		private String expressCode;
		private String postId;
		private String expressRemark;
		private Integer hasCancel;
		private String orderCode;
		private String paySeqCode;
		private String transactionId;
		private Integer mallId;
		private Integer supplierId;
		private Integer sceneId;
		private LocalDateTime collectSceneConfirmDate;
		private Integer orgId;
		private Integer orderType;
		private String itemCode;
		private String mainCode;
		private Integer hasApply = 0;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderRefundRequestBuilder() {
		}


		public OrderRefundRequestBuilder refundCode(String refundCode) {
			this.refundCode = refundCode;
			return this;
		}

		public OrderRefundRequestBuilder refundType(Integer refundType) {
			this.refundType = refundType;
			return this;
		}

		public OrderRefundRequestBuilder receiveStatus(Integer receiveStatus) {
			this.receiveStatus = receiveStatus;
			return this;
		}

		public OrderRefundRequestBuilder mainStatus(Integer mainStatus) {
			this.mainStatus = mainStatus;
			return this;
		}

		public OrderRefundRequestBuilder childStatus(Integer childStatus) {
			this.childStatus = childStatus;
			return this;
		}

		public OrderRefundRequestBuilder userReturnGoodsStatus(Integer userReturnGoodsStatus) {
			this.userReturnGoodsStatus = userReturnGoodsStatus;
			return this;
		}

		public OrderRefundRequestBuilder refundCause(String refundCause) {
			this.refundCause = refundCause;
			return this;
		}

		public OrderRefundRequestBuilder refundRemark(String refundRemark) {
			this.refundRemark = refundRemark;
			return this;
		}

		public OrderRefundRequestBuilder totalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
			return this;
		}

		public OrderRefundRequestBuilder reqRefundAmount(BigDecimal reqRefundAmount) {
			this.reqRefundAmount = reqRefundAmount;
			return this;
		}

		public OrderRefundRequestBuilder actRefundAmount(BigDecimal actRefundAmount) {
			this.actRefundAmount = actRefundAmount;
			return this;
		}

		public OrderRefundRequestBuilder customerId(String customerId) {
			this.customerId = customerId;
			return this;
		}

		public OrderRefundRequestBuilder customerName(String customerName) {
			this.customerName = customerName;
			return this;
		}

		public OrderRefundRequestBuilder customerPhone(String customerPhone) {
			this.customerPhone = customerPhone;
			return this;
		}

		public OrderRefundRequestBuilder receiverName(String receiverName) {
			this.receiverName = receiverName;
			return this;
		}

		public OrderRefundRequestBuilder receiverPhone(String receiverPhone) {
			this.receiverPhone = receiverPhone;
			return this;
		}

		public OrderRefundRequestBuilder receiverAddress(String receiverAddress) {
			this.receiverAddress = receiverAddress;
			return this;
		}

		public OrderRefundRequestBuilder subDate(LocalDateTime subDate) {
			this.subDate = subDate;
			return this;
		}

		public OrderRefundRequestBuilder endDate(LocalDateTime endDate) {
			this.endDate = endDate;
			return this;
		}

		public OrderRefundRequestBuilder stewardDate(LocalDateTime stewardDate) {
			this.stewardDate = stewardDate;
			return this;
		}

		public OrderRefundRequestBuilder returnGoodsDate(LocalDateTime returnGoodsDate) {
			this.returnGoodsDate = returnGoodsDate;
			return this;
		}

		public OrderRefundRequestBuilder mchHandleDate(LocalDateTime mchHandleDate) {
			this.mchHandleDate = mchHandleDate;
			return this;
		}

		public OrderRefundRequestBuilder returnMoneyDate(LocalDateTime returnMoneyDate) {
			this.returnMoneyDate = returnMoneyDate;
			return this;
		}

		public OrderRefundRequestBuilder productSpuId(String productSpuId) {
			this.productSpuId = productSpuId;
			return this;
		}

		public OrderRefundRequestBuilder productSpuName(String productSpuName) {
			this.productSpuName = productSpuName;
			return this;
		}

		public OrderRefundRequestBuilder productSkuId(String productSkuId) {
			this.productSkuId = productSkuId;
			return this;
		}

		public OrderRefundRequestBuilder productSkuName(String productSkuName) {
			this.productSkuName = productSkuName;
			return this;
		}

		public OrderRefundRequestBuilder productPrice(BigDecimal productPrice) {
			this.productPrice = productPrice;
			return this;
		}

		public OrderRefundRequestBuilder buyNum(Integer buyNum) {
			this.buyNum = buyNum;
			return this;
		}

		public OrderRefundRequestBuilder stewardId(String stewardId) {
			this.stewardId = stewardId;
			return this;
		}

		public OrderRefundRequestBuilder stewardName(String stewardName) {
			this.stewardName = stewardName;
			return this;
		}

		public OrderRefundRequestBuilder stewardNick(String stewardNick) {
			this.stewardNick = stewardNick;
			return this;
		}

		public OrderRefundRequestBuilder closeCause(String closeCause) {
			this.closeCause = closeCause;
			return this;
		}

		public OrderRefundRequestBuilder closeRemark(String closeRemark) {
			this.closeRemark = closeRemark;
			return this;
		}

		public OrderRefundRequestBuilder expressCode(String expressCode) {
			this.expressCode = expressCode;
			return this;
		}

		public OrderRefundRequestBuilder postId(String postId) {
			this.postId = postId;
			return this;
		}

		public OrderRefundRequestBuilder expressRemark(String expressRemark) {
			this.expressRemark = expressRemark;
			return this;
		}

		public OrderRefundRequestBuilder hasCancel(Integer hasCancel) {
			this.hasCancel = hasCancel;
			return this;
		}

		public OrderRefundRequestBuilder orderCode(String orderCode) {
			this.orderCode = orderCode;
			return this;
		}

		public OrderRefundRequestBuilder paySeqCode(String paySeqCode) {
			this.paySeqCode = paySeqCode;
			return this;
		}

		public OrderRefundRequestBuilder transactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public OrderRefundRequestBuilder mallId(Integer mallId) {
			this.mallId = mallId;
			return this;
		}

		public OrderRefundRequestBuilder supplierId(Integer supplierId) {
			this.supplierId = supplierId;
			return this;
		}

		public OrderRefundRequestBuilder sceneId(Integer sceneId) {
			this.sceneId = sceneId;
			return this;
		}

		public OrderRefundRequestBuilder collectSceneConfirmDate(
			LocalDateTime collectSceneConfirmDate) {
			this.collectSceneConfirmDate = collectSceneConfirmDate;
			return this;
		}

		public OrderRefundRequestBuilder orgId(Integer orgId) {
			this.orgId = orgId;
			return this;
		}

		public OrderRefundRequestBuilder orderType(Integer orderType) {
			this.orderType = orderType;
			return this;
		}

		public OrderRefundRequestBuilder itemCode(String itemCode) {
			this.itemCode = itemCode;
			return this;
		}

		public OrderRefundRequestBuilder mainCode(String mainCode) {
			this.mainCode = mainCode;
			return this;
		}

		public OrderRefundRequestBuilder hasApply(Integer hasApply) {
			this.hasApply = hasApply;
			return this;
		}

		public OrderRefundRequestBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderRefundRequestBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderRefundRequestBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderRefundRequestBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderRefundRequestBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderRefundRequestBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderRefundRequestBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderRefundRequest build() {
			OrderRefundRequest orderRefundRequest = new OrderRefundRequest();
			orderRefundRequest.setRefundCode(refundCode);
			orderRefundRequest.setRefundType(refundType);
			orderRefundRequest.setReceiveStatus(receiveStatus);
			orderRefundRequest.setMainStatus(mainStatus);
			orderRefundRequest.setChildStatus(childStatus);
			orderRefundRequest.setUserReturnGoodsStatus(userReturnGoodsStatus);
			orderRefundRequest.setRefundCause(refundCause);
			orderRefundRequest.setRefundRemark(refundRemark);
			orderRefundRequest.setTotalAmount(totalAmount);
			orderRefundRequest.setReqRefundAmount(reqRefundAmount);
			orderRefundRequest.setActRefundAmount(actRefundAmount);
			orderRefundRequest.setCustomerId(customerId);
			orderRefundRequest.setCustomerName(customerName);
			orderRefundRequest.setCustomerPhone(customerPhone);
			orderRefundRequest.setReceiverName(receiverName);
			orderRefundRequest.setReceiverPhone(receiverPhone);
			orderRefundRequest.setReceiverAddress(receiverAddress);
			orderRefundRequest.setSubDate(subDate);
			orderRefundRequest.setEndDate(endDate);
			orderRefundRequest.setStewardDate(stewardDate);
			orderRefundRequest.setReturnGoodsDate(returnGoodsDate);
			orderRefundRequest.setMchHandleDate(mchHandleDate);
			orderRefundRequest.setReturnMoneyDate(returnMoneyDate);
			orderRefundRequest.setProductSpuId(productSpuId);
			orderRefundRequest.setProductSpuName(productSpuName);
			orderRefundRequest.setProductSkuId(productSkuId);
			orderRefundRequest.setProductSkuName(productSkuName);
			orderRefundRequest.setProductPrice(productPrice);
			orderRefundRequest.setBuyNum(buyNum);
			orderRefundRequest.setStewardId(stewardId);
			orderRefundRequest.setStewardName(stewardName);
			orderRefundRequest.setStewardNick(stewardNick);
			orderRefundRequest.setCloseCause(closeCause);
			orderRefundRequest.setCloseRemark(closeRemark);
			orderRefundRequest.setExpressCode(expressCode);
			orderRefundRequest.setPostId(postId);
			orderRefundRequest.setExpressRemark(expressRemark);
			orderRefundRequest.setHasCancel(hasCancel);
			orderRefundRequest.setOrderCode(orderCode);
			orderRefundRequest.setPaySeqCode(paySeqCode);
			orderRefundRequest.setTransactionId(transactionId);
			orderRefundRequest.setMallId(mallId);
			orderRefundRequest.setSupplierId(supplierId);
			orderRefundRequest.setSceneId(sceneId);
			orderRefundRequest.setCollectSceneConfirmDate(collectSceneConfirmDate);
			orderRefundRequest.setOrgId(orgId);
			orderRefundRequest.setOrderType(orderType);
			orderRefundRequest.setItemCode(itemCode);
			orderRefundRequest.setMainCode(mainCode);
			orderRefundRequest.setHasApply(hasApply);
			orderRefundRequest.setId(id);
			orderRefundRequest.setCreatedBy(createBy);
			orderRefundRequest.setLastModifiedBy(lastModifiedBy);
			orderRefundRequest.setCreateTime(createTime);
			orderRefundRequest.setLastModifiedTime(lastModifiedTime);
			orderRefundRequest.setVersion(version);
			orderRefundRequest.setDelFlag(delFlag);
			return orderRefundRequest;
		}
	}
}
