package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 直播间VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@RecordBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudioCommodityVO extends StudioVO {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	@Schema(description = "直播间商品列表")
	private List<CommodityVO> commodityList;

}
