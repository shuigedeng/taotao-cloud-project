package com.taotao.cloud.payment.biz.daxpay.single.core.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * 微信OpenId查询结果
 * @author xxm
 * @since 2024/6/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信OpenId查询结果")
public class OpenIdResult {

    @Schema(description = "OpenId")
    private String openId;
}
