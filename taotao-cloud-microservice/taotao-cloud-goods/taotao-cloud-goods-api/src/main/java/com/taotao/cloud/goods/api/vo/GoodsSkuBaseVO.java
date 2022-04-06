package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品sku基础VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品sku基础VO")
public class GoodsSkuBaseVO {

	private Long id;

	@Schema(description = "商品id")
	private String goodsId;

	@Schema(description = "规格信息json")
	private String specs;

	@Schema(description = "规格信息")
	private String simpleSpecs;

	@Schema(description = "配送模版id")
	private String freightTemplateId;

	@Schema(description = "是否是促销商品")
	private Boolean isPromotion;

	@Schema(description = "促销价")
	private BigDecimal promotionPrice;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品编号")
	private String sn;

	@Schema(description = "品牌id")
	private String brandId;

	@Schema(description = "分类path")
	private String categoryPath;

	@Schema(description = "计量单位")
	private String goodsUnit;

	@Schema(description = "卖点")
	private String sellingPoint;

	@Schema(description = "重量")
	private BigDecimal weight;

	/**
	 * @see GoodsStatusEnum
	 */
	@Schema(description = "上架状态")
	private String marketEnable;

	@Schema(description = "商品详情")
	private String intro;

	@Schema(description = "商品价格")
	private BigDecimal price;

	@Schema(description = "成本价格")
	private BigDecimal cost;

	@Schema(description = "浏览数量")
	private Integer viewCount;

	@Schema(description = "购买数量")
	private Integer buyCount;

	@Schema(description = "库存")
	private Integer quantity;

	@Schema(description = "商品好评率")
	private BigDecimal grade;

	@Schema(description = "缩略图路径")
	private String thumbnail;

	@Schema(description = "大图路径")
	private String big;

	@Schema(description = "小图路径")
	private String small;

	@Schema(description = "原图路径")
	private String original;

	@Schema(description = "店铺分类id")
	private String storeCategoryPath;

	@Schema(description = "评论数量")
	private Integer commentNum;

	@Schema(description = "卖家id")
	private String storeId;

	@Schema(description = "卖家名字")
	private String storeName;

	@Schema(description = "运费模板id")
	private String templateId;

	/**
	 * @see GoodsAuthEnum
	 */
	@Schema(description = "审核状态")
	private String isAuth;

	@Schema(description = "审核信息")
	private String authMessage;

	@Schema(description = "下架原因")
	private String underMessage;

	@Schema(description = "是否自营")
	private Boolean selfOperated;

	@Schema(description = "商品移动端详情")
	private String mobileIntro;

	@Schema(description = "商品视频")
	private String goodsVideo;

	@Schema(description = "是否为推荐商品")
	private Boolean recommend;

	@Schema(description = "销售模式")
	private String salesModel;

	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	private String goodsType;
}
