package com.taotao.cloud.promotion.api.vo.kanjia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 砍价商品视图对象
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsListVO {

    @Schema(description =  "砍价活动商品id")
    private String id;

    @Schema(description =  "货品名称")
    private String goodsName;

    @Schema(description =  "缩略图")
    private String thumbnail;

    @Schema(description =  "最低购买金额")
    private BigDecimal purchasePrice;

    @Schema(description =  "活动库存")
    private Integer stock;

}
