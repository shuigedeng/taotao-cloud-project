package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 积分商品实体类
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PointsGoods.TABLE_NAME)
@TableName(PointsGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PointsGoods.TABLE_NAME, comment = "积分商品实体类")
public class PointsGoods extends BasePromotions<PointsGoods, Long> {

	public static final String TABLE_NAME = "li_points_goods";

	@Schema(description = "商品编号")
	private String goodsId;

	@Schema(description = "商品sku编号")
	private String skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品原价")
	private BigDecimal originalPrice;

	@Schema(description = "结算价格")
	private BigDecimal settlementPrice;

	@Schema(description = "积分商品分类编号")
	private String pointsGoodsCategoryId;

	@Schema(description = "分类名称")
	private String pointsGoodsCategoryName;

	@Schema(description = "缩略图")
	private String thumbnail;

	@Schema(description = "活动库存数量")
	private Integer activeStock;

	@Schema(description = "兑换积分")
	private Long points;

}
