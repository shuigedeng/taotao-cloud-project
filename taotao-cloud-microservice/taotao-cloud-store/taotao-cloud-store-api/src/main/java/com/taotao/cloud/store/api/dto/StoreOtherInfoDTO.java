package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 店铺入驻其他信息
 *
 * 
 * @since 2020/12/7 16:16
 */
@Schema(description = "店铺入驻其他信息")
public class StoreOtherInfoDTO {

	@Size(min = 2, max = 200)
	@NotBlank(message = "店铺名称不能为空")
	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺logo")
	private String storeLogo;

	@Size(min = 6, max = 200)
	@NotBlank(message = "店铺简介不能为空")
	@Schema(description = "店铺简介")
	private String storeDesc;

	@Schema(description = "经纬度")
	@NotEmpty
	private String storeCenter;

	@NotBlank(message = "店铺经营类目不能为空")
	@Schema(description = "店铺经营类目")
	private String goodsManagementCategory;

	@NotBlank(message = "地址不能为空")
	@Schema(description = "地址名称， '，'分割")
	private String storeAddressPath;

	@NotBlank(message = "地址ID不能为空")
	@Schema(description = "地址id，'，'分割 ")
	private String storeAddressIdPath;

	@NotBlank(message = "地址详情")
	@Schema(description = "地址详情")
	private String storeAddressDetail;

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
}
