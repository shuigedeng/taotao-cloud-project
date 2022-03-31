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

	private String id;

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private String goodsId;

	/**
	 * 规格信息json
	 */
	@Schema(description = "规格信息json")
	private String specs;

	/**
	 * 规格信息
	 */
	@Schema(description = "规格信息")
	private String simpleSpecs;

	/**
	 * 配送模版id
	 */
	@Schema(description = "配送模版id")
	private String freightTemplateId;

	/**
	 * 是否是促销商品
	 */
	@Schema(description = "是否是促销商品")
	private Boolean isPromotion;

	/**
	 * 促销价
	 */
	@Schema(description = "促销价")
	private BigDecimal promotionPrice;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String goodsName;

	/**
	 * 商品编号
	 */
	@Schema(description = "商品编号")
	private String sn;

	/**
	 * 品牌id
	 */
	@Schema(description = "品牌id")
	private String brandId;

	/**
	 * 分类path
	 */
	@Schema(description = "分类path")
	private String categoryPath;

	/**
	 * 计量单位
	 */
	@Schema(description = "计量单位")
	private String goodsUnit;

	/**
	 * 卖点
	 */
	@Schema(description = "卖点")
	private String sellingPoint;

	/**
	 * 重量
	 */
	@Schema(description = "重量")
	private BigDecimal weight;

	/**
	 * 上架状态
	 *
	 * @see GoodsStatusEnum
	 */
	@Schema(description = "上架状态")
	private String marketEnable;

	/**
	 * 商品详情
	 */
	@Schema(description = "商品详情")
	private String intro;

	/**
	 * 商品价格
	 */
	@Schema(description = "商品价格")
	private BigDecimal price;

	/**
	 * 成本价格
	 */
	@Schema(description = "成本价格")
	private BigDecimal cost;

	/**
	 * 浏览数量
	 */
	@Schema(description = "浏览数量")
	private Integer viewCount;

	/**
	 * 购买数量
	 */
	@Schema(description = "购买数量")
	private Integer buyCount;

	/**
	 * 库存
	 */
	@Schema(description = "库存")
	private Integer quantity;

	/**
	 * 商品好评率
	 */
	@Schema(description = "商品好评率")
	private BigDecimal grade;

	/**
	 * 缩略图路径
	 */
	@Schema(description = "缩略图路径")
	private String thumbnail;

	/**
	 * 大图路径
	 */
	@Schema(description = "大图路径")
	private String big;

	/**
	 * 小图路径
	 */
	@Schema(description = "小图路径")
	private String small;

	/**
	 * 原图路径
	 */
	@Schema(description = "原图路径")
	private String original;

	/**
	 * 店铺分类id
	 */
	@Schema(description = "店铺分类id")
	private String storeCategoryPath;

	/**
	 * 评论数量
	 */
	@Schema(description = "评论数量")
	private Integer commentNum;

	/**
	 * 卖家id
	 */
	@Schema(description = "卖家id")
	private String storeId;

	/**
	 * 卖家名字
	 */
	@Schema(description = "卖家名字")
	private String storeName;

	/**
	 * 运费模板id
	 */
	@Schema(description = "运费模板id")
	private String templateId;

	/**
	 * 审核状态
	 *
	 * @see GoodsAuthEnum
	 */
	@Schema(description = "审核状态")
	private String isAuth;

	/**
	 * 审核信息
	 */
	@Schema(description = "审核信息")
	private String authMessage;

	/**
	 * 下架原因
	 */
	@Schema(description = "下架原因")
	private String underMessage;

	/**
	 * 是否自营
	 */
	@Schema(description = "是否自营")
	private Boolean selfOperated;

	/**
	 * 商品移动端详情
	 */
	@Schema(description = "商品移动端详情")
	private String mobileIntro;

	/**
	 * 商品视频
	 */
	@Schema(description = "商品视频")
	private String goodsVideo;

	/**
	 * 是否为推荐商品
	 */
	@Schema(description = "是否为推荐商品")
	private Boolean recommend;

	/**
	 * 销售模式
	 */
	@Schema(description = "销售模式")
	private String salesModel;

	/**
	 * 商品类型
	 *
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	private String goodsType;
}
