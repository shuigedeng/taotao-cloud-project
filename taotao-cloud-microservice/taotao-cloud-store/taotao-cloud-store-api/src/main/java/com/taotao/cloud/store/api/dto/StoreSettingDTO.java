package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;

/**
 * 店铺设置
 *
 * 
 * @since 2020/12/16 15:15
 */
@Schema(description = "店铺设置")
public class StoreSettingDTO {

	@Schema(description = "店铺logo")
	private String storeLogo;

	@Schema(description = "店铺简介")
	private String storeDesc;

	@Schema(description = "地址id，'，'分割 ")
	private String storeAddressIdPath;

	@Schema(description = "地址名称， '，'分割")
	private String storeAddressPath;

	@Schema(description = "详细地址")
	private String storeAddressDetail;

	@NotEmpty
	@Schema(description = "经纬度")
	private String storeCenter;

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

	public String getStoreAddressIdPath() {
		return storeAddressIdPath;
	}

	public void setStoreAddressIdPath(String storeAddressIdPath) {
		this.storeAddressIdPath = storeAddressIdPath;
	}

	public String getStoreAddressPath() {
		return storeAddressPath;
	}

	public void setStoreAddressPath(String storeAddressPath) {
		this.storeAddressPath = storeAddressPath;
	}

	public String getStoreAddressDetail() {
		return storeAddressDetail;
	}

	public void setStoreAddressDetail(String storeAddressDetail) {
		this.storeAddressDetail = storeAddressDetail;
	}

	public String getStoreCenter() {
		return storeCenter;
	}

	public void setStoreCenter(String storeCenter) {
		this.storeCenter = storeCenter;
	}
}
