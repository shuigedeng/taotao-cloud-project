package com.taotao.cloud.member.api.vo;

import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 会员发票查询VO
 *
 *
 * @since 2021-03-29 14:10:16
 */
@Schema(description = "会员发票查询VO")
public class MemberReceiptVO {

	private static final long serialVersionUID = -8210927982915677995L;

	@Schema(description = "会员ID")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	/**
	 * @see MemberReceiptEnum
	 */
	@Schema(description = "发票类型")
	private String receiptType;

	//public LambdaQueryWrapper<MemberReceipt> lambdaQueryWrapper() {
	//	LambdaQueryWrapper<MemberReceipt> queryWrapper = new LambdaQueryWrapper<>();
	//
	//	//会员名称查询
	//	if (StringUtils.isNotEmpty(memberName)) {
	//		queryWrapper.like(MemberReceipt::getMemberName, memberName);
	//	}
	//	//会员id查询
	//	if (StringUtils.isNotEmpty(memberId)) {
	//		queryWrapper.eq(MemberReceipt::getMemberId, memberId);
	//	}
	//	//会员id查询
	//	if (StringUtils.isNotEmpty(receiptType)) {
	//		queryWrapper.eq(MemberReceipt::getReceiptType, receiptType);
	//	}
	//	queryWrapper.eq(MemberReceipt::getDeleteFlag, true);
	//	queryWrapper.orderByDesc(MemberReceipt::getCreateTime);
	//	return queryWrapper;
	//}


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
}
