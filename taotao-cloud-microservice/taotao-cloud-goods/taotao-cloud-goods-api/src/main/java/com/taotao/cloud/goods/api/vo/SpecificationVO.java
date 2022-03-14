package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 规格项表规格项
 */
@Data
@Schema(description = "参数组关联的参数集合")
public class SpecificationVO {

	/**
	 * 规格名称
	 */
	@Schema(description = "规格名称")
	private String specName;

	/**
	 * 所属卖家 0属于平台
	 * <p>
	 * 店铺自定义规格暂时废弃 2021-06-23 后续推出新配置方式
	 */
	@Schema(description = "所属卖家 0属于平台")
	private String storeId;

	/**
	 * 规格值名字, 《,》分割
	 */
	@Schema(description = "规格值名字, 《,》分割")
	private String specValue;

}
