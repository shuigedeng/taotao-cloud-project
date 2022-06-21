package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.common.model.PageModel;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存警告封装类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:39
 */
@RecordBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockWarningVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "库存警告数量")
	private Integer stockWarningNum;

	@Schema(description = "商品SKU列表")
	private PageModel<GoodsSkuVO> goodsSkuPage;
}
