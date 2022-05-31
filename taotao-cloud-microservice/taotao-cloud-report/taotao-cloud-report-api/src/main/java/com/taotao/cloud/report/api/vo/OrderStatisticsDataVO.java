package com.taotao.cloud.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单统计数据VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticsDataVO {

    @Schema(description =  "店铺")
    private String storeName;

    @Schema(description =  "购买人")
    private String memberName;

    @Schema(description =  "订单金额")
    private BigDecimal price;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "创建时间")
    private LocalDateTime createTime;

    @Schema(description =  "订单编号")
    private String orderItemSn;
}
