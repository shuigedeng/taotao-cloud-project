package com.taotao.cloud.report.biz.entity.statistics.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品统计VO
 */
@Data
public class GoodsStatisticsDataVO {

    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "销售数量")
    private String num;

    @ApiModelProperty(value = "销售金额")
    private Double price;
}
