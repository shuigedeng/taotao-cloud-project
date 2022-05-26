package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 直播间VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@RecordBuilder
public record StudioVO(
	@Schema(description = "直播间商品列表")
	List<CommodityBaseVO> commodityList,

	@Schema(description = "小程序直播间基")
	StudioBaseVO studioBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

}
