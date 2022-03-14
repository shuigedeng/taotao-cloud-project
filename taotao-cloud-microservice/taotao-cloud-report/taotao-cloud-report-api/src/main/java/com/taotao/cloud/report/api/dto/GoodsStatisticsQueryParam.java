package com.taotao.cloud.report.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品统计查询参数
 */
@Data
public class GoodsStatisticsQueryParam extends StatisticsQueryParam {

    @Schema(description =  "查询类型：按数量（NUM）、按金额（PRICE）")
    private String type;

}
