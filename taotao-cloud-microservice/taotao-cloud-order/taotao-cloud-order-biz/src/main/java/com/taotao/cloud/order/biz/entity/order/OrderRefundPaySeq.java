package com.taotao.cloud.order.biz.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 退款流水表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:46
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderRefundPaySeq.TABLE_NAME)
@Table(name = OrderRefundPaySeq.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderRefundPaySeq.TABLE_NAME, comment = "退款流水表")
public class OrderRefundPaySeq  extends BaseSuperEntity<OrderRefundPaySeq,Long> {

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
	@Column(name = "steward_id", columnDefinition = "bigint not null comment '管家id'")
	private Long stewardId;

	/**
	 * 退款金额
	 */
	@Column(name = "amount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '退款金额'")
	private BigDecimal amount;

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
	@Column(name = "refund_date", columnDefinition = "datetime comment '退款时间'")
	private LocalDateTime refundDate;

	/**
	 * 创建日期
	 */
	@Column(name = "create_date", columnDefinition = "datetime comment '创建日期'")
	private LocalDateTime createDate;
}
