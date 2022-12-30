package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;


/**
 * 会员发票添加VO
 *
 * @param receiptType
 */
@Schema(description = "会员发票")
public record MemberReceiptAddVO(@Schema(description = "唯一标识", hidden = true) String id,
                                 @Schema(description = "发票抬头") String receiptTitle,
                                 @Schema(description = "纳税人识别号") String taxpayerId,
                                 @Schema(description = "发票内容") String receiptContent,
                                 @Schema(description = "发票类型") String receiptType,
                                 @Schema(description = "是否为默认选项 0：否，1：是") Integer isDefault) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -8267092982915677995L;

}
