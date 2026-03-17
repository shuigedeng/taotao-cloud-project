package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.core.result;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户应用基础返回结果
 * @author xxm
 * @since 2024/7/20
 */
@EqualsAndHashCode(callSuper = true)
@Data

@Schema(title = "商户应用基础返回结果")
public class MchAppResult extends BaseResult {

    @Schema(description = "应用AppId")
    private String appId;

}
