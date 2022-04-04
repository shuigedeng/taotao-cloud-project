package com.taotao.cloud.distribution.api.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分销员对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销订单查询对象")
public class DistributionOrderSearchParams extends PageParam {

    private static final long serialVersionUID = -8736018687663645064L;

    @Schema(description =  "分销员名称")
    private String distributionName;

    @Schema(description =  "订单sn")
    private String orderSn;

    @Schema(description =  "分销员ID", hidden = true)
    private String distributionId;

    @Schema(description =  "分销订单状态")
    private String distributionOrderStatus;

    @Schema(description =  "店铺ID")
    private String storeId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "开始时间")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "结束时间")
    private Date endTime;


    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        queryWrapper.like(StringUtils.isNotBlank(distributionName), "distribution_name", distributionName);
        queryWrapper.eq(StringUtils.isNotBlank(distributionOrderStatus), "distribution_order_status", distributionOrderStatus);
        queryWrapper.eq(StringUtils.isNotBlank(orderSn), "order_sn", orderSn);
        queryWrapper.eq(StringUtils.isNotBlank(distributionId), "distribution_id", distributionId);
        queryWrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        if (endTime != null && startTime != null) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        return queryWrapper;
    }

}
