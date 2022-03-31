package com.taotao.cloud.promotion.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import lombok.NoArgsConstructor;

/**
 * 砍价活动商品DTO
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class KanjiaActivityGoodsDTO extends KanjiaActivityGoods implements Serializable {
public class KanjiaActivityGoodsDTO  implements Serializable {


    private static final long serialVersionUID = 1969340823809319805L;

    //@Schema(description =  "商品规格详细信息")
    //private GoodsSku goodsSku;

}
