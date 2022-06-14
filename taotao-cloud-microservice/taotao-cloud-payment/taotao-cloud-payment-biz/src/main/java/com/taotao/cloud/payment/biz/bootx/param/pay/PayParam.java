package com.taotao.cloud.payment.biz.bootx.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
* @author xxm
* @date 2020/12/9
*/
@Data
@Accessors(chain = true)
@Schema(title = "支付参数")
public class PayParam implements Serializable {
    private static final long serialVersionUID = 3895679513150533566L;

    @Schema(description= "用户ID")
    private Long userId;

    @Schema(description= "业务ID")
    private String businessId;

    @Schema(description= "支付标题")
    private String title;

    @Schema(description= "支付描述")
    private String description;

    @Schema(description= "支付信息",required = true)
    private List<PayModeParam> payModeList;
}
