package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
 * 店铺-公司信息
 *
 * 
 * @since 2020/12/7 15:50
 */
@Schema(description = "店铺-公司信息")
public class StoreCompanyDTO {

	//公司基本信息

	@Size(min = 2, max = 100)
	@NotBlank(message = "公司名称不能为空")
	@Schema(description = "公司名称")
	private String companyName;

	@Schema(description = "公司地址地区Id")
	private String companyAddressIdPath;

	@Schema(description = "公司地址地区")
	private String companyAddressPath;

	@Size(min = 1, max = 200)
	@NotBlank(message = "公司地址不能为空")
	@Schema(description = "公司地址")
	private String companyAddress;

	//@Mobile
	@Schema(description = "公司电话")
	private String companyPhone;

	@Email
	@Schema(description = "电子邮箱")
	private String companyEmail;

	@Min(1)
	@Schema(description = "员工总数")
	private Integer employeeNum;

	@Min(1)
	@Schema(description = "注册资金")
	private Double registeredCapital;

	@Length(min = 2, max = 20)
	@NotBlank(message = "联系人姓名为空")
	@Schema(description = "联系人姓名")
	private String linkName;

	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	@Schema(description = "联系人电话")
	private String linkPhone;

	//营业执照信息

	@Size(min = 18, max = 18)
	@Schema(description = "营业执照号")
	private String licenseNum;

	@Size(min = 1, max = 200)
	@Schema(description = "法定经营范围")
	private String scope;

	@NotBlank(message = "营业执照电子版不能为空")
	@Schema(description = "营业执照电子版")
	private String licencePhoto;

	//法人信息

	@Size(min = 2, max = 20)
	@NotBlank(message = "法人姓名不能为空")
	@Schema(description = "法人姓名")
	private String legalName;

	@Size(min = 18, max = 18)
	@NotBlank(message = "法人身份证不能为空")
	@Schema(description = "法人身份证")
	private String legalId;

	@NotBlank(message = "法人身份证不能为空")
	@Schema(description = "法人身份证照片")
	private String legalPhoto;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public Integer getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(Integer employeeNum) {
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
}
