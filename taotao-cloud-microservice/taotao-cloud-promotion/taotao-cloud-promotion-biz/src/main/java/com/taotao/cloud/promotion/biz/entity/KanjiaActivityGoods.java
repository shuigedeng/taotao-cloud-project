package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * 砍价活动商品实体类
 *
 */
@Entity
@Table(name = KanjiaActivityGoods.TABLE_NAME)
@TableName(KanjiaActivityGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = KanjiaActivityGoods.TABLE_NAME, comment = "砍价活动商品对象")
public class KanjiaActivityGoods extends BasePromotions<KanjiaActivityGoods, Long> {

	public static final String TABLE_NAME = "li_kanjia_activity_goods";

	@Schema(description =  "结算价格")
	@NotEmpty(message = "结算价格不能为空")
	private Double settlementPrice;

	@Schema(description =  "商品原价")
	private Double originalPrice;

	@Schema(description =  "最低购买金额")
	@NotEmpty(message = "最低购买金额不能为空")
	private Double purchasePrice;

	@Schema(description =  "货品id")
	@NotEmpty(message = "货品id不能为空")
	private String goodsId;

	@Schema(description =  "货品SkuId")
	@NotEmpty(message = "货品SkuId不能为空")
	private String skuId;

	@Schema(description =  "货品名称")
	private String goodsName;

	@Schema(description =  "缩略图")
	private String thumbnail;

	@Schema(description =  "活动库存")
	@NotEmpty(message = "活动库存不能为空")
	private Integer stock;

	@Schema(description =  "每人最低砍价金额")
	@NotEmpty(message = "每人最低砍价金额不能为空")
	private Double lowestPrice;

	@Schema(description =  "每人最高砍价金额")
	@NotEmpty(message = "每人最高砍价金额不能为空")
	private Double highestPrice;
}
