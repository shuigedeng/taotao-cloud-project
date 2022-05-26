package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品基础VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 17:21:04
 */
@Schema(description = "商品基础VO")
public record GoodsBaseVO(

	Long id,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "商品价格")
	BigDecimal price,

	@Schema(description = "品牌id")
	String brandId,

	@Schema(description = "分类path")
	String categoryPath,

	@Schema(description = "计量单位")
	String goodsUnit,

	@Schema(description = "商品卖点太长")
	String sellingPoint,

	@Schema(description = "上架状态")
	String marketEnable,

	@Schema(description = "详情")
	String intro,

	@Schema(description = "购买数量")
	Integer buyCount,

	@Schema(description = "库存")
	Integer quantity,

	@Schema(description = "商品好评率")
	BigDecimal grade,

	@Schema(description = "缩略图路径")
	String thumbnail,

	@Schema(description = "小图路径")
	String small,

	@Schema(description = "原图路径")
	String original,

	@Schema(description = "店铺分类id")
	String storeCategoryPath,

	@Schema(description = "评论数量")
	Integer commentNum,

	@Schema(description = "卖家id")
	String storeId,

	@Schema(description = "卖家名字")
	String storeName,

	@Schema(description = "运费模板id")
	String templateId,

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

	@Schema(description = "商品海报id")
	Long posterPicId,

	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	String goodsType,

	@Schema(description = "商品参数json")
	String params,

	Boolean delFlag
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 1450550797436233753L;

}
