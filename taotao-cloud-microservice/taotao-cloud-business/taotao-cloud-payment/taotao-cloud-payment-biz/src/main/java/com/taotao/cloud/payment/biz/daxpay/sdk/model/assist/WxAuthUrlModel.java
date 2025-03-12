package com.taotao.cloud.payment.biz.daxpay.sdk.model.assist;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信oauth2授权的url连接
 * @author xxm
 * @since 2024/2/10
 */
@Data
public class WxAuthUrlModel{

    /** 微信oauth2授权的url连接 */
    private String url;
}
