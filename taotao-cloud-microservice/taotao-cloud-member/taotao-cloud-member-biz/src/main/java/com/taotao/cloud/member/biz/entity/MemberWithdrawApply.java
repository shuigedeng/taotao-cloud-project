package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员提现申请表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:33:20
 */
@Entity
@Table(name = MemberWithdrawApply.TABLE_NAME)
@TableName(MemberWithdrawApply.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberWithdrawApply.TABLE_NAME, comment = "会员提现申请表")
public class MemberWithdrawApply extends BaseSuperEntity<MemberWithdrawApply, Long> {

	public static final String TABLE_NAME = "tt_member_withdraw_apply";

	/**
	 * 申请提现金额
	 */
	@Column(name = "apply_money", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '申请提现金额'")
	private BigDecimal applyMoney;

	/**
	 * 提现状态
	 */
	@Column(name = "apply_status", nullable = false, columnDefinition = "varchar(32) not null comment '提现状态'")
	private String applyStatus;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	/**
	 * 审核备注
	 */
	@Column(name = "inspect_remark", nullable = false, columnDefinition = "varchar(32) not null comment '审核备注'")
	private String inspectRemark;

	/**
	 * 审核时间
	 */
	@Column(name = "inspect_time", nullable = false, columnDefinition = "TIMESTAMP comment '审核时间'")
	private LocalDateTime inspectTime;

	/**
	 * sn
	 */
	@Column(name = "sn", nullable = false, columnDefinition = "varchar(32) not null comment 'sn'")
	private String sn;

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getInspectRemark() {
		return inspectRemark;
	}

	public void setInspectRemark(String inspectRemark) {
		this.inspectRemark = inspectRemark;
	}

	public LocalDateTime getInspectTime() {
		return inspectTime;
	}

	public void setInspectTime(LocalDateTime inspectTime) {
		this.inspectTime = inspectTime;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
}
