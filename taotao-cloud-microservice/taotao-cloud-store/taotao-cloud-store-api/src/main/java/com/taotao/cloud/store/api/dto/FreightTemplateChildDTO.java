package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;


/**
 * 模版详细配置
 *
 * 
 * @since 2018-08-22 15:10:51
 */
@Schema(description = "模版详细配置")
//public class FreightTemplateChildDTO extends FreightTemplateChild {
public class FreightTemplateChildDTO {

	private static final long serialVersionUID = -4143478496868965214L;


	@NotEmpty(message = "计价方式不能为空")
	@Schema(description = "计价方式：按件、按重量", allowableValues = "WEIGHT, NUM")
	private String pricingMethod;

	//public FreightTemplateChildDTO(FreightTemplateChild freightTemplateChild) {
	//    BeanUtils.copyProperties(freightTemplateChild, this);
	//}


	public String getPricingMethod() {
		return pricingMethod;
	}

	public void setPricingMethod(String pricingMethod) {
		this.pricingMethod = pricingMethod;
	}
}
