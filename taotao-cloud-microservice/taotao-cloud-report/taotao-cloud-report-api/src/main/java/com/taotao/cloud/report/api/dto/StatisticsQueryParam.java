package com.taotao.cloud.report.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计查询参数
 *
 * @author Bulbasaur
 * @since 2020/12/9 14:20
 */
@Data
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
