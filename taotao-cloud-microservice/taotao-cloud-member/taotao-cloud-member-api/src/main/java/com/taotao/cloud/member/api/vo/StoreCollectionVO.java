package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员店铺收藏VO
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Schema(description = "租会员店铺收藏VO户id")
public class StoreCollectionVO {

	@Schema(description = "店铺id")
	private String id;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺Logo")
	private String storeLogo;

	@Schema(description = "是否自营")
	private Boolean selfOperated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Boolean getSelfOperated() {
		return selfOperated;
	}

	public void setSelfOperated(Boolean selfOperated) {
		this.selfOperated = selfOperated;
	}
}
