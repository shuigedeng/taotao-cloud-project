package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * MemberReceipt
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:28:38
 */
@Entity
@Table(name = MemberReceipt.TABLE_NAME)
@TableName(MemberReceipt.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberReceipt.TABLE_NAME, comment = "会员发票表")
public class MemberReceipt extends BaseSuperEntity<MemberReceipt, Long> {

	public static final String TABLE_NAME = "li_member_receipt";
	/**
	 * 发票抬头
	 */
	@Column(name = "receipt_title", nullable = false, columnDefinition = "varchar(32) not null comment '发票抬头'")
	private String receiptTitle;

	/**
	 * 纳税人识别号
	 */
	@Column(name = "taxpayer_id", nullable = false, columnDefinition = "varchar(32) not null comment '纳税人识别号'")
	private String taxpayerId;

	/**
	 * 发票内容
	 */
	@Column(name = "receipt_content", nullable = false, columnDefinition = "varchar(32) not null comment '发票内容'")
	private String receiptContent;

	/**
	 * 会员ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;

	/**
	 * 会员名称
	 */
	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(32) not null comment '会员名称'")
	private String memberName;

	/**
	 * 发票类型
	 *
	 * @see MemberReceiptEnum
	 */
	@Column(name = "receipt_type", nullable = false, columnDefinition = "varchar(32) not null comment '发票类型'")
	private String receiptType;

	/**
	 * 是否为默认选项 0：否，1：是
	 */
	@Column(name = "defaulted", nullable = false, columnDefinition = "int not null default 0 comment '是否为默认选项 0：否，1：是'")
	private Integer defaulted;

	/**
	 * 删除标志 true/false 删除/未删除
	 */
	@Column(name = "delete_flag", nullable = false, columnDefinition = "boolean not null default false comment '删除标志 true/false 删除/未删除'")
	private Boolean deleteFlag;

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public String getTaxpayerId() {
		return taxpayerId;
	}

	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Integer defaulted) {
		this.defaulted = defaulted;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
