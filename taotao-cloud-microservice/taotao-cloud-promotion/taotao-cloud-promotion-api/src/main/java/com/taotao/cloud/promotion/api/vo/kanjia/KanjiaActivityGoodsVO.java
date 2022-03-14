package com.taotao.cloud.promotion.api.vo.kanjia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 砍价商品视图对象
 **/
@Data
public class KanjiaActivityGoodsVO {

    @Schema(description =  "商品规格详细信息")
    private GoodsSku goodsSku;

    @Schema(description =  "最低购买金额")
    private Double purchasePrice;

    public Double getPurchasePrice() {
        if (purchasePrice < 0) {
            return 0D;
        }
        return purchasePrice;
    }

    @Schema(description =  "活动库存")
    private Integer stock;

}
