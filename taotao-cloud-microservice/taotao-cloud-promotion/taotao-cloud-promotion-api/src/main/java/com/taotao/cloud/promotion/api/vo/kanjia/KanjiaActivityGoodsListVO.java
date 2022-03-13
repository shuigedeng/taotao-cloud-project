package com.taotao.cloud.promotion.api.vo.kanjia;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 砍价商品视图对象
 *
 * 
 * @date 2021/1/13
 **/
@Data
public class KanjiaActivityGoodsListVO {

    @Schema(description =  "砍价活动商品id")
    private String id;

    @Schema(description =  "货品名称")
    private String goodsName;

    @Schema(description =  "缩略图")
    private String thumbnail;

    @Schema(description =  "最低购买金额")
    private Double purchasePrice;

    @Schema(description =  "活动库存")
    private Integer stock;

}
