package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品sku基础VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:15:33
 */
@RecordBuilder
@Schema(description = "商品sku基础VO")
public record GoodsSkuBaseVO(
	Long id,

	@Schema(description = "商品id")
	Long goodsId,

	@Schema(description = "规格信息json")
	String specs,

	@Schema(description = "规格信息")
	String simpleSpecs,

	@Schema(description = "配送模版id")
	Long freightTemplateId,

	@Schema(description = "是否是促销商品")
	Boolean promotionFlag,

	@Schema(description = "促销价")
	BigDecimal promotionPrice,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "商品编号")
	String sn,

	@Schema(description = "品牌id")
	Long brandId,

	@Schema(description = "分类path")
	String categoryPath,

	@Schema(description = "计量单位")
	String goodsUnit,

	@Schema(description = "卖点")
	String sellingPoint,

	@Schema(description = "重量")
	BigDecimal weight,

	/**
	 * @see GoodsStatusEnum
	 */
	@Schema(description = "上架状态")
	String marketEnable,

	@Schema(description = "商品详情")
	String intro,

	@Schema(description = "商品价格")
	BigDecimal price,

	@Schema(description = "成本价格")
	BigDecimal cost,

	@Schema(description = "浏览数量")
	Integer viewCount,

	@Schema(description = "购买数量")
	Integer buyCount,

	@Schema(description = "库存")
	Integer quantity,

	@Schema(description = "商品好评率")
	BigDecimal grade,

	@Schema(description = "缩略图路径")
	String thumbnail,

	@Schema(description = "大图路径")
	String big,

	@Schema(description = "小图路径")
	String small,

	@Schema(description = "原图路径")
	String original,

	@Schema(description = "店铺分类id")
	String storeCategoryPath,

	@Schema(description = "评论数量")
	Integer commentNum,

	@Schema(description = "卖家id")
	Long storeId,

	@Schema(description = "卖家名字")
	String storeName,

	@Schema(description = "运费模板id")
	String templateId,

	/**
	 * @see GoodsAuthEnum
	 */
	@Schema(description = "审核状态")
	String isAuth,

	@Schema(description = "审核信息")
	String authMessage,

	@Schema(description = "下架原因")
	String underMessage,

	@Schema(description = "是否自营")
	Boolean selfOperated,

	@Schema(description = "商品移动端详情")
	String mobileIntro,

	@Schema(description = "商品视频")
	String goodsVideo,

	@Schema(description = "是否为推荐商品")
	Boolean recommend,

	@Schema(description = "销售模式")
	String salesModel,

	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	String goodsType
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 1450550797436233753L;


}
