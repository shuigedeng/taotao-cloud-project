package com.taotao.cloud.distribution.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分销佣金
 *
 * 
 * @since 2020-03-14 23:04:56
 */
@Data
@TableName("li_distribution_cash")
@ApiModel(value = "分销佣金")
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCash extends BaseEntity {

    private static final long serialVersionUID = -233580160480894288L;

    @Schema(description =  "分销佣金sn")
    private String sn;

    @Schema(description =  "分销员id")
    private String distributionId;

    @Schema(description =  "分销员名称")
    private String distributionName;

    @Schema(description =  "分销佣金")
    private BigDecimal price;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "支付时间")
    private Date payTime;

    @Schema(description =  "状态")
    private String distributionCashStatus;

    public DistributionCash(String sn, String distributionId, BigDecimal price, String memberName) {
        this.sn = sn;
        this.distributionId = distributionId;
        this.price = price;
        this.distributionCashStatus = WithdrawStatusEnum.APPLY.name();
        this.distributionName = memberName;
    }
}
