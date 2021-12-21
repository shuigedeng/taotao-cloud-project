package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 店铺结算日
 *
 * 
 * @since 2021/2/20 3:24 下午
 */
@Schema(description = "店铺结算日")
public class StoreSettlementDay {

	@Schema(description = "店铺ID")
	private String storeId;

	@Schema(description = "结算日")
	private Date settlementDay;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Date getSettlementDay() {
		return settlementDay;
	}

	public void setSettlementDay(Date settlementDay) {
		this.settlementDay = settlementDay;
	}
}
