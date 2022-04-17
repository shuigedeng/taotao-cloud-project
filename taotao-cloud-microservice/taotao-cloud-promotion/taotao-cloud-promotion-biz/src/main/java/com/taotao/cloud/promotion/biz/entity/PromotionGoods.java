package com.taotao.cloud.promotion.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import lombok.Setter;

/**
 * 促销活动商品实体类
 *
 * 
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PromotionGoods.TABLE_NAME)
@TableName(PromotionGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PromotionGoods.TABLE_NAME, comment = "促销商品")
public class PromotionGoods extends BaseSuperEntity<PromotionGoods, Long> {

	public static final String TABLE_NAME = "li_promotion_goods";
	
    @Schema(description =  "商家ID")
    private String storeId;

    @Schema(description =  "商家名称")
    private String storeName;

    @Schema(description =  "商品id")
    private String goodsId;

    @Schema(description =  "商品SkuId")
    private String skuId;

    @Schema(description =  "商品名称")
    private String goodsName;

    @Schema(description =  "缩略图")
    private String thumbnail;

    @Schema(description =  "活动开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description =  "活动结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description =  "活动id")
    private String promotionId;

    /**
     * @see PromotionTypeEnum
     */
    @Schema(description =  "促销工具类型")
    private String promotionType;

    /**
     * @see GoodsTypeEnum
     */
    @Schema(description =  "商品类型")
    private String goodsType;

    @Schema(description =  "活动标题")
    private String title;

    @Schema(description =  "卖出的商品数量")
    private Integer num;

    @Schema(description =  "原价")
    private BigDecimal originalPrice;

    @Schema(description =  "促销价格")
    private BigDecimal price;

    @Schema(description =  "兑换积分")
    private Long points;

    @Schema(description =  "限购数量")
    private Integer limitNum;

    @Schema(description =  "促销库存")
    private Integer quantity;

    @Schema(description =  "分类path")
    private String categoryPath;

    /**
     * @see PromotionsScopeTypeEnum
     */
    @Schema(description =  "关联范围类型")
    private String scopeType = PromotionsScopeTypeEnum.PORTION_GOODS.name();


    @Schema(description =  "范围关联的id")
    private String scopeId;

    public PromotionGoods(GoodsSku sku) {
        if (sku != null) {
            BeanUtil.copyProperties(sku, this, "id", "price");
            this.skuId = sku.getId();
            this.originalPrice = sku.getPrice();
        }
    }

    public PromotionGoods(PointsGoods pointsGoods, GoodsSku sku) {
        if (pointsGoods != null) {
            BeanUtil.copyProperties(sku, this, "id");
            BeanUtil.copyProperties(pointsGoods, this, "id");
            this.promotionId = pointsGoods.getId();
            this.quantity = pointsGoods.getActiveStock();
            this.originalPrice = sku.getPrice();
        }
    }

    public PromotionGoods(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO) {
        if (kanjiaActivityGoodsDTO != null) {
            BeanUtil.copyProperties(kanjiaActivityGoodsDTO, this, "id");
            BeanUtil.copyProperties(kanjiaActivityGoodsDTO.getGoodsSku(), this, "id");
        }
    }
}
