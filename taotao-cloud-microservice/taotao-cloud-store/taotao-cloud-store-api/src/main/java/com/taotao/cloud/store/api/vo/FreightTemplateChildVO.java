package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 运费模板子配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "运费模板子配置vo")
public class FreightTemplateChildVO {

	private Long freightTemplateId;

	private BigDecimal firstCompany;

	private BigDecimal firstPrice;

	private BigDecimal continuedCompany;

	private BigDecimal continuedPrice;

	private String area;

	private String areaId;
}
