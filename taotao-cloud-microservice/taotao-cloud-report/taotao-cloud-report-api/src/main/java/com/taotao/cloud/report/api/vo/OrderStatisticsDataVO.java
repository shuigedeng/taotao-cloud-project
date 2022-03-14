package com.taotao.cloud.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单统计数据VO
 */
@Data
public class OrderStatisticsDataVO {

    @Schema(description =  "店铺")
    private String storeName;

    @Schema(description =  "购买人")
    private String memberName;

    @Schema(description =  "订单金额")
    private Double price;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "创建时间")
    private Date createTime;

    @Schema(description =  "订单编号")
    private String orderItemSn;
}
