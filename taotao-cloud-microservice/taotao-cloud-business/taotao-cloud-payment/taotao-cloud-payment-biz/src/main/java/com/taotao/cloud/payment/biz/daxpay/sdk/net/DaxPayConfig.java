package com.taotao.cloud.payment.biz.daxpay.sdk.net;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;

/**
 * 支付配置
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Accessors(chain=true)
public class DaxPayConfig {

    /** 服务地址 */
    private String serviceUrl;

    /** 应用号 */
    private String appId;

    /** 签名方式 */
    
    private SignTypeEnum signType = SignTypeEnum.HMAC_SHA256;

    /** 签名秘钥 */
    private String signSecret;

    /** 请求超时时间 */
    
    private int reqTimeout = 30000;


    public String getServiceUrl() {
        return StrUtil.removeSuffix(serviceUrl, "/");
    }
}
