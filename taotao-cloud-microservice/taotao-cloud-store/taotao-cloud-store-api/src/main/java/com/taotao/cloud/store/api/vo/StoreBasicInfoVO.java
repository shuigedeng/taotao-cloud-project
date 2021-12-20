package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺基本信息DTO
 *
 * 
 * @since 2020/12/7 14:43
 */
@Schema(description = "店铺基本信息DTO")
public class StoreBasicInfoVO {

	@Schema(description = "店铺ID")
	private String storeId;

	@Schema(description = "店铺名称")
	private String storeName;

	/**
	 * @see StoreStatusEnum
	 */
	@Schema(description = "店铺状态")
	private String storeDisable;

	@Schema(description = "地址名称， '，'分割")
	private String companyAddressPath;

	@Schema(description = "店铺logo")
	private String storeLogo;

	@Schema(description = "店铺简介")
	private String storeDesc;

	@Schema(description = "PC端页面")
	private String pcPageData;

	@Schema(description = "移动端页面")
	private String mobilePageData;

	@Schema(description = "是否自营")
	private String selfOperated;

	@Schema(description = "商品数量")
	private Integer goodsNum;

	@Schema(description = "收藏数量")
	private Integer collectionNum;

	@Schema(description = "腾讯云智服唯一标识")
	private String yzfSign;

	@Schema(description = "腾讯云智服小程序唯一标识")
	private String yzfMpSign;

	@Schema(description = "udesk标识")
	private String merchantEuid;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreDisable() {
		return storeDisable;
	}

	public void setStoreDisable(String storeDisable) {
		this.storeDisable = storeDisable;
	}

	public String getCompanyAddressPath() {
		return companyAddressPath;
	}

	public void setCompanyAddressPath(String companyAddressPath) {
		this.companyAddressPath = companyAddressPath;
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

	public String getPcPageData() {
		return pcPageData;
	}

	public void setPcPageData(String pcPageData) {
		this.pcPageData = pcPageData;
	}

	public String getMobilePageData() {
		return mobilePageData;
	}

	public void setMobilePageData(String mobilePageData) {
		this.mobilePageData = mobilePageData;
	}

	public String getSelfOperated() {
		return selfOperated;
	}

	public void setSelfOperated(String selfOperated) {
		this.selfOperated = selfOperated;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	public String getYzfSign() {
		return yzfSign;
	}

	public void setYzfSign(String yzfSign) {
		this.yzfSign = yzfSign;
	}

	public String getYzfMpSign() {
		return yzfMpSign;
	}

	public void setYzfMpSign(String yzfMpSign) {
		this.yzfMpSign = yzfMpSign;
	}

	public String getMerchantEuid() {
		return merchantEuid;
	}

	public void setMerchantEuid(String merchantEuid) {
		this.merchantEuid = merchantEuid;
	}
}
