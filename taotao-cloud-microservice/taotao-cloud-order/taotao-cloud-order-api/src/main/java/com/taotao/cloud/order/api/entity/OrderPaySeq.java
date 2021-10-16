package com.taotao.cloud.order.api.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单支付流水表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:45
 */
@Entity
@TableName(OrderPaySeq.TABLE_NAME)
@Table(name = OrderPaySeq.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderPaySeq.TABLE_NAME, comment = "订单支付流水表")
public class OrderPaySeq extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "order_pay_seq";

	/**
	 * 支付流水编码--需要与微信的预支付ID进行关联
	 */
	@Column(name = "pay_code", columnDefinition = "varchar(32) not null comment '支付流水编码'")
	private String payCode;

	/**
	 * 买家ID
	 */
	@Column(name = "customer_id", nullable = false, columnDefinition = "bigint not null comment '买家ID'")
	private Long customerId;

	/**
	 * 付款方银行编码
	 */
	@Column(name = "payer_bank_code", columnDefinition = "varchar(32) not null comment '付款方银行编码'")
	private String payerBankCode;

	/**
	 * 交易金额
	 */
	@Column(name = "actual_amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '交易金额'")
	private BigDecimal actualAmount = BigDecimal.ZERO;

	/**
	 * 微信预支付ID
	 */
	@Column(name = "prepay_id", columnDefinition = "varchar(32) not null comment '微信预支付ID'")
	private String prepayId;

	/**
	 * 微信交易ID
	 */
	@Column(name = "transaction_id", columnDefinition = "varchar(32) not null comment '微信交易ID'")
	private String transactionId;

	/**
	 * 微信商户ID
	 */
	@Column(name = "mch_id", columnDefinition = "varchar(32) not null comment '微信商户ID'")
	private String mchId;

	/**
	 * 微信APPID
	 */
	@Column(name = "app_id", columnDefinition = "varchar(32) not null comment '微信APPID'")
	private String appId;

	/**
	 * 状态 0-等待支付 1-超时关闭 2-支付失败 3-支付成功
	 */
	@Column(name = "status", columnDefinition = "int not null default 0 comment '状态 0-等待支付 1-超时关闭 2-支付失败 3-支付成功'")
	private Integer status;

	/**
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(3200) comment '备注'")
	private String remark;

	public OrderPaySeq() {
	}

	public OrderPaySeq(String payCode, Long customerId, String payerBankCode,
		BigDecimal actualAmount, String prepayId, String transactionId, String mchId,
		String appId, Integer status, String remark) {
		this.payCode = payCode;
		this.customerId = customerId;
		this.payerBankCode = payerBankCode;
		this.actualAmount = actualAmount;
		this.prepayId = prepayId;
		this.transactionId = transactionId;
		this.mchId = mchId;
		this.appId = appId;
		this.status = status;
		this.remark = remark;
	}


	@Override
	public String toString() {
		return "OrderPaySeq{" +
			"payCode='" + payCode + '\'' +
			", customerId=" + customerId +
			", payerBankCode='" + payerBankCode + '\'' +
			", actualAmount=" + actualAmount +
			", prepayId='" + prepayId + '\'' +
			", transactionId='" + transactionId + '\'' +
			", mchId='" + mchId + '\'' +
			", appId='" + appId + '\'' +
			", status=" + status +
			", remark='" + remark + '\'' +
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
		OrderPaySeq that = (OrderPaySeq) o;
		return Objects.equals(payCode, that.payCode) && Objects.equals(customerId,
			that.customerId) && Objects.equals(payerBankCode, that.payerBankCode)
			&& Objects.equals(actualAmount, that.actualAmount)
			&& Objects.equals(prepayId, that.prepayId) && Objects.equals(
			transactionId, that.transactionId) && Objects.equals(mchId, that.mchId)
			&& Objects.equals(appId, that.appId) && Objects.equals(status,
			that.status) && Objects.equals(remark, that.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), payCode, customerId, payerBankCode, actualAmount,
			prepayId, transactionId, mchId, appId, status, remark);
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getPayerBankCode() {
		return payerBankCode;
	}

	public void setPayerBankCode(String payerBankCode) {
		this.payerBankCode = payerBankCode;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public static OrderPaySeqBuilder builder() {
		return new OrderPaySeqBuilder();
	}

	public static final class OrderPaySeqBuilder {

		private String payCode;
		private Long customerId;
		private String payerBankCode;
		private BigDecimal actualAmount = new BigDecimal(0);
		private String prepayId;
		private String transactionId;
		private String mchId;
		private String appId;
		private Integer status;
		private String remark;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderPaySeqBuilder() {
		}

		public static OrderPaySeqBuilder anOrderPaySeq() {
			return new OrderPaySeqBuilder();
		}

		public OrderPaySeqBuilder payCode(String payCode) {
			this.payCode = payCode;
			return this;
		}

		public OrderPaySeqBuilder customerId(Long customerId) {
			this.customerId = customerId;
			return this;
		}

		public OrderPaySeqBuilder payerBankCode(String payerBankCode) {
			this.payerBankCode = payerBankCode;
			return this;
		}

		public OrderPaySeqBuilder actualAmount(BigDecimal actualAmount) {
			this.actualAmount = actualAmount;
			return this;
		}

		public OrderPaySeqBuilder prepayId(String prepayId) {
			this.prepayId = prepayId;
			return this;
		}

		public OrderPaySeqBuilder transactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public OrderPaySeqBuilder mchId(String mchId) {
			this.mchId = mchId;
			return this;
		}

		public OrderPaySeqBuilder appId(String appId) {
			this.appId = appId;
			return this;
		}

		public OrderPaySeqBuilder status(Integer status) {
			this.status = status;
			return this;
		}

		public OrderPaySeqBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public OrderPaySeqBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderPaySeqBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderPaySeqBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderPaySeqBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderPaySeqBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderPaySeqBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderPaySeqBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderPaySeq build() {
			OrderPaySeq orderPaySeq = new OrderPaySeq();
			orderPaySeq.setPayCode(payCode);
			orderPaySeq.setCustomerId(customerId);
			orderPaySeq.setPayerBankCode(payerBankCode);
			orderPaySeq.setActualAmount(actualAmount);
			orderPaySeq.setPrepayId(prepayId);
			orderPaySeq.setTransactionId(transactionId);
			orderPaySeq.setMchId(mchId);
			orderPaySeq.setAppId(appId);
			orderPaySeq.setStatus(status);
			orderPaySeq.setRemark(remark);
			orderPaySeq.setId(id);
			orderPaySeq.setCreatedBy(createBy);
			orderPaySeq.setLastModifiedBy(lastModifiedBy);
			orderPaySeq.setCreateTime(createTime);
			orderPaySeq.setLastModifiedTime(lastModifiedTime);
			orderPaySeq.setVersion(version);
			orderPaySeq.setDelFlag(delFlag);
			return orderPaySeq;
		}
	}
}
