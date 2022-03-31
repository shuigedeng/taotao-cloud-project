package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import lombok.NoArgsConstructor;

/**
 * 秒杀活动商品视图对象
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoodsVO implements Serializable {

    private static final long serialVersionUID = 5170316685407828228L;

    @Schema(description =  "活动id")
    private String seckillId;

    @Schema(description =  "时刻")
    private Integer timeLine;

    @Schema(description =  "商品id")
    private String goodsId;

    @Schema(description =  "以积分渠道购买需要积分数量")
    private Integer point;

    @Schema(description =  "skuID")
    private String skuId;

    @Schema(description =  "商品名称")
    private String goodsName;

    @Schema(description =  "商品图片")
    private String goodsImage;

    @Schema(description =  "商家id")
    private String storeId;

    @Schema(description =  "价格")
    private Double price;

    @Schema(description =  "促销数量")
    private Integer quantity;

    @Schema(description =  "已售数量")
    private Integer salesNum;

    @Schema(description =  "商品原始价格")
    private Double originalPrice;

}
