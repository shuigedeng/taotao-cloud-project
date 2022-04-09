package com.taotao.cloud.order.biz.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 订单支付流水表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:45
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderPaySeq.TABLE_NAME)
@Table(name = OrderPaySeq.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderPaySeq.TABLE_NAME, comment = "订单支付流水表")
public class OrderPaySeq extends BaseSuperEntity<OrderPaySeq, Long> {

	public static final String TABLE_NAME = "order_pay_seq";

	/**
	 * 支付流水编码--需要与微信的预支付ID进行关联
	 */
	@Column(name = "pay_code", columnDefinition = "varchar(32) not null comment '支付流水编码'")
	private String payCode;

	/**
	 * 买家ID
	 */
	@Column(name = "customer_id", columnDefinition = "bigint not null comment '买家ID'")
	private Long customerId;

	/**
	 * 付款方银行编码
	 */
	@Column(name = "payer_bank_code", columnDefinition = "varchar(32) not null comment '付款方银行编码'")
	private String payerBankCode;

	/**
	 * 交易金额
	 */
	@Column(name = "actual_amount", columnDefinition = "decimal(10,2) not null default 0 comment '交易金额'")
	private BigDecimal actualAmount;

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
}
