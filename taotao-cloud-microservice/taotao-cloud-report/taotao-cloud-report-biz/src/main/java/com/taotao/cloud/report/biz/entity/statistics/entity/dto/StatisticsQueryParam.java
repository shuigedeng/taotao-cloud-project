package com.taotao.cloud.report.biz.entity.statistics.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计查询参数
 */
@Data
public class StatisticsQueryParam {

    @ApiModelProperty(value = "快捷搜索", allowableValues = "TODAY, YESTERDAY, LAST_SEVEN, LAST_THIRTY")
    private String searchType;

    @ApiModelProperty(value = "类型：年（YEAR）、月（MONTH）")
    private String timeType;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "店铺ID")
    private String storeId;

}
