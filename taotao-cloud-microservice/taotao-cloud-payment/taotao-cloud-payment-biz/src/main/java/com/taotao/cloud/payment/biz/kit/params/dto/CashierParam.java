package com.taotao.cloud.payment.biz.kit.params.dto;

import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付参数
 *
 */
@Data
@ToString
public class CashierParam {

    @Schema(description =  "价格")
    private BigDecimal price;

    @Schema(description =  "支付title")
    private String title;

    @Schema(description =  "支付详细描述")
    private String detail;

    @Schema(description =  "订单sn集合")
    private String orderSns;

    @Schema(description =  "支持支付方式")
    private List<String> support;


    @Schema(description =  "订单创建时间")
    private Date createTime;

    @Schema(description =  "支付自动结束时间")
    private Long autoCancel;

    @Schema(description =  "剩余余额")
    private BigDecimal walletValue;

    public String getDetail() {
        if (StringUtil.isEmpty(detail)) {
            return "清单详细";
        }
        return StringUtil.filterSpecialChart(detail);
    }
}
