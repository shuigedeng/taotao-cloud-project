package com.taotao.cloud.report.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 分类统计VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatisticsDataVO {

    @Schema(description="一级分类ID")
    private String categoryId;

    @Schema(description="一级分类名称")
    private String categoryName;

    @Schema(description =  "销售数量")
    private String num;

    @Schema(description =  "销售金额")
    private BigDecimal price;
}
