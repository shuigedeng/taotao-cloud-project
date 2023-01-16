package com.taotao.cloud.store.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 店铺-银行信息
 *
 * @since 2020/12/7 15:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺-银行信息")
public class StoreBankDTO {

	@Length(min = 1, max = 200)
	@NotBlank(message = "结算银行开户行名称不能为空")
	@Schema(description = "结算银行开户行名称")
	private String settlementBankAccountName;

	@Length(min = 1, max = 200)
	@NotBlank(message = "结算银行开户账号不能为空")
	@Schema(description = "结算银行开户账号")
	private String settlementBankAccountNum;

	@Length(min = 1, max = 200)
	@NotBlank(message = "结算银行开户支行名称不能为空")
	@Schema(description = "结算银行开户支行名称")
	private String settlementBankBranchName;

	@Length(min = 1, max = 50)
	@NotBlank(message = "结算银行支行联行号不能为空")
	@Schema(description = "结算银行支行联行号")
	private String settlementBankJointName;

	@NotBlank(message = "开户银行许可证电子版不能为空")
	@Schema(description = "开户银行许可证电子版")
	private String settlementBankLicencePhoto;
}
