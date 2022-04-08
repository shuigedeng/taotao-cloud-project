package com.taotao.cloud.store.api.vo;

import com.taotao.cloud.store.api.enums.FreightTemplateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 运费模板vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺运费模板")
public class FreightTemplateBaseVO {

	private Long id;

	private Long storeId;

	private String name;

	/**
	 * @see FreightTemplateEnum
	 */
	private String pricingMethod;
}
