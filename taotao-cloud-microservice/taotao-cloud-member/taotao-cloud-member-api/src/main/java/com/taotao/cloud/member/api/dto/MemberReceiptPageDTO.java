package com.taotao.cloud.member.api.dto;

import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员发票查询DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员发票查询DTO")
public class MemberReceiptPageDTO extends PageParam {

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
