package com.taotao.cloud.goods.api.query;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

import lombok.*;

/**
 * es商品搜索查询
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsSearchQuery extends PageParam {

	@Schema(description = "关键字")
	private String keyword;

	@Schema(description = "分类")
	private String categoryId;

	@Schema(description = "品牌,可以多选 品牌Id@品牌Id@品牌Id")
	private String brandId;

	@Schema(description = "是否为推荐商品")
	private Boolean recommend;

	@Schema(description = "价格", example = "10_30")
	private String price;

	@Schema(description = "属性:参数名_参数值@参数名_参数值", example = "屏幕类型_LED@屏幕尺寸_15英寸")
	private String prop;

	@Schema(description = "规格项列表")
	private List<String> nameIds;

	@Schema(description = "卖家id，搜索店铺商品的时候使用")
	private String storeId;

	@Schema(description = "商家分组id，搜索店铺商品的时候使用")
	private String storeCatId;

	@Schema(hidden = true)
	private Map<String, List<String>> notShowCol;

}
