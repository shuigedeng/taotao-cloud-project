package com.taotao.cloud.payment.biz.daxpay.single.service.param.constant;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * 支付接口
 * @author xxm
 * @since 2024/7/14
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "支付接口")
public class ApiConstQuery {
    /** 接口编码 */
    @Schema(description = "编码")
    private String code;

    /** 接口名称 */
    @Schema(description = "接口名称")
    private String name;

    /** 接口地址 */
    @Schema(description = "接口地址")
    private String api;

}
