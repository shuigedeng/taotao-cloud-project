package com.taotao.cloud.report.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品统计VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreStatisticsDataVO {

    @Schema(description =  "店铺ID")
    private String storeId;

    @Schema(description =  "店铺名称")
    private String storeName;

    @Schema(description =  "销售数量")
    private String num;

    @Schema(description =  "销售金额")
    private BigDecimal price;
}
