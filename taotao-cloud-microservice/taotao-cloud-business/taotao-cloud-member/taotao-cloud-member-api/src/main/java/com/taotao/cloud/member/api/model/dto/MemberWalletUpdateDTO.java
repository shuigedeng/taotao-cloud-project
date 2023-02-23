package com.taotao.cloud.member.api.model.dto;

import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 会员余额变动模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberWalletUpdateDTO {

	@Schema(description = "变动金额")
	private BigDecimal money;
	@Schema(description = "变动会员id")
	private Long memberId;
	@Schema(description = "日志详情")
	private String detail;

	/**
	 * @see DepositServiceTypeEnum
	 */
	@Schema(description = "变动业务原因")
	private String serviceType;
}
