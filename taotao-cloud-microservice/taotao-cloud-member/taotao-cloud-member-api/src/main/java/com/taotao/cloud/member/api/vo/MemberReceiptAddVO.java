package com.taotao.cloud.member.api.vo;

import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


/**
 * 会员发票添加VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员发票")
public class MemberReceiptAddVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -8267092982915677995L;

	@Schema(description = "唯一标识", hidden = true)
	private String id;

	@Schema(description = "发票抬头")
	private String receiptTitle;

	@Schema(description = "纳税人识别号")
	private String taxpayerId;

	@Schema(description = "发票内容")
	private String receiptContent;

	/**
	 * @see MemberReceiptEnum
	 */
	@Schema(description = "发票类型")
	private String receiptType;

	@Schema(description = "是否为默认选项 0：否，1：是")
	private Integer isDefault;

}
