package com.taotao.cloud.flowable.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 模版详细配置
 *
 * @since 2018-08-22 15:10:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "模版详细配置")
public class FreightTemplateChildDTO {

	private static final long serialVersionUID = -4143478496868965214L;

	private Long freightTemplateId;

	private BigDecimal firstCompany;

	private BigDecimal firstPrice;

	private BigDecimal continuedCompany;

	private BigDecimal continuedPrice;

	private String area;

	private String areaId;

	// ***************************************************************************

	@NotEmpty(message = "计价方式不能为空")
	@Schema(description = "计价方式：按件、按重量", allowableValues = "WEIGHT, NUM")
	private String pricingMethod;

}
