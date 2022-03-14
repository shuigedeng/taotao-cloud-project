package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分商品视图对象
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class PointsGoodsVO extends PointsGoods {

    private static final long serialVersionUID = -5163709626742905057L;

    @Schema(description =  "商品规格详细信息")
    private GoodsSku goodsSku;

}
