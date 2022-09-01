package com.taotao.cloud.goods.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商品关联参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:36:45
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品参数分组")
public class GoodsParamsDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 4892783539320159200L;

	@Schema(description = "分组id")
	private Long groupId;

	@Schema(description = "分组名称")
	private String groupName;

	@Valid
	@Schema(description = "分组内的商品参数列表")
	private List<GoodsParamsItemDTO> goodsParamsItemDTOList;

}
