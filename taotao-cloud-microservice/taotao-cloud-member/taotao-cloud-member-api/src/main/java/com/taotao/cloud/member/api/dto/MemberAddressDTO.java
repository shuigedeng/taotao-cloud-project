package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 会员地址DTO
 *
 * 
 * @since 2020/12/14 16:31
 */
@Schema(description = "租户id")
public class MemberAddressDTO {

	@NotEmpty(message = "收货人姓名不能为空")
	@Schema(description = "收货人姓名")
	private String consigneeName;

	//@Phone
	@Schema(description = "手机号码")
	private String consigneeMobile;

	@NotBlank(message = "地址不能为空")
	@Schema(description = "地址名称， '，'分割")
	private String consigneeAddressPath;

	@NotBlank(message = "地址不能为空")
	@Schema(description = "地址id，'，'分割 ")
	private String consigneeAddressIdPath;

	@NotEmpty(message = "详细地址不能为空")
	@Schema(description = "详细地址")
	private String consigneeDetail;

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeAddressPath() {
		return consigneeAddressPath;
	}

	public void setConsigneeAddressPath(String consigneeAddressPath) {
		this.consigneeAddressPath = consigneeAddressPath;
	}

	public String getConsigneeAddressIdPath() {
		return consigneeAddressIdPath;
	}

	public void setConsigneeAddressIdPath(String consigneeAddressIdPath) {
		this.consigneeAddressIdPath = consigneeAddressIdPath;
	}

	public String getConsigneeDetail() {
		return consigneeDetail;
	}

	public void setConsigneeDetail(String consigneeDetail) {
		this.consigneeDetail = consigneeDetail;
	}
}
