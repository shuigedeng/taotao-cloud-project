package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规格项表规格项
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "参数组关联的参数集合")
public class SpecificationDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4433579132929428572L;

	@Schema(description = "规格名称")
	private String specName;

	@Schema(description = "所属卖家 0属于平台")
	private Long storeId;

	@Schema(description = "规格值名字, 《,》分割")
	private String specValue;

}
