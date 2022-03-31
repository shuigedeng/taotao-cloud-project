package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品关联参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品参数分组")
public class GoodsParamsDTO implements Serializable {

	private static final long serialVersionUID = 4892783539320159200L;

	@Schema(description = "分组id")
	private String groupId;

	@Schema(description = "分组名称")
	private String groupName;

	@Schema(description = "分组内的商品参数列表")
	private List<GoodsParamsItemDTO> goodsParamsItemDTOList;

}
