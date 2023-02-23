package com.taotao.cloud.file.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 统计查询参数
 */
@Data
@SuperBuilder
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
