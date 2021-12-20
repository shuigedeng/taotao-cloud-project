package com.taotao.cloud.store.api.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺其他信息
 * 
 * @date: 2021/8/11 3:42 下午
 *
 */
@Schema(description = "店铺其他信息")
public class StoreOtherVO {

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司地址")
    private String companyAddress;

    @Schema(description = "公司地址地区")
    private String companyAddressPath;

    @Schema(description = "营业执照电子版")
    private String licencePhoto;

    @Schema(description = "法定经营范围")
    private String scope;

    @Schema(description = "员工总数")
    private Integer employeeNum;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyAddressPath() {
		return companyAddressPath;
	}

	public void setCompanyAddressPath(String companyAddressPath) {
		this.companyAddressPath = companyAddressPath;
	}

	public String getLicencePhoto() {
		return licencePhoto;
	}

	public void setLicencePhoto(String licencePhoto) {
		this.licencePhoto = licencePhoto;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(Integer employeeNum) {
		this.employeeNum = employeeNum;
	}
}
