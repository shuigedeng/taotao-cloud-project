package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 店铺-银行信息
 *
 * 
 * @since 2020/12/7 15:54
 */
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

	public String getSettlementBankAccountName() {
		return settlementBankAccountName;
	}

	public void setSettlementBankAccountName(String settlementBankAccountName) {
		this.settlementBankAccountName = settlementBankAccountName;
	}

	public String getSettlementBankAccountNum() {
		return settlementBankAccountNum;
	}

	public void setSettlementBankAccountNum(String settlementBankAccountNum) {
		this.settlementBankAccountNum = settlementBankAccountNum;
	}

	public String getSettlementBankBranchName() {
		return settlementBankBranchName;
	}

	public void setSettlementBankBranchName(String settlementBankBranchName) {
		this.settlementBankBranchName = settlementBankBranchName;
	}

	public String getSettlementBankJointName() {
		return settlementBankJointName;
	}

	public void setSettlementBankJointName(String settlementBankJointName) {
		this.settlementBankJointName = settlementBankJointName;
	}

	public String getSettlementBankLicencePhoto() {
		return settlementBankLicencePhoto;
	}

	public void setSettlementBankLicencePhoto(String settlementBankLicencePhoto) {
		this.settlementBankLicencePhoto = settlementBankLicencePhoto;
	}
}
