package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
 * 后台添加店铺信息DTO
 *
 * 
 * @since 2020/12/12 11:35
 */
@Schema(description = "后台添加店铺信息DTO")
public class AdminStoreApplyDTO {

	/****店铺基本信息***/
	@Schema(description = "会员ID")
	public String memberId;

	@Size(min = 2, max = 200, message = "店铺名称长度为2-200位")
	@NotBlank(message = "店铺名称不能为空")
	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺logo")
	private String storeLogo;

	@Size(min = 6, max = 200, message = "店铺简介需在6-200字符之间")
	@NotBlank(message = "店铺简介不能为空")
	@Schema(description = "店铺简介")
	private String storeDesc;

	@Schema(description = "经纬度")
	private String storeCenter;

	@Schema(description = "店铺经营类目")
	private String goodsManagementCategory;

	@Schema(description = "是否自营")
	private Boolean selfOperated;

	@Schema(description = "地址名称， '，'分割")
	private String storeAddressPath;

	@Schema(description = "地址id，'，'分割 ")
	private String storeAddressIdPath;

	@Schema(description = "详细地址")
	private String storeAddressDetail;

	/****公司基本信息***/
	@NotBlank(message = "公司名称不能为空")
	@Size(min = 2, max = 100, message = "公司名称错误")
	@Schema(description = "公司名称")
	private String companyName;

	//@Mobile
	@Schema(description = "公司电话")
	private String companyPhone;

	@NotBlank(message = "公司地址不能为空")
	@Size(min = 1, max = 200, message = "公司地址,长度为1-200字符")
	@Schema(description = "公司地址")
	private String companyAddress;

	@Schema(description = "公司地址地区Id")
	private String companyAddressIdPath;

	@Schema(description = "公司地址地区")
	private String companyAddressPath;

	@Schema(description = "员工总数")
	private String employeeNum;

	@Min(value = 1, message = "注册资金,至少一位")
	@Schema(description = "注册资金")
	private Double registeredCapital;

	@NotBlank(message = "联系人姓名为空")
	@Length(min = 2, max = 20, message = "联系人长度为：2-20位字符")
	@Schema(description = "联系人姓名")
	private String linkName;

	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	@Schema(description = "联系人电话")
	private String linkPhone;

	@Email
	@Schema(description = "电子邮箱")
	private String companyEmail;


	/****营业执照信息***/
	@Size(min = 18, max = 18, message = "营业执照长度为18位字符")
	@Schema(description = "营业执照号")
	private String licenseNum;

	@Size(min = 1, max = 200, message = "法定经营范围长度为1-200位字符")
	@Schema(description = "法定经营范围")
	private String scope;

	@NotBlank(message = "营业执照电子版不能为空")
	@Schema(description = "营业执照电子版")
	private String licencePhoto;

	/****法人信息***/
	@NotBlank(message = "法人姓名不能为空")
	@Size(min = 2, max = 20, message = "法人姓名长度为2-20位字符")
	@Schema(description = "法人姓名")
	private String legalName;

	@NotBlank(message = "法人身份证不能为空")
	@Size(min = 18, max = 18, message = "法人身份证号长度为18位")
	@Schema(description = "法人身份证")
	private String legalId;

	@NotBlank(message = "法人身份证不能为空")
	@Schema(description = "法人身份证照片")
	private String legalPhoto;

	/****结算银行信息***/
	@Size(min = 1, max = 200, message = "结算银行开户行名称长度为1-200位")
	@NotBlank(message = "结算银行开户行名称不能为空")
	@Schema(description = "结算银行开户行名称")
	private String settlementBankAccountName;

	@Size(min = 1, max = 200, message = "结算银行开户账号长度为1-200位")
	@NotBlank(message = "结算银行开户账号不能为空")
	@Schema(description = "结算银行开户账号")
	private String settlementBankAccountNum;

	@Size(min = 1, max = 200, message = "结算银行开户支行名称长度为1-200位")
	@NotBlank(message = "结算银行开户支行名称不能为空")
	@Schema(description = "结算银行开户支行名称")
	private String settlementBankBranchName;

	@Size(min = 1, max = 50, message = "结算银行支行联行号长度为1-200位")
	@NotBlank(message = "结算银行支行联行号不能为空")
	@Schema(description = "结算银行支行联行号")
	private String settlementBankJointName;

	/****店铺退货收件地址***/
	@Schema(description = "收货人姓名")
	private String salesConsigneeName;

	@Schema(description = "收件人手机")
	private String salesConsigneeMobile;

	@Schema(description = "地址Id， '，'分割")
	private String salesConsigneeAddressId;

	@Schema(description = "地址名称， '，'分割")
	private String salesConsigneeAddressPath;

	@Schema(description = "详细地址")
	private String salesConsigneeDetail;


	/****配送信息***/
	@Schema(description = "同城配送达达店铺编码")
	private String ddCode;

	/****结算周期***/
	@Schema(description = "结算周期")
	private String settlementCycle;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreDesc() {
		return storeDesc;
	}

	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}

	public String getStoreCenter() {
		return storeCenter;
	}

	public void setStoreCenter(String storeCenter) {
		this.storeCenter = storeCenter;
	}

	public String getGoodsManagementCategory() {
		return goodsManagementCategory;
	}

	public void setGoodsManagementCategory(String goodsManagementCategory) {
		this.goodsManagementCategory = goodsManagementCategory;
	}

	public Boolean getSelfOperated() {
		return selfOperated;
	}

	public void setSelfOperated(Boolean selfOperated) {
		this.selfOperated = selfOperated;
	}

	public String getStoreAddressPath() {
		return storeAddressPath;
	}

	public void setStoreAddressPath(String storeAddressPath) {
		this.storeAddressPath = storeAddressPath;
	}

	public String getStoreAddressIdPath() {
		return storeAddressIdPath;
	}

	public void setStoreAddressIdPath(String storeAddressIdPath) {
		this.storeAddressIdPath = storeAddressIdPath;
	}

	public String getStoreAddressDetail() {
		return storeAddressDetail;
	}

	public void setStoreAddressDetail(String storeAddressDetail) {
		this.storeAddressDetail = storeAddressDetail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyAddressIdPath() {
		return companyAddressIdPath;
	}

	public void setCompanyAddressIdPath(String companyAddressIdPath) {
		this.companyAddressIdPath = companyAddressIdPath;
	}

	public String getCompanyAddressPath() {
		return companyAddressPath;
	}

	public void setCompanyAddressPath(String companyAddressPath) {
		this.companyAddressPath = companyAddressPath;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	public Double getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(Double registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLicencePhoto() {
		return licencePhoto;
	}

	public void setLicencePhoto(String licencePhoto) {
		this.licencePhoto = licencePhoto;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalId() {
		return legalId;
	}

	public void setLegalId(String legalId) {
		this.legalId = legalId;
	}

	public String getLegalPhoto() {
		return legalPhoto;
	}

	public void setLegalPhoto(String legalPhoto) {
		this.legalPhoto = legalPhoto;
	}

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

	public String getSalesConsigneeName() {
		return salesConsigneeName;
	}

	public void setSalesConsigneeName(String salesConsigneeName) {
		this.salesConsigneeName = salesConsigneeName;
	}

	public String getSalesConsigneeMobile() {
		return salesConsigneeMobile;
	}

	public void setSalesConsigneeMobile(String salesConsigneeMobile) {
		this.salesConsigneeMobile = salesConsigneeMobile;
	}

	public String getSalesConsigneeAddressId() {
		return salesConsigneeAddressId;
	}

	public void setSalesConsigneeAddressId(String salesConsigneeAddressId) {
		this.salesConsigneeAddressId = salesConsigneeAddressId;
	}

	public String getSalesConsigneeAddressPath() {
		return salesConsigneeAddressPath;
	}

	public void setSalesConsigneeAddressPath(String salesConsigneeAddressPath) {
		this.salesConsigneeAddressPath = salesConsigneeAddressPath;
	}

	public String getSalesConsigneeDetail() {
		return salesConsigneeDetail;
	}

	public void setSalesConsigneeDetail(String salesConsigneeDetail) {
		this.salesConsigneeDetail = salesConsigneeDetail;
	}

	public String getDdCode() {
		return ddCode;
	}

	public void setDdCode(String ddCode) {
		this.ddCode = ddCode;
	}

	public String getSettlementCycle() {
		return settlementCycle;
	}

	public void setSettlementCycle(String settlementCycle) {
		this.settlementCycle = settlementCycle;
	}
}
