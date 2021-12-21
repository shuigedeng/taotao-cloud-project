package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 会员预存款
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberWallet.TABLE_NAME)
@TableName(MemberWallet.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberWallet.TABLE_NAME, comment = "会员预存款表")
public class MemberWallet extends BaseSuperEntity<MemberWallet, Long> {

	public static final String TABLE_NAME = "li_member_wallet";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;

	@Column(name = "member_wallet", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '会员预存款'")
	private BigDecimal memberWallet;

	@Column(name = "member_frozen_wallet", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '会员预存款冻结金额,提现使用'")
	private BigDecimal memberFrozenWallet;

	@Column(name = "wallet_password", nullable = false, columnDefinition = "varchar(32) not null comment '预存款密码'")
	private String walletPassword;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getMemberWallet() {
		return memberWallet;
	}

	public void setMemberWallet(BigDecimal memberWallet) {
		this.memberWallet = memberWallet;
	}

	public BigDecimal getMemberFrozenWallet() {
		return memberFrozenWallet;
	}

	public void setMemberFrozenWallet(BigDecimal memberFrozenWallet) {
		this.memberFrozenWallet = memberFrozenWallet;
	}

	public String getWalletPassword() {
		return walletPassword;
	}

	public void setWalletPassword(String walletPassword) {
		this.walletPassword = walletPassword;
	}
}
