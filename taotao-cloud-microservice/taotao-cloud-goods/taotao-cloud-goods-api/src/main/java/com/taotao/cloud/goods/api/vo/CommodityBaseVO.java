package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 小程序直播商品
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:43
 */
@Schema(description = "小程序直播商品VO")
public record CommodityBaseVO(

	/**
	 * 图片
	 */
	String goodsImage,

	/**
	 * 商品名称
	 */
	String name,

	/**
	 * 1：一口价（只需要传入price，price2不传）
	 * <p></p>
	 * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
	 * <p></p>
	 * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
	 */
	Integer priceType,

	/**
	 * 价格
	 */
	BigDecimal price,

	/**
	 * 价格2
	 */
	BigDecimal price2,

	/**
	 * 商品详情页的小程序路径
	 */
	String url,

	/**
	 * 微信程序直播商品ID
	 */
	Long liveGoodsId,

	/**
	 * 审核单ID
	 */
	Long auditId,

	/**
	 * 审核状态
	 */
	String auditStatus,

	/**
	 * 店铺ID
	 */
	Long storeId,

	/**
	 * 商品ID
	 */
	Long goodsId,

	/**
	 * skuId
	 */
	Long skuId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

}
