package com.taotao.cloud.goods.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存警告封装类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockWarningVO {

	@Schema(description = "库存警告数量")
	private Integer stockWarningNum;

	@Schema(description = "商品SKU列表")
	private IPage<GoodsSku> goodsSkuPage;

}
