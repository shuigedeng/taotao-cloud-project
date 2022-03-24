package com.taotao.cloud.report.biz.entity.statistics.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品统计查询参数
 */
@Data
public class GoodsStatisticsQueryParam extends StatisticsQueryParam {

    @ApiModelProperty(value = "查询类型：按数量（NUM）、按金额（PRICE）")
    private String type;

}
