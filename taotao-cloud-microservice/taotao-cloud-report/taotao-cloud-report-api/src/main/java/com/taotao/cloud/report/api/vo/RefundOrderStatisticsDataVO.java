package com.taotao.cloud.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退款统计VO
 *
 * @author Bulbasaur
 * @since 2020/12/10 11:24
 */
@Data
public class RefundOrderStatisticsDataVO {

    @Schema(description =  "售后SN")
    private String refundSn;

    @Schema(description =  "商家名称 ")
    private String storeName;

    @Schema(description =  "会员名称")
    private String memberName;

    @Schema(description =  "商品名称")
    private String name;

    @Schema(description =  "规格内容")
    private String specs;

    @Schema(description =  "实际退款金额")
    private Double finalPrice;
}
