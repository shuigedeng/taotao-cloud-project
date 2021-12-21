package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 预存款充值记录
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = Recharge.TABLE_NAME)
@TableName(Recharge.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Recharge.TABLE_NAME, comment = "预存款充值记录表")
public class Recharge extends BaseSuperEntity<Recharge, Long> {

	public static final String TABLE_NAME = "li_recharge";

	@Column(name = "recharge_sn", nullable = false, columnDefinition = "varchar(32) not null comment '充值订单编号'")
	private String rechargeSn;

	@Schema(description = "会员id")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	@Column(name = "recharge_money", nullable = false, columnDefinition = "decimal(10,2) not null comment '充值金额'")
	private BigDecimal rechargeMoney;

	@Column(name = "recharge_way", nullable = false, columnDefinition = "varchar(32) not null comment '充值方式，如：支付宝，微信'")
	private String rechargeWay;

	@Column(name = "pay_status", nullable = false, columnDefinition = "varchar(32) not null comment '支付状态'")
	private String payStatus;

	@Column(name = "payment_plugin_id", nullable = false, columnDefinition = "varchar(32) not null comment '支付插件id'")
	private String paymentPluginId;

	@Column(name = "receivable_no", nullable = false, columnDefinition = "varchar(32) not null comment '第三方流水'")
	private String receivableNo;

	@Column(name = "pay_time", nullable = false, columnDefinition = "TIMESTAMP  comment '支付时间'")
	private LocalDateTime payTime;

	/**
	 * 构建充值账单信息
	 *
	 * @param rechargeSn 充值订单号
	 * @param memberId   会员id
	 * @param memberName 会员名称
	 * @param money      充值金额
	 */
	//public Recharge(String rechargeSn, String memberId, String memberName, Double money) {
	//	this.rechargeSn = rechargeSn;
	//	this.memberId = memberId;
	//	this.memberName = memberName;
	//	this.rechargeMoney = money;
	//	this.payStatus = PayStatusEnum.UNPAID.name();
	//}


	public String getRechargeSn() {
		return rechargeSn;
	}

	public void setRechargeSn(String rechargeSn) {
		this.rechargeSn = rechargeSn;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public String getRechargeWay() {
		return rechargeWay;
	}

	public void setRechargeWay(String rechargeWay) {
		this.rechargeWay = rechargeWay;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	public String getReceivableNo() {
		return receivableNo;
	}

	public void setReceivableNo(String receivableNo) {
		this.receivableNo = receivableNo;
	}

	public LocalDateTime getPayTime() {
		return payTime;
	}

	public void setPayTime(LocalDateTime payTime) {
		this.payTime = payTime;
	}
}
