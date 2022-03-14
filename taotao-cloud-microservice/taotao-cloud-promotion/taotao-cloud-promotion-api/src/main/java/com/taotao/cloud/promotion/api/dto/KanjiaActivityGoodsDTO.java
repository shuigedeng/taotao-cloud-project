package com.taotao.cloud.promotion.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 砍价活动商品DTO
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class KanjiaActivityGoodsDTO extends KanjiaActivityGoods implements Serializable {


    private static final long serialVersionUID = 1969340823809319805L;

    @Schema(description =  "商品规格详细信息")
    private GoodsSku goodsSku;

}
