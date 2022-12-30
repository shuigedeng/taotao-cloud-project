package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;


/**
 * 会员发票查询VO
 *
 * @param receiptType
 */
@Schema(description = "会员发票查询VO")
public record MemberReceiptVO(@Schema(description = "会员ID") String memberId,
                              @Schema(description = "会员名称") String memberName,
                              @Schema(description = "发票类型") String receiptType) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -8210927982915677995L;

	//public LambdaQueryWrapper<MemberReceipt> lambdaQueryWrapper() {
	//	LambdaQueryWrapper<MemberReceipt> queryWrapper = new LambdaQueryWrapper<>();
	//
	//	//会员名称查询
	//	if (StringUtil.isNotEmpty(memberName)) {
	//		queryWrapper.like(MemberReceipt::getMemberName, memberName);
	//	}
	//	//会员id查询
	//	if (StringUtil.isNotEmpty(memberId)) {
	//		queryWrapper.eq(MemberReceipt::getMemberId, memberId);
	//	}
	//	//会员id查询
	//	if (StringUtil.isNotEmpty(receiptType)) {
	//		queryWrapper.eq(MemberReceipt::getReceiptType, receiptType);
	//	}
	//	queryWrapper.eq(MemberReceipt::getDeleteFlag, true);
	//	queryWrapper.orderByDesc(MemberReceipt::getCreateTime);
	//	return queryWrapper;
	//}
}
