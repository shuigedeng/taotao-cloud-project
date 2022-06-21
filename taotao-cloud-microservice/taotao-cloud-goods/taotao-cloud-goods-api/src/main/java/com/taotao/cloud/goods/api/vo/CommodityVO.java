package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * 小程序直播商品
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:43
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "小程序直播商品VO")
public class CommodityVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	/**
	 * 图片
	 */
	private String goodsImage;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 1：一口价（只需要传入price，price2不传）
	 * <p></p>
	 * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
	 * <p></p>
	 * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
	 */
	private Integer priceType;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 价格2
	 */
	private BigDecimal price2;

	/**
	 * 商品详情页的小程序路径
	 */
	private String url;

	/**
	 * 微信程序直播商品ID
	 */
	private Long liveGoodsId;

	/**
	 * 审核单ID
	 */
	private Long auditId;

	/**
	 * 审核状态
	 */
	private String auditStatus;

	/**
	 * 店铺ID
	 */
	private Long storeId;

	/**
	 * 商品ID
	 */
	private Long goodsId;

	/**
	 * skuId
	 */
	private Long skuId;
}
