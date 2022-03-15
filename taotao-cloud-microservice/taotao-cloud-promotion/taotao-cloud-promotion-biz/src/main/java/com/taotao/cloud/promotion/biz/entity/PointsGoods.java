package com.taotao.cloud.promotion.biz.entity;

import cn.lili.modules.promotion.entity.dto.BasePromotions;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 积分商品实体类
 *
 * 
 **/
@Entity
@Table(name = PointsGoods.TABLE_NAME)
@TableName(PointsGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PointsGoods.TABLE_NAME, comment = "积分商品实体类")
public class PointsGoods extends BasePromotions<PointsGoods, Long> {

	public static final String TABLE_NAME = "li_points_goods";

    @Schema(description =  "商品编号")
    private String goodsId;

    @Schema(description =  "商品sku编号")
    private String skuId;

    @Schema(description =  "商品名称")
    private String goodsName;

    @Schema(description =  "商品原价")
    private Double originalPrice;

    @Schema(description =  "结算价格")
    private Double settlementPrice;

    @Schema(description =  "积分商品分类编号")
    private String pointsGoodsCategoryId;

    @Schema(description =  "分类名称")
    private String pointsGoodsCategoryName;

    @Schema(description =  "缩略图")
    private String thumbnail;

    @Schema(description =  "活动库存数量")
    private Integer activeStock;

    @Schema(description =  "兑换积分")
    private Long points;

}
