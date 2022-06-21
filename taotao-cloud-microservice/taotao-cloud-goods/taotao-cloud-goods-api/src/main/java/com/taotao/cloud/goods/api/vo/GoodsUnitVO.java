package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品计量VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:06:43
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品计量VO")
public class GoodsUnitVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4433579132929428572L;

	private Long id;

	@Schema(description = "计量单位名称")
	private String name;
}
