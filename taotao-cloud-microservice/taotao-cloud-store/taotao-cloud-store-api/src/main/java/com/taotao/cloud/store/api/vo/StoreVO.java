package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺VO
 *
 * 
 * @since 2020-03-07 17:02:05
 */
@Schema(description = "店铺VO")
//public class StoreVO extends Store {
public class StoreVO {

	@Schema(description = "库存预警数量")
	private Integer stockWarning;

	@Schema(description = "登录用户的昵称")
	private String nickName;

	public Integer getStockWarning() {
		return stockWarning;
	}

	public void setStockWarning(Integer stockWarning) {
		this.stockWarning = stockWarning;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
