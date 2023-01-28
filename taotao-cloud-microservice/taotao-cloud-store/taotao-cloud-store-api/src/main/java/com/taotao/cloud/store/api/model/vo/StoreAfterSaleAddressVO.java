package com.taotao.cloud.store.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺售后收件地址
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺售后收件地址")
public class StoreAfterSaleAddressVO {

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
}
