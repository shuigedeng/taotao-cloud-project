package com.taotao.cloud.store.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺设置
 *
 * @since 2020/12/16 15:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
