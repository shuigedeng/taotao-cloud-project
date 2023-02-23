package com.taotao.cloud.file.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 商品统计查询参数
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsStatisticsQueryParam extends StatisticsQueryParam {

    @Schema(description =  "查询类型：按数量（NUM）、按金额（PRICE）")
    private String type;

}
