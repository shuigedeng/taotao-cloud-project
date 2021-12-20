package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺售后收件地址
 *
 * 
 * @since 2020-08-22 15:10:51
 */
@Schema(description = "店铺售后收件地址")
public class StoreAfterSaleAddressDTO {

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
}
