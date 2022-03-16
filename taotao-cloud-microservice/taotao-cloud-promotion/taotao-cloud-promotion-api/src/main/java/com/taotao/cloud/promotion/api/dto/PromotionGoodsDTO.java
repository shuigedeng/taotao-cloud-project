package com.taotao.cloud.promotion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 促销商品数据传输对象
 **/
@Data
@NoArgsConstructor
//public class PromotionGoodsDTO extends PromotionGoods {
public class PromotionGoodsDTO  {

    private static final long serialVersionUID = 9206970681612883421L;

    @Schema(description =  "商品id")
    private String goodsId;

    @Schema(description =  "商品名称")
    private String goodsName;

    @Schema(description =  "商品图片")
    private String goodsImage;

    //public PromotionGoodsDTO(GoodsSku sku) {
    //    super(sku);
    //}
}
