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
 * 退款流水表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:46
 */
@Entity
@TableName(OrderRefundPaySeq.TABLE_NAME)
@Table(name = OrderRefundPaySeq.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderRefundPaySeq.TABLE_NAME, comment = "退款流水表")
public class OrderRefundPaySeq  extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "order_refund_pay_seq";

	/**
	 * 售后申请ID
	 */
	@Column(name = "refund_code", columnDefinition = "varchar(32) not null comment '售后申请ID'")
	private String refundCode;

	/**
	 * 管家审核日期
	 */
	@Column(name = "steward_audit_date", columnDefinition = "TIMESTAMP comment '管家审核日期'")
	private LocalDateTime stewardAuditDate;

	/**
	 * 管家id
	 */
	@Column(name = "steward_id", columnDefinition = "varchar(32) not null comment '管家id'")
	private String stewardId;

	/**
	 * 退款金额
	 */
	@Column(name = "amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '退款金额'")
	private BigDecimal amount = BigDecimal.ZERO;

	/**
	 * 微信退款ID
	 */
	@Column(name = "wx_refund_id", columnDefinition = "varchar(32) not null comment '微信退款ID'")
	private String wxRefundId;

	/**
	 * 微信退款渠道 需要通过微信 “查询退款”接口设置
	 */
	@Column(name = "wx_refund_chanel", columnDefinition = "varchar(32) not null comment '微信退款渠道 需要通过微信 “查询退款”接口设置'")
	private String wxRefundChanel;

	/**
	 * 微信退款状态 需要通过微信 “查询退款”接口设置
	 */
	@Column(name = "wx_refund_status", columnDefinition = "int not null default 0 comment ' 微信退款状态 需要通过微信 “查询退款”接口设置'")
	private Integer wxRefundStatus;

	/**
	 * 微信退款收款账户 需要通过微信 “查询退款”接口设置
	 */
	@Column(name = "wx_refund_target", columnDefinition = "varchar(32) not null comment '微信退款收款账户 需要通过微信 “查询退款”接口设置'")
	private String wxRefundTarget;

	/**
	 * 退款时间
	 */
	@Column(name = "refund_date", columnDefinition = "TIMESTAMP comment '退款时间'")
	private LocalDateTime refundDate;

	/**
	 * 创建日期
	 */
	@Column(name = "create_date", columnDefinition = "TIMESTAMP comment '创建日期'")
	private LocalDateTime createDate;

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public LocalDateTime getStewardAuditDate() {
		return stewardAuditDate;
	}

	public void setStewardAuditDate(LocalDateTime stewardAuditDate) {
		this.stewardAuditDate = stewardAuditDate;
	}

	public String getStewardId() {
		return stewardId;
	}

	public void setStewardId(String stewardId) {
		this.stewardId = stewardId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getWxRefundId() {
		return wxRefundId;
	}

	public void setWxRefundId(String wxRefundId) {
		this.wxRefundId = wxRefundId;
	}

	public String getWxRefundChanel() {
		return wxRefundChanel;
	}

	public void setWxRefundChanel(String wxRefundChanel) {
		this.wxRefundChanel = wxRefundChanel;
	}

	public Integer getWxRefundStatus() {
		return wxRefundStatus;
	}

	public void setWxRefundStatus(Integer wxRefundStatus) {
		this.wxRefundStatus = wxRefundStatus;
	}

	public String getWxRefundTarget() {
		return wxRefundTarget;
	}

	public void setWxRefundTarget(String wxRefundTarget) {
		this.wxRefundTarget = wxRefundTarget;
	}

	public LocalDateTime getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(LocalDateTime refundDate) {
		this.refundDate = refundDate;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public OrderRefundPaySeq() {
	}

	public OrderRefundPaySeq(String refundCode, LocalDateTime stewardAuditDate,
		String stewardId, BigDecimal amount, String wxRefundId, String wxRefundChanel,
		Integer wxRefundStatus, String wxRefundTarget, LocalDateTime refundDate,
		LocalDateTime createDate) {
		this.refundCode = refundCode;
		this.stewardAuditDate = stewardAuditDate;
		this.stewardId = stewardId;
		this.amount = amount;
		this.wxRefundId = wxRefundId;
		this.wxRefundChanel = wxRefundChanel;
		this.wxRefundStatus = wxRefundStatus;
		this.wxRefundTarget = wxRefundTarget;
		this.refundDate = refundDate;
		this.createDate = createDate;
	}

	public static OrderRefundPaySeqBuilder builder() {
		return new OrderRefundPaySeqBuilder();
	}

	public static final class OrderRefundPaySeqBuilder {

		private String refundCode;
		private LocalDateTime stewardAuditDate;
		private String stewardId;
		private BigDecimal amount = BigDecimal.ZERO;
		private String wxRefundId;
		private String wxRefundChanel;
		private Integer wxRefundStatus;
		private String wxRefundTarget;
		private LocalDateTime refundDate;
		private LocalDateTime createDate;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderRefundPaySeqBuilder() {
		}


		public OrderRefundPaySeqBuilder refundCode(String refundCode) {
			this.refundCode = refundCode;
			return this;
		}

		public OrderRefundPaySeqBuilder stewardAuditDate(LocalDateTime stewardAuditDate) {
			this.stewardAuditDate = stewardAuditDate;
			return this;
		}

		public OrderRefundPaySeqBuilder stewardId(String stewardId) {
			this.stewardId = stewardId;
			return this;
		}

		public OrderRefundPaySeqBuilder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public OrderRefundPaySeqBuilder wxRefundId(String wxRefundId) {
			this.wxRefundId = wxRefundId;
			return this;
		}

		public OrderRefundPaySeqBuilder wxRefundChanel(String wxRefundChanel) {
			this.wxRefundChanel = wxRefundChanel;
			return this;
		}

		public OrderRefundPaySeqBuilder wxRefundStatus(Integer wxRefundStatus) {
			this.wxRefundStatus = wxRefundStatus;
			return this;
		}

		public OrderRefundPaySeqBuilder wxRefundTarget(String wxRefundTarget) {
			this.wxRefundTarget = wxRefundTarget;
			return this;
		}

		public OrderRefundPaySeqBuilder refundDate(LocalDateTime refundDate) {
			this.refundDate = refundDate;
			return this;
		}

		public OrderRefundPaySeqBuilder createDate(LocalDateTime createDate) {
			this.createDate = createDate;
			return this;
		}

		public OrderRefundPaySeqBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderRefundPaySeqBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderRefundPaySeqBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderRefundPaySeqBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderRefundPaySeqBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderRefundPaySeqBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderRefundPaySeqBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderRefundPaySeq build() {
			OrderRefundPaySeq orderRefundPaySeq = new OrderRefundPaySeq();
			orderRefundPaySeq.setRefundCode(refundCode);
			orderRefundPaySeq.setStewardAuditDate(stewardAuditDate);
			orderRefundPaySeq.setStewardId(stewardId);
			orderRefundPaySeq.setAmount(amount);
			orderRefundPaySeq.setWxRefundId(wxRefundId);
			orderRefundPaySeq.setWxRefundChanel(wxRefundChanel);
			orderRefundPaySeq.setWxRefundStatus(wxRefundStatus);
			orderRefundPaySeq.setWxRefundTarget(wxRefundTarget);
			orderRefundPaySeq.setRefundDate(refundDate);
			orderRefundPaySeq.setCreateDate(createDate);
			orderRefundPaySeq.setId(id);
			orderRefundPaySeq.setCreatedBy(createBy);
			orderRefundPaySeq.setLastModifiedBy(lastModifiedBy);
			orderRefundPaySeq.setCreateTime(createTime);
			orderRefundPaySeq.setLastModifiedTime(lastModifiedTime);
			orderRefundPaySeq.setVersion(version);
			orderRefundPaySeq.setDelFlag(delFlag);
			return orderRefundPaySeq;
		}
	}
}
