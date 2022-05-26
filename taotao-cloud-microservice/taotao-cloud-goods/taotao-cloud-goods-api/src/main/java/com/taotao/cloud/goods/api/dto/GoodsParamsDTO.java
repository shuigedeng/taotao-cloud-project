package com.taotao.cloud.goods.api.dto;

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
 * @since 2022-04-25 16:31:29
 */
@Schema(description = "商品参数分组")
public record GoodsParamsDTO(
	@Schema(description = "分组id")
	Long groupId,

	@Schema(description = "分组名称")
	String groupName,

	@Valid
	@Schema(description = "分组内的商品参数列表")
	List<GoodsParamsItemDTO> goodsParamsItemDTOList
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = 4892783539320159200L;


}
