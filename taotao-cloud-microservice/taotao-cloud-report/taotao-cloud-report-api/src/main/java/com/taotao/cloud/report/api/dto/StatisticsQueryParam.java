package com.taotao.cloud.report.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计查询参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsQueryParam {

    @Schema(description =  "快捷搜索", allowableValues = "TODAY, YESTERDAY, LAST_SEVEN, LAST_THIRTY")
    private String searchType;

    @Schema(description =  "类型：年（YEAR）、月（MONTH）")
    private String timeType;

    @Schema(description =  "年份")
    private Integer year;

    @Schema(description =  "月份")
    private Integer month;

    @Schema(description =  "店铺ID")
    private String storeId;

}
