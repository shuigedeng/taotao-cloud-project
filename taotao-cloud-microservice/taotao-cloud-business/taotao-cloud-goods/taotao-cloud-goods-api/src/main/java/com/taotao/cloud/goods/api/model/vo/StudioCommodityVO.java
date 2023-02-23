package com.taotao.cloud.goods.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * 直播间VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudioCommodityVO extends StudioVO {

	@Schema(description = "直播间商品列表")
	private List<CommodityVO> commodityList;

}
