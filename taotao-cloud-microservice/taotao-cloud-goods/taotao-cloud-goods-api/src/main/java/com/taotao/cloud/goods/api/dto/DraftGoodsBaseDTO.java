package com.taotao.cloud.goods.api.dto;

import com.taotao.cloud.goods.api.enums.DraftGoodsSaveType;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 草稿商品
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:12
 */
@RecordBuilder
public record DraftGoodsBaseDTO(
	/**
	 * 商品名称
	 */
	String goodsName,

	/**
	 * 商品价格
	 */
	BigDecimal price,

	/**
	 * 品牌id
	 */
	Long brandId,

	/**
	 * 分类path
	 */
	String categoryPath,

	/**
	 * 计量单位
	 */
	String goodsUnit,

	/**
	 * 卖点
	 */
	String sellingPoint,

	/**
	 * 上架状态
	 *
	 * @see GoodsStatusEnum
	 */
	String marketEnable,

	/**
	 * 详情
	 */
	String intro,

	/**
	 * 商品移动端详情
	 */
	String mobileIntro,

	/**
	 * 购买数量
	 */
	Integer buyCount,

	/**
	 * 库存
	 */
	Integer quantity,

	/**
	 * 可用库存
	 */
	Integer enableQuantity,

	/**
	 * 商品好评率
	 */
	BigDecimal grade,

	/**
	 * 缩略图路径
	 */
	String thumbnail,

	/**
	 * 大图路径
	 */
	String big,

	/**
	 * 小图路径
	 */
	String small,

	/**
	 * 原图路径
	 */
	String original,

	/**
	 * 店铺分类路径
	 */
	String storeCategoryPath,

	/**
	 * 评论数量
	 */
	Integer commentNum,

	/**
	 * 卖家id
	 */
	Long storeId,

	/**
	 * 卖家名字
	 */
	String storeName,

	/**
	 * 运费模板id
	 */
	Long templateId,

	/**
	 * 是否自营
	 */
	Boolean selfOperated,

	/**
	 * 商品视频
	 */
	String goodsVideo,

	/**
	 * 是否为推荐商品
	 */
	Boolean recommend,

	/**
	 * 销售模式
	 */
	String salesModel,

	/**
	 * 草稿商品保存类型
	 *
	 * @see DraftGoodsSaveType
	 */
	String saveType,

	/**
	 * 分类名称JSON
	 */
	String categoryNameJson,

	/**
	 * 商品参数JSON
	 */
	String goodsParamsListJson,

	/**
	 * 商品图片JSON
	 */
	String goodsGalleryListJson,

	/**
	 * sku列表JSON
	 */
	String skuListJson,

	/**
	 * 商品类型
	 *
	 * @see GoodsTypeEnum
	 */
	String goodsType
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
